/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.search.nibrs.xmlfile.importer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ParsedObject;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.model.AbstractReport;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.BadSegmentLevelReport;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.model.OffenderSegment;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.PropertySegment;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.ZeroReport;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.xmlfile.util.NibrsStringUtils;
import org.search.nibrs.xmlfile.util.XmlUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
/**
 * Builder class that constructs incidents from a stream of NIBRS report data.
 * Incidents are broadcast to listeners as events; this keeps the class as
 * memory-unintensive as possible (NIBRS report streams can be rather large).
 * <br/>
 * At some point, if other report elements than Incidents are desired, this will
 * need to be modified. Currently, it only broadcasts Incident "add" records.
 * 
 */
@Component
public class XmlIncidentBuilder {
	private static final Log log = LogFactory.getLog(XmlIncidentBuilder.class);;
	
	private static final class LogListener implements ReportListener {
		public int reportCount = 0;
		public int errorCount = 0;
		public void newReport(AbstractReport newReport, List<NIBRSError> errorList) {
			log.info("Created " + newReport.getUniqueReportDescription());
			reportCount++;
			errorCount += errorList.size();
		}
	}
	
	private List<ReportListener> listeners;
	private LogListener logListener = new LogListener();
	private DateFormat dateFormat;
	private DocumentBuilder documentBuilder; 

	public XmlIncidentBuilder() throws ParserConfigurationException {
		listeners = new ArrayList<ReportListener>();
		listeners.add(logListener);
		dateFormat = new SimpleDateFormat("yyyyMMdd");
		dateFormat.setLenient(false);
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setIgnoringComments(false);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		documentBuilderFactory.setNamespaceAware(true);

		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		documentBuilder.setEntityResolver(new NullResolver());
	}

	public void addIncidentListener(ReportListener listener) {
		listeners.add(listener);
	}

	public void removeIncidentListener(ReportListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Read NIBRS incidents in the XML format from the input stream.
	 * @param reader the source of the data
	 * @throws Exception 
	 */
	public void buildIncidents(InputStream inputStream, String readerLocationName) {

		AbstractReport currentReport = null;
		
		log.info("Processing NIBRS XML file");
		
		List<NIBRSError> errorList = new ArrayList<NIBRSError>();
		
		Document document;
		
		try {
			document = documentBuilder.parse(inputStream);
			NodeList reportElements = (NodeList) XmlUtils.xPathNodeListSearch(document, "nibrs:Submission/nibrs:Report");
			
			for(int i=0; i < reportElements.getLength(); i++){
				ReportSource reportSource = new ReportSource();
				reportSource.setSourceLocation(String.valueOf(i+1));
				reportSource.setSourceName(readerLocationName);
				
				Element reportNode = (Element)reportElements.item(i);
				
				ReportBaseData reportBaseData = new ReportBaseData();
				List<NIBRSError> reportBaseDataErrors = reportBaseData.setData(reportSource, reportNode);
				errorList.addAll(reportBaseDataErrors);

				currentReport = buildReport(errorList, reportNode, readerLocationName, reportBaseData);
				errorList = new ArrayList<NIBRSError>();
				handleNewReport(currentReport, errorList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		
		
		
//		while ((line = br.readLine()) != null) {
//			Segment s = new Segment();
//			ReportSource reportSource = new ReportSource();
//			reportSource.setSourceLocation(String.valueOf(lineNumber));
//			reportSource.setSourceName(readerLocationName);
//			List<NIBRSError> segmentErrors = s.setData(reportSource, line);
//			errorList.addAll(segmentErrors);
//			if (segmentErrors.isEmpty()) {
//				char level = s.getSegmentLevel();
//				if (level == ZeroReport.ZERO_REPORT_TYPE_IDENTIFIER 
//						|| level == GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER 
//						|| level == ArresteeSegment.GROUP_B_ARRESTEE_SEGMENT_TYPE_IDENTIFIER 
//						|| !Objects.equals(currentReport.getIdentifier(), s.getSegmentUniqueIdentifier())) {
//					handleNewReport(currentReport, errorList);
//					errorList = new ArrayList<NIBRSError>();
//					currentReport = buildReport(errorList, s, readerLocationName);
//				} else {
//					int errorListSize = errorList.size();
//					if (currentReport instanceof GroupAIncidentReport){
//						addSegmentToIncident((GroupAIncidentReport) currentReport, s, errorList);
//					}
//					if (errorList.size() > errorListSize && currentReport != null) {
//						currentReport.setHasUpstreamErrors(true);
//					}
//				}
//			}
//			lineNumber++;
//		}
//		
		log.info("finished processing file");
		log.info("Encountered " + logListener.errorCount + " error(s).");
		log.info("Created " + logListener.reportCount + " incident(s).");

	}

	AbstractReport buildReport(List<NIBRSError> errorList, Element reportElement, String readerLocationName, ReportBaseData reportBaseData) throws Exception {
		int errorListSize = errorList.size();
		AbstractReport ret = null;
		
		String nibrsReportCategoryCode = XmlUtils.xPathStringSearch(reportElement, "nibrs:ReportHeader/nibrs:NIBRSReportCategoryCode"); 
		
		switch (nibrsReportCategoryCode){
		case "GROUP A INCIDENT REPORT":
			ret = buildGroupAIncidentReport(reportElement, errorList); 
			break; 
		case "GROUP B ARREST REPORT": 
			ret = builGroupBArrestReport(reportElement, errorList);
			break; 
		case "ZERO REPORT": 
			ret = buildZeroReport(reportBaseData, errorList); 
			break;
		}
		
		if (errorList.size() > errorListSize) {
			ret.setHasUpstreamErrors(true);
		}
		ret.setSource(reportBaseData.getReportSource());
		return ret;
	}

	private AbstractReport buildZeroReport(ReportBaseData reportBaseData, List<NIBRSError> errorList) throws Exception {
		List<NIBRSError> newErrorList = new ArrayList<>();
		ZeroReport ret = new ZeroReport();
		ret.setOri(reportBaseData.getOri());
		ret.setReportActionType(reportBaseData.getActionType());
		ret.setIncidentNumber(reportBaseData.getIncidentNumber());
		
		String submissionDateString = XmlUtils.xPathStringSearch(reportBaseData.getReportElement(), "nibrs:ReportHeader/nibrs:ReportDate/nc:YearMonthDate");
		
		try {
			if (StringUtils.isNotBlank(submissionDateString) && submissionDateString.length() == 7){
				YearMonth submissionDate = YearMonth.parse(submissionDateString);
				ret.setYearOfTape(submissionDate.getYear());
				ret.setMonthOfTape(submissionDate.getMonth().getValue());
			}
		}
		catch (DateTimeParseException e){
			log.info(e);
			NIBRSError nibrsError = new NIBRSError();
			nibrsError.setContext(reportBaseData.getReportSource());
			nibrsError.setReportUniqueIdentifier(reportBaseData.getIncidentNumber());
			nibrsError.setNIBRSErrorCode(NIBRSErrorCode._001);
			nibrsError.setValue(submissionDateString);
			nibrsError.setSegmentType('0');
			errorList.add(nibrsError);
			log.debug("Error in DateTimeParse conversion: lineNumber=" + reportBaseData.getReportSource()
				+ ", xPath = 'nibrs:ReportHeader/nibrs:ReportDate/nc:YearMonthDate'"
				+ ", value=" + StringUtils.trimToEmpty(submissionDateString));

		}
		
		//TODO find out Zero Report Year and Zero report Month xPath.
		//TODO find out city indicator's xPath
		//ret.setCityIndicator(StringUtils.getStringBetween(13, 16, s.getData()));
			
		for (NIBRSError e : newErrorList) {
			e.setReport(ret);
		}
		
		errorList.addAll(newErrorList);
		
		return ret;
	}

	private AbstractReport builGroupBArrestReport(Element reportElement, List<NIBRSError> errorList) {
		// TODO Auto-generated method stub
		return null;
	}

	private AbstractReport buildGroupAIncidentReport(Element reportElement, List<NIBRSError> errorList) {
		// TODO Auto-generated method stub
		return null;
	}

	private ZeroReport buildZeroReport(Segment s, List<NIBRSError> errorList) {
		
		List<NIBRSError> newErrorList = new ArrayList<>();
		ZeroReport ret = new ZeroReport();
		ret.setOri(s.getOri());
		ret.setReportActionType(s.getActionType());
		int length = s.getSegmentLength();
		
		if (length == 43) {
			ret.setMonthOfTape(getIntValueFromSegment(s, 7, 8, newErrorList, NIBRSErrorCode._001));
			ret.setYearOfTape(getIntValueFromSegment(s, 9, 12, newErrorList, NIBRSErrorCode._001));
			ret.setCityIndicator(NibrsStringUtils.getStringBetween(13, 16, s.getData()));
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getReportSource());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._001);
			newErrorList.add(e);
		}
		
		for (NIBRSError e : newErrorList) {
			e.setReport(ret);
		}
		
		errorList.addAll(newErrorList);
		
		return ret;
		
	}

	private AbstractReport buildGroupBIncidentReport(Segment s, List<NIBRSError> errorList) {
		List<NIBRSError> newErrorList = new ArrayList<>();
		GroupBArrestReport ret = new GroupBArrestReport();
		ArresteeSegment arrestee = new ArresteeSegment(ArresteeSegment.GROUP_B_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		String segmentData = s.getData();
		ret.setOri(s.getOri());
		ret.setReportActionType(s.getActionType());
		int length = s.getSegmentLength();
		if (length == 66) {
			ret.setMonthOfTape(getIntValueFromSegment(s, 7, 8, newErrorList, NIBRSErrorCode._701));
			ret.setYearOfTape(getIntValueFromSegment(s, 9, 12, newErrorList, NIBRSErrorCode._701));
			ret.setCityIndicator(NibrsStringUtils.getStringBetween(13, 16, segmentData));
			
			ParsedObject<Integer> sequenceNumber = arrestee.getArresteeSequenceNumber();
			sequenceNumber.setMissing(false);
			sequenceNumber.setInvalid(false);
			String sequenceNumberString = NibrsStringUtils.getStringBetween(38, 39, segmentData);
			if (sequenceNumberString == null) {
				sequenceNumber.setMissing(true);
				sequenceNumber.setValue(null);
			} else {
				try {
					Integer sequenceNumberI = Integer.parseInt(sequenceNumberString);
					sequenceNumber.setValue(sequenceNumberI);
				} catch (NumberFormatException nfe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(sequenceNumberString);
					e.setNIBRSErrorCode(NIBRSErrorCode._701);
					e.setDataElementIdentifier("40");
					errorList.add(e);
					sequenceNumber.setInvalid(true);
					sequenceNumber.setValidationError(e);
				}
			}
			
			arrestee.setArresteeSequenceNumber(sequenceNumber);
			
			arrestee.setArrestTransactionNumber(NibrsStringUtils.getStringBetween(26, 37, segmentData));
			
			ParsedObject<Date> arrestDate = arrestee.getArrestDate();
			arrestDate.setMissing(false);
			arrestDate.setInvalid(false);
			String arrestDateString = NibrsStringUtils.getStringBetween(40, 47, segmentData);
			if (arrestDateString == null) {
				arrestDate.setMissing(true);
				arrestDate.setValue(null);
			} else {
				try {
					Date d = dateFormat.parse(arrestDateString);
					arrestDate.setValue(d);
				} catch (ParseException pe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(arrestDateString);
					e.setNIBRSErrorCode(NIBRSErrorCode._705);
					e.setDataElementIdentifier("42");
					newErrorList.add(e);
					arrestDate.setInvalid(true);
					arrestDate.setValidationError(e);
				}
			}
			arrestee.setArrestDate(arrestDate);
			
			arrestee.setTypeOfArrest(NibrsStringUtils.getStringBetween(48, 48, segmentData));
			arrestee.setUcrArrestOffenseCode(NibrsStringUtils.getStringBetween(49, 51, segmentData));
			for (int i = 0; i < 2; i++) {
				arrestee.setArresteeArmedWith(i, NibrsStringUtils.getStringBetween(52 + 3 * i, 53 + 3 * i, segmentData));
				arrestee.setAutomaticWeaponIndicator(i, NibrsStringUtils.getStringBetween(54 + 3 * i, 54 + 3 * i, segmentData));
			}
			arrestee.setAgeString(NibrsStringUtils.getStringBetween(58, 61, segmentData));
			arrestee.setSex(NibrsStringUtils.getStringBetween(62, 62, segmentData));
			arrestee.setRace(NibrsStringUtils.getStringBetween(63, 63, segmentData));
			arrestee.setEthnicity(NibrsStringUtils.getStringBetween(64, 64, segmentData));
			arrestee.setResidentStatus(NibrsStringUtils.getStringBetween(65, 65, segmentData));
			arrestee.setDispositionOfArresteeUnder18(NibrsStringUtils.getStringBetween(66, 66, segmentData));
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getReportSource());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._701);
			newErrorList.add(e);
		}
		
		for (NIBRSError e : newErrorList) {
			e.setReport(ret);
		}
		
		ret.addArrestee(arrestee);
		errorList.addAll(newErrorList);
	
		return ret;
	}

	private final void handleNewReport(AbstractReport newReport, List<NIBRSError> errorList) {
		if (newReport != null) {
			for (Iterator<ReportListener> it = listeners.iterator(); it.hasNext();) {
				ReportListener listener = it.next();
				listener.newReport(newReport, errorList);
			}
		}
	}

	private final AbstractReport buildGroupAIncidentSegment(Segment s, List<NIBRSError> errorList) {
		List<NIBRSError> newErrorList = new ArrayList<>();
		GroupAIncidentReport newIncident = new GroupAIncidentReport();
		newIncident.setIncidentNumber(s.getSegmentUniqueIdentifier());
		newIncident.setOri(s.getOri());
		newIncident.setReportActionType(s.getActionType());
		String segmentData = s.getData();
		int length = s.getSegmentLength();
		if (length == 87 || length == 88) {
			newIncident.setMonthOfTape(getIntValueFromSegment(s, 7, 8, newErrorList, NIBRSErrorCode._101));
			newIncident.setYearOfTape(getIntValueFromSegment(s, 9, 12, newErrorList, NIBRSErrorCode._101));
			newIncident.setCityIndicator(NibrsStringUtils.getStringBetween(13, 16, segmentData));
			ParsedObject<Date> incidentDate = newIncident.getIncidentDate();
			incidentDate.setMissing(false);
			incidentDate.setInvalid(false);
			String incidentDateString = NibrsStringUtils.getStringBetween(38, 45, segmentData);
			if (incidentDateString == null) {
				incidentDate.setMissing(true);
				incidentDate.setValue(null);
			} else {
				try {
					Date d = dateFormat.parse(incidentDateString);
					incidentDate.setValue(d);
				} catch (ParseException pe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(incidentDateString);
					e.setNIBRSErrorCode(NIBRSErrorCode._105);
					e.setDataElementIdentifier("3");
					newErrorList.add(e);
					incidentDate.setInvalid(true);
					incidentDate.setValidationError(e);
				}
			}
			newIncident.setIncidentDate(incidentDate);
			
			newIncident.setReportDateIndicator(NibrsStringUtils.getStringBetween(46, 46, segmentData));
			
			String hourString = NibrsStringUtils.getStringBetween(47, 48, segmentData);
			ParsedObject<Integer> hour = newIncident.getIncidentHour();
			hour.setMissing(false);
			hour.setInvalid(false);
			if (hourString != null && hourString.trim().length() > 0) {
				try {
					Integer hourI = new Integer(hourString);
					hour.setValue(hourI);
				} catch(NumberFormatException nfe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(hourString);
					e.setNIBRSErrorCode(NIBRSErrorCode._104);
					e.setDataElementIdentifier("3");
					newErrorList.add(e);
					hour.setInvalid(true);
					hour.setValidationError(e);
				}
			} else {
				hour.setMissing(true);
			}
			
			newIncident.setExceptionalClearanceCode(NibrsStringUtils.getStringBetween(49, 49, segmentData));
			
			ParsedObject<Date> clearanceDate = newIncident.getExceptionalClearanceDate();
			clearanceDate.setMissing(false);
			clearanceDate.setInvalid(false);
			String clearanceDateString = NibrsStringUtils.getStringBetween(50, 57, segmentData);
			if (clearanceDateString == null) {
				clearanceDate.setMissing(true);
				clearanceDate.setValue(null);
			} else {
				try {
					Date d = dateFormat.parse(clearanceDateString);
					clearanceDate.setValue(d);
				} catch (ParseException pe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(clearanceDateString);
					e.setNIBRSErrorCode(NIBRSErrorCode._105);
					e.setDataElementIdentifier("5");
					newErrorList.add(e);
					incidentDate.setInvalid(true);
					incidentDate.setValidationError(e);
				}
			}
			newIncident.setExceptionalClearanceDate(clearanceDate);
			
			boolean cargoTheft = length == 88;
			if (cargoTheft) {
				String cargoTheftYN = NibrsStringUtils.getStringBetween(88, 88, segmentData);
				
				if (org.apache.commons.lang3.StringUtils.isNotBlank(cargoTheftYN)){
					newIncident.setCargoTheftIndicator(cargoTheftYN);
					newIncident.setIncludesCargoTheft(true);
				}
				else{
					//TODO comment out temporarily  --hw
//					NIBRSError e = new NIBRSError();
//					e.setContext(s.getReportSource());
//					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
//					e.setSegmentType(s.getSegmentType());
//					e.setDataElementIdentifier("2A");
//					e.setNIBRSErrorCode(NIBRSErrorCode._101);
//					newErrorList.add(e);
				}
			}
			
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getReportSource());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._101);
			newErrorList.add(e);
		}
		for (NIBRSError e : newErrorList) {
			e.setReport(newIncident);
		}
		errorList.addAll(newErrorList);
		return newIncident;
	}
	private final AbstractReport buildBadSegmentLevelIncidentSegment(Segment s, List<NIBRSError> errorList) {
		List<NIBRSError> newErrorList = new ArrayList<>();
		BadSegmentLevelReport newIncident = new BadSegmentLevelReport();
		newIncident.setIncidentNumber(s.getSegmentUniqueIdentifier());
		newIncident.setOri(s.getOri());
		newIncident.setReportActionType(s.getActionType());
		String segmentData = s.getData();
		int length = s.getSegmentLength();
		if (length >=38 ) {
			newIncident.setMonthOfTape(getIntValueFromSegment(s, 7, 8, newErrorList, NIBRSErrorCode._101));
			newIncident.setYearOfTape(getIntValueFromSegment(s, 9, 12, newErrorList, NIBRSErrorCode._101));
			newIncident.setCityIndicator(NibrsStringUtils.getStringBetween(13, 16, segmentData));
		}
		
		NIBRSError e = new NIBRSError();
		e.setContext(s.getReportSource());
		e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
		e.setNIBRSErrorCode(NIBRSErrorCode._050);
		e.setCrossSegment(true);
		newErrorList.add(e);
		
		e.setReport(newIncident);
		errorList.addAll(newErrorList);
		return newIncident;
	}

	private Integer getIntValueFromSegment(Segment s, int startPos, int endPos, List<NIBRSError> errorList, NIBRSErrorCode errorCode) {
		String sv = NibrsStringUtils.getStringBetween(startPos, endPos, s.getData());
		Integer i = null;
		try {
			i = new Integer(sv);
		} catch (NumberFormatException nfe) {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getReportSource());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setNIBRSErrorCode(errorCode);
			e.setValue(sv);
			e.setSegmentType(s.getSegmentType());
			errorList.add(e);
			log.debug("Error in int conversion: lineNumber=" + s.getReportSource() + ", value=" + sv);
		}
		return i;
	}

	private Integer getIntValueFromXpath(ReportBaseData reportBaseData, String xPath,  List<NIBRSError> errorList, NIBRSErrorCode errorCode) 
			throws Exception {
		Element parent = reportBaseData.getReportElement();
		String value = XmlUtils.xPathStringSearch(parent, xPath);
		Integer i = null;
		try {
			i = new Integer(value);
		} catch (NumberFormatException nfe) {
			NIBRSError e = new NIBRSError();
			e.setContext(reportBaseData.getReportSource());
			e.setReportUniqueIdentifier(reportBaseData.getIncidentNumber());
			e.setNIBRSErrorCode(errorCode);
			e.setValue(value);
			e.setSegmentType(reportBaseData.getSegmentLevel());
			errorList.add(e);
			log.debug("Error in int conversion: position =" + reportBaseData.getReportSource()
				+ ", xPath = '" + xPath
				+ "', value=" + value);
		}
		return i;
	}
	
	private final void addSegmentToIncident(GroupAIncidentReport currentIncident, Segment s, List<NIBRSError> errorList) {
		if (Objects.isNull(currentIncident)) return; 
		
		List<NIBRSError> newErrorList = new ArrayList<>();
		char segmentType = s.getSegmentType();
		switch (segmentType) {
		case OffenseSegment.OFFENSE_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addOffense(buildOffenseSegment(s, newErrorList));
			break;
		case PropertySegment.PROPERTY_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addProperty(buildPropertySegment(s, newErrorList));
			break;
		case VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addVictim(buildVictimSegment(s, currentIncident, newErrorList));
			break;
		case OffenderSegment.OFFENDER_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addOffender(buildOffenderSegment(s, newErrorList));
			break;
		case ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addArrestee(buildGroupAArresteeSegment(s, newErrorList));
			break;
		default:
			NIBRSError error = new NIBRSError();
			error.setContext(s.getReportSource());
			error.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			error.setNIBRSErrorCode(NIBRSErrorCode._051);
			error.setValue(segmentType);
			newErrorList.add(error);
		}
		for (NIBRSError e : newErrorList) {
			e.setReport(currentIncident);
		}
		errorList.addAll(newErrorList);
	}

	private ArresteeSegment buildGroupAArresteeSegment(Segment s, List<NIBRSError> errorList) {
		ArresteeSegment newArrestee = new ArresteeSegment(ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER);
		String segmentData = s.getData();
		int length = s.getSegmentLength();
		if (length == 110) {
			
			ParsedObject<Integer> sequenceNumber = newArrestee.getArresteeSequenceNumber();
			sequenceNumber.setMissing(false);
			sequenceNumber.setInvalid(false);
			String sequenceNumberString = NibrsStringUtils.getStringBetween(38, 39, segmentData);
			if (sequenceNumberString == null) {
				sequenceNumber.setMissing(true);
				sequenceNumber.setValue(null);
			} else {
				try {
					Integer sequenceNumberI = Integer.parseInt(sequenceNumberString);
					sequenceNumber.setValue(sequenceNumberI);
				} catch (NumberFormatException nfe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(sequenceNumberString);
					e.setNIBRSErrorCode(NIBRSErrorCode._601);
					e.setDataElementIdentifier("40");
					errorList.add(e);
					sequenceNumber.setInvalid(true);
					sequenceNumber.setValidationError(e);
				}
			}
			
			newArrestee.setArresteeSequenceNumber(sequenceNumber);
			
			newArrestee.setArrestTransactionNumber(NibrsStringUtils.getStringBetween(40, 51, segmentData));
			
			ParsedObject<Date> arrestDate = newArrestee.getArrestDate();
			arrestDate.setMissing(false);
			arrestDate.setInvalid(false);
			String arrestDateString = NibrsStringUtils.getStringBetween(52, 59, segmentData);
			if (arrestDateString == null) {
				arrestDate.setMissing(true);
				arrestDate.setValue(null);
			} else {
				try {
					Date d = dateFormat.parse(arrestDateString);
					arrestDate.setValue(d);
				} catch (ParseException pe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(arrestDateString);
					e.setNIBRSErrorCode(NIBRSErrorCode._705);
					e.setDataElementIdentifier("42");
					errorList.add(e);
					arrestDate.setInvalid(true);
					arrestDate.setValidationError(e);
				}
			}
			newArrestee.setArrestDate(arrestDate);
			
			newArrestee.setTypeOfArrest(NibrsStringUtils.getStringBetween(60, 60, segmentData));
			newArrestee.setMultipleArresteeSegmentsIndicator(NibrsStringUtils.getStringBetween(61, 61, segmentData));
			newArrestee.setUcrArrestOffenseCode(NibrsStringUtils.getStringBetween(62, 64, segmentData));
			for (int i = 0; i < ArresteeSegment.ARRESTEE_ARMED_WITH_COUNT; i++) {
				newArrestee.setArresteeArmedWith(i, NibrsStringUtils.getStringBetween(65 + 3 * i, 66 + 3 * i, segmentData));
			}
			for (int i = 0; i < ArresteeSegment.AUTOMATIC_WEAPON_INDICATOR_COUNT; i++) {
				newArrestee.setAutomaticWeaponIndicator(i, NibrsStringUtils.getStringBetween(67 + 3 * i, 67 + 3 * i, segmentData));
			}
			newArrestee.setAgeString(NibrsStringUtils.getStringBetween(71, 74, segmentData));
			newArrestee.setSex(NibrsStringUtils.getStringBetween(75, 75, segmentData));
			newArrestee.setRace(NibrsStringUtils.getStringBetween(76, 76, segmentData));
			newArrestee.setEthnicity(NibrsStringUtils.getStringBetween(77, 77, segmentData));
			newArrestee.setResidentStatus(NibrsStringUtils.getStringBetween(78, 78, segmentData));
			newArrestee.setDispositionOfArresteeUnder18(NibrsStringUtils.getStringBetween(79, 79, segmentData));
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getReportSource());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._601);
			errorList.add(e);
		}
		return newArrestee;
	}

	private OffenderSegment buildOffenderSegment(Segment s, List<NIBRSError> errorList) {
		OffenderSegment newOffender = new OffenderSegment();
		String segmentData = s.getData();
		int length = s.getSegmentLength();
		if (length == 45 || length == 46) {
			
			ParsedObject<Integer> sequenceNumber = newOffender.getOffenderSequenceNumber();
			sequenceNumber.setMissing(false);
			sequenceNumber.setInvalid(false);
			String sequenceNumberString = NibrsStringUtils.getStringBetween(38, 39, segmentData);
			if (sequenceNumberString == null) {
				sequenceNumber.setMissing(true);
				sequenceNumber.setValue(null);
			} else {
				try {
					Integer sequenceNumberI = Integer.parseInt(sequenceNumberString);
					sequenceNumber.setValue(sequenceNumberI);
				} catch (NumberFormatException nfe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(sequenceNumberString);
					e.setNIBRSErrorCode(NIBRSErrorCode._301);
					e.setDataElementIdentifier("36");
					errorList.add(e);
					sequenceNumber.setInvalid(true);
					sequenceNumber.setValidationError(e);
				}
			}
			
			newOffender.setAgeString(NibrsStringUtils.getStringBetween(40, 43, segmentData));
			newOffender.setSex(NibrsStringUtils.getStringBetween(44, 44, segmentData));
			newOffender.setRace(NibrsStringUtils.getStringBetween(45, 45, segmentData));
			boolean hasOffenderEthnicity = length == 46;
			if (hasOffenderEthnicity) {
				newOffender.setEthnicity(NibrsStringUtils.getStringBetween(46, 46, segmentData));
			}
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getReportSource());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._301);
			errorList.add(e);
		}
		return newOffender;
	}

	private VictimSegment buildVictimSegment(Segment s, GroupAIncidentReport parentIncident, List<NIBRSError> errorList) {

		VictimSegment newVictim = new VictimSegment();
		String segmentData = s.getData();
		int length = s.getSegmentLength();

// comment out temporarily for Hawaii file validation. TODO  -hw		
//		if (length == 129 || length >= 141) {

			Integer sequenceNumberI = null;
			ParsedObject<Integer> sequenceNumber = newVictim.getVictimSequenceNumber();
			sequenceNumber.setMissing(false);
			sequenceNumber.setInvalid(false);
			String sequenceNumberString = NibrsStringUtils.getStringBetween(38, 40, segmentData);
			if (sequenceNumberString == null) {
				sequenceNumber.setMissing(true);
				sequenceNumber.setValue(null);
			} else {
				try {
					sequenceNumberI = Integer.parseInt(sequenceNumberString);
					sequenceNumber.setValue(sequenceNumberI);
				} catch (NumberFormatException nfe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(sequenceNumberString);
					e.setNIBRSErrorCode(NIBRSErrorCode._401);
					e.setDataElementIdentifier("23");
					errorList.add(e);
					sequenceNumber.setInvalid(true);
					sequenceNumber.setValidationError(e);
				}
			}

			for (int i = 0; i < VictimSegment.UCR_OFFENSE_CODE_CONNECTION_COUNT; i++) {
				newVictim.setUcrOffenseCodeConnection(i, NibrsStringUtils.getStringBetween(41 + 3 * i, 43 + 3 * i, segmentData));
			}
			for (int i = 0; i < VictimSegment.OFFENDER_NUMBER_RELATED_COUNT; i++) {
				String offenderNumberRelatedString = NibrsStringUtils.getStringBetween(90 + 4 * i, 91 + 4 * i, segmentData);
				ParsedObject<Integer> offenderNumberRelated = newVictim.getOffenderNumberRelated(i);
				offenderNumberRelated.setInvalid(false);
				offenderNumberRelated.setMissing(false);
				if (offenderNumberRelatedString == null) {
					offenderNumberRelated.setMissing(true);
					offenderNumberRelated.setInvalid(false);
				} else {
					try {
						Integer offenderNumberRelatedValue = Integer.parseInt(offenderNumberRelatedString);
						offenderNumberRelated.setValue(offenderNumberRelatedValue);
					} catch (NumberFormatException nfe) {
						NIBRSError e = new NIBRSError();
						e.setContext(s.getReportSource());
						e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
						e.setSegmentType(s.getSegmentType());
						e.setValue(NibrsStringUtils.getStringBetween(90 + 4 * i, 91 + 4 * i, segmentData));
						e.setNIBRSErrorCode(NIBRSErrorCode._402);
						e.setWithinSegmentIdentifier(sequenceNumberI);
						e.setDataElementIdentifier("34");
						errorList.add(e);
						offenderNumberRelated.setMissing(false);
						offenderNumberRelated.setInvalid(true);
					}
				}
			}
			for (int i = 0; i < VictimSegment.OFFENDER_NUMBER_RELATED_COUNT; i++) {
				newVictim.setVictimOffenderRelationship(i, NibrsStringUtils.getStringBetween(92 + 4 * i, 93 + 4 * i, segmentData));
			}

			newVictim.setTypeOfVictim(NibrsStringUtils.getStringBetween(71, 71, segmentData));
			newVictim.setAgeString(NibrsStringUtils.getStringBetween(72, 75, segmentData));
			newVictim.setSex(NibrsStringUtils.getStringBetween(76, 76, segmentData));
			newVictim.setRace(NibrsStringUtils.getStringBetween(77, 77, segmentData));
			newVictim.setEthnicity(NibrsStringUtils.getStringBetween(78, 78, segmentData));
			newVictim.setResidentStatus(NibrsStringUtils.getStringBetween(79, 79, segmentData));
			newVictim.setAggravatedAssaultHomicideCircumstances(0, NibrsStringUtils.getStringBetween(80, 81, segmentData));
			newVictim.setAggravatedAssaultHomicideCircumstances(1, NibrsStringUtils.getStringBetween(82, 83, segmentData));
			newVictim.setAdditionalJustifiableHomicideCircumstances(NibrsStringUtils.getStringBetween(84, 84, segmentData));

			for (int i = 0; i < VictimSegment.TYPE_OF_INJURY_COUNT; i++) {
				newVictim.setTypeOfInjury(i, NibrsStringUtils.getStringBetween(85 + i, 85 + i, segmentData));
			}

			boolean leoka = length == 141;

			if (leoka) {
				newVictim.setTypeOfOfficerActivityCircumstance(NibrsStringUtils.getStringBetween(130, 131, segmentData));
				newVictim.setOfficerAssignmentType(NibrsStringUtils.getStringBetween(132, 132, segmentData));
				newVictim.setOfficerOtherJurisdictionORI(NibrsStringUtils.getStringBetween(133, 141, segmentData));
			}
			
			parentIncident.setIncludesLeoka(leoka);
//TODO temporary change for Hawaii file validation. 
//		} else {
		if (!(length == 129 || length >= 141)){
				NIBRSError e = new NIBRSError();
			e.setContext(s.getReportSource());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._401);
			errorList.add(e);
		}

		return newVictim;

	}

	private PropertySegment buildPropertySegment(Segment s, List<NIBRSError> errorList) {

		PropertySegment newProperty = new PropertySegment();
		String segmentData = s.getData();
		int length = s.getSegmentLength();

		if (length == 307) {

			String typeOfPropertyLoss = NibrsStringUtils.getStringBetween(38, 38, segmentData);
			newProperty.setTypeOfPropertyLoss(typeOfPropertyLoss);

			for (int i = 0; i < PropertySegment.PROPERTY_DESCRIPTION_COUNT; i++) {
				newProperty.setPropertyDescription(i, NibrsStringUtils.getStringBetween(39 + 19 * i, 40 + 19 * i, segmentData));
			}
			for (int i = 0; i < PropertySegment.VALUE_OF_PROPERTY_COUNT; i++) {
				String propertyValueString = NibrsStringUtils.getStringBetween(41 + 19 * i, 49 + 19 * i, segmentData);
				ParsedObject<Integer> propertyValue = newProperty.getValueOfProperty(i);
				propertyValue.setInvalid(false);
				propertyValue.setMissing(false);
				if (propertyValueString == null) {
					propertyValue.setValue(null);
					propertyValue.setInvalid(false);
					propertyValue.setMissing(true);
				} else {
					try {
						String valueOfPropertyPattern = "\\d{1,9}";
						if (propertyValueString.matches(valueOfPropertyPattern)){
							Integer propertyValueI = Integer.parseInt(propertyValueString);
							propertyValue.setValue(propertyValueI);
						}
						else{
							throw new NumberFormatException(); 
						}
					} catch (NumberFormatException nfe) {
						NIBRSError e = new NIBRSError();
						e.setContext(s.getReportSource());
						e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
						e.setSegmentType(s.getSegmentType());
						e.setValue(org.apache.commons.lang3.StringUtils.leftPad(propertyValueString, 9));
						e.setNIBRSErrorCode(NIBRSErrorCode._302);
						e.setWithinSegmentIdentifier(null);
						e.setDataElementIdentifier("16");
						errorList.add(e);
						propertyValue.setMissing(false);
						propertyValue.setInvalid(true);
					}
				}
			}
			for (int i = 0; i < PropertySegment.DATE_RECOVERED_COUNT; i++) {
				
				ParsedObject<Date> d = newProperty.getDateRecovered(i);
				d.setMissing(false);
				d.setInvalid(false);
				String ds = NibrsStringUtils.getStringBetween(50 + 19 * i, 57 + 19 * i, segmentData);
				if (ds == null) {
					d.setMissing(true);
					d.setValue(null);
				} else {
					try {
						Date dd = dateFormat.parse(ds);
						d.setValue(dd);
					} catch (ParseException pe) {
						NIBRSError e = new NIBRSError();
						e.setContext(s.getReportSource());
						e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
						e.setSegmentType(s.getSegmentType());
						e.setValue(ds);
						e.setNIBRSErrorCode(NIBRSErrorCode._305);
						e.setDataElementIdentifier("17");
						errorList.add(e);
						d.setInvalid(true);
						d.setValidationError(e);
					}
				}
				
			}

			parseIntegerObject(segmentData, newProperty.getNumberOfStolenMotorVehicles(), 229, 230);
			parseIntegerObject(segmentData, newProperty.getNumberOfRecoveredMotorVehicles(), 231, 232);

			for (int i = 0; i < PropertySegment.SUSPECTED_DRUG_TYPE_COUNT; i++) {
				newProperty.setSuspectedDrugType(i, NibrsStringUtils.getStringBetween(233 + 15 * i, 233 + 15 * i, segmentData));
				String drugQuantityWholePartString = NibrsStringUtils.getStringBetween(234 + 15 * i, 242 + 15 * i, segmentData);
				String drugQuantityFractionalPartString = NibrsStringUtils.getStringBetween(243 + 15 * i, 245 + 15 * i, segmentData);
				if (drugQuantityWholePartString != null || drugQuantityFractionalPartString != null) {
					String fractionalValueString = "000";
					String value = org.apache.commons.lang3.StringUtils.isBlank(drugQuantityWholePartString)? "0":drugQuantityWholePartString.trim();
					if (drugQuantityFractionalPartString != null) {
						fractionalValueString = drugQuantityFractionalPartString;
						value += fractionalValueString;
					}
					
					String drugQuantityFullValueString = org.apache.commons.lang3.StringUtils.trimToEmpty(drugQuantityWholePartString) + "." + fractionalValueString;
					
					try{
						Double doubleValue = new Double(drugQuantityFullValueString);
						newProperty.setEstimatedDrugQuantity(i, new ParsedObject<Double>(doubleValue));
					}
					catch (NumberFormatException ne){
						log.error(ne);
						ParsedObject<Double> estimatedDrugQuantity = ParsedObject.getInvalidParsedObject();
						newProperty.setEstimatedDrugQuantity(i, estimatedDrugQuantity);
						NIBRSError e = new NIBRSError();
						e.setContext(s.getReportSource());
						e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
						e.setSegmentType(s.getSegmentType());
						e.setValue(value);
						e.setNIBRSErrorCode(NIBRSErrorCode._302);
						e.setWithinSegmentIdentifier(null);
						e.setDataElementIdentifier("21");
						errorList.add(e);
						estimatedDrugQuantity.setValidationError(e);

					}
				}
				else{
					newProperty.setEstimatedDrugQuantity(i, ParsedObject.getMissingParsedObject());
				}
				
				newProperty.setTypeDrugMeasurement(i, NibrsStringUtils.getStringBetween(246 + 15 * i, 247 + 15 * i, segmentData));
			}

		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getReportSource());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._401);
			errorList.add(e);
		}

		return newProperty;

	}

	private void parseIntegerObject(String segmentData,
			ParsedObject<Integer> parsedObject, 
			int startPosition, 
			int endPosition) {
		
		parsedObject.setMissing(false);
		parsedObject.setInvalid(false);
		
		String parsedString = 
				NibrsStringUtils.getStringBetween(startPosition, endPosition, segmentData);
		if (parsedString == null) {
			parsedObject.setMissing(true);
			parsedObject.setValue(null);
		} else {
			try {
				parsedObject.setValue(Integer.parseInt(parsedString));
			} catch (NumberFormatException nfe) {
				parsedObject.setInvalid(true);
			}
		}
	}

	private OffenseSegment buildOffenseSegment(Segment s, List<NIBRSError> errorList) {

		OffenseSegment newOffense = new OffenseSegment();

		String segmentData = s.getData();
		int length = s.getSegmentLength();

		if (length == 63 || length == 71) {

			newOffense.setUcrOffenseCode(NibrsStringUtils.getStringBetween(38, 40, segmentData));
			newOffense.setOffenseAttemptedCompleted(NibrsStringUtils.getStringBetween(41, 41, segmentData));
			newOffense.setLocationType(NibrsStringUtils.getStringBetween(45, 46, segmentData));
			
			String premisesEnteredString = NibrsStringUtils.getStringBetween(47, 48, segmentData);
			ParsedObject<Integer> premisesEntered = newOffense.getNumberOfPremisesEntered();
			
			if (premisesEnteredString == null) {
				premisesEntered.setMissing(true);
				premisesEntered.setInvalid(false);
				premisesEntered.setValue(null);
			} else {
				
				try {
					Integer value = Integer.parseInt(premisesEnteredString);
					premisesEntered.setValue(value);
					premisesEntered.setMissing(false);
					premisesEntered.setInvalid(false);
				} catch (NumberFormatException nfe) {
					NIBRSError e = new NIBRSError();
					e.setContext(s.getReportSource());
					e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
					e.setSegmentType(s.getSegmentType());
					e.setValue(premisesEnteredString);
					e.setNIBRSErrorCode(NIBRSErrorCode._204);
					e.setDataElementIdentifier("10");
					errorList.add(e);
					premisesEntered.setInvalid(true);
					premisesEntered.setValidationError(e);
				}
				
			}
			
			newOffense.setMethodOfEntry(NibrsStringUtils.getStringBetween(49, 49, segmentData));

			int biasMotivationFields = length == 63 ? 1 : OffenseSegment.BIAS_MOTIVATION_COUNT;

			for (int i = 0; i < biasMotivationFields; i++) {
				newOffense.setBiasMotivation(i, NibrsStringUtils.getStringBetween(62 + 2*i, 63 + 2*i, segmentData));
			}

			for (int i = 0; i < OffenseSegment.OFFENDERS_SUSPECTED_OF_USING_COUNT; i++) {
				newOffense.setOffendersSuspectedOfUsing(i, NibrsStringUtils.getStringBetween(42 + i, 42 + i, segmentData));
			}
			for (int i = 0; i < OffenseSegment.TYPE_OF_CRIMINAL_ACTIVITY_COUNT; i++) {
				newOffense.setTypeOfCriminalActivity(i, NibrsStringUtils.getStringBetween(50 + i, 50 + i, segmentData));
			}
			for (int i = 0; i < OffenseSegment.TYPE_OF_WEAPON_FORCE_INVOLVED_COUNT; i++) {
				newOffense.setTypeOfWeaponForceInvolved(i, NibrsStringUtils.getStringBetween(53 + 3 * i, 54 + 3 * i, segmentData));
			}
			for (int i = 0; i < OffenseSegment.AUTOMATIC_WEAPON_INDICATOR_COUNT; i++) {
				newOffense.setAutomaticWeaponIndicator(i, NibrsStringUtils.getStringBetween(55 + 3 * i, 55 + 3 * i, segmentData));
			}

		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getReportSource());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._201);
			errorList.add(e);
		}

		return newOffense;

	}

	class NullResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			return new InputSource(new StringReader(""));
		}
	}
}
