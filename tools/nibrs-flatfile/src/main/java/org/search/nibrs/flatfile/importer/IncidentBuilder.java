package org.search.nibrs.flatfile.importer;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.common.ReportSource;
import org.search.nibrs.flatfile.util.*;
import org.search.nibrs.model.*;
import org.search.nibrs.model.codes.NIBRSErrorCode;

/**
 * Builder class that constructs incidents from a stream of NIBRS report data.
 * Incidents are broadcast to listeners as events; this keeps the class as
 * memory-unintensive as possible (NIBRS report streams can be rather large).
 * <br/>
 * At some point, if other report elements than Incidents are desired, this will
 * need to be modified. Currently, it only broadcasts Incident "add" records.
 * 
 */
public class IncidentBuilder {

	private static final class LogListener implements ReportListener {
		public int reportCount = 0;
		public void newReport(AbstractReport newReport) {
			LOG.info("Created " + newReport.getUniqueReportDescription());
			reportCount++;
		}
	}

	private static final Logger LOG = LogManager.getLogger(IncidentBuilder.class);

	private List<ReportListener> listeners;
	private LogListener logListener = new LogListener();

	public IncidentBuilder() {
		listeners = new ArrayList<ReportListener>();
		listeners.add(logListener);
	}

	public void addIncidentListener(ReportListener listener) {
		listeners.add(listener);
	}

	public void removeIncidentListener(ReportListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Read NIBRS incidents from the flatfile format exposed by the specified Reader
	 * @param reader the source of the data
	 * @return a list of errors encountered, if any
	 * @throws IOException exception encountered in addressing the Reader
	 */
	public List<NIBRSError> buildIncidents(Reader reader, String readerLocationName) throws IOException {

		List<NIBRSError> errorList = new ArrayList<NIBRSError>();

		BufferedReader br = null;
		
		// we buffer to improve performance in reading big files
		if (!(reader instanceof BufferedReader)) {
			br = new BufferedReader(reader);
		} else {
			br = (BufferedReader) reader;
		}
		
		String line = null;
		AbstractReport currentReport = null;
		int lineNumber = 1;
		
		LOG.info("Processing NIBRS flat file");
		
		while ((line = br.readLine()) != null) {
			Segment s = new Segment();
			List<NIBRSError> segmentErrors = s.setData(lineNumber, line);
			errorList.addAll(segmentErrors);
			if (segmentErrors.isEmpty()) {
				char level = s.getSegmentLevel();
				if (level == ZeroReport.ZERO_REPORT_TYPE_IDENTIFIER || level == GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER || level == ArresteeSegment.GROUP_B_ARRESTEE_SEGMENT_TYPE_IDENTIFIER) {
					handleNewReport(currentReport);
					currentReport = buildReport(errorList, s, readerLocationName);
				} else {
					int errorListSize = errorList.size();
					addSegmentToIncident((GroupAIncidentReport) currentReport, s, errorList);
					if (errorList.size() > errorListSize) {
						currentReport.setHasUpstreamErrors(true);
					}
				}
			}
			lineNumber++;
		}
		
		handleNewReport(currentReport);

		LOG.info("finished processing file, read " + (lineNumber - 1) + " lines.");
		LOG.info("Encountered " + errorList.size() + " error(s).");
		LOG.info("Created " + logListener.reportCount + " incident(s).");

		return errorList;

	}

	public AbstractReport buildReport(List<NIBRSError> errorList, Segment s, String readerLocationName) {
		int errorListSize = errorList.size();
		AbstractReport ret = null;
		char level = s.getSegmentLevel();
		if (level == GroupAIncidentReport.ADMIN_SEGMENT_TYPE_IDENTIFIER) {
			ret = buildGroupAIncidentSegment(s, errorList);
		} else if (level == ArresteeSegment.GROUP_B_ARRESTEE_SEGMENT_TYPE_IDENTIFIER) {
			ret = buildGroupBIncidentReport(s, errorList);
		} else if (level == ZeroReport.ZERO_REPORT_TYPE_IDENTIFIER) {
			ret = buildZeroReport(s, errorList);
		}
		if (errorList.size() > errorListSize) {
			ret.setHasUpstreamErrors(true);
		}
		ReportSource source = new ReportSource();
		source.setSourceLocation(String.valueOf(s.getLineNumber()));
		source.setSourceName(readerLocationName);
		ret.setSource(source);
		return ret;
	}

	private ZeroReport buildZeroReport(Segment s, List<NIBRSError> errorList) {
		ZeroReport ret = new ZeroReport();
		ret.setOri(s.getOri());
		ret.setReportActionType(s.getActionType());
		int length = s.getSegmentLength();
		if (length == 43) {
			ret.setMonthOfTape(getIntValueFromSegment(s, 7, 8, errorList, NIBRSErrorCode._001));
			ret.setYearOfTape(getIntValueFromSegment(s, 9, 12, errorList, NIBRSErrorCode._001));
			ret.setCityIndicator(StringUtils.getStringBetween(13, 16, s.getData()));
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getLineNumber());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._001);
			errorList.add(e);
		}
		return ret;
	}

	private AbstractReport buildGroupBIncidentReport(Segment s, List<NIBRSError> errorList) {
		GroupBIncidentReport ret = new GroupBIncidentReport();
		ArresteeSegment arrestee = new ArresteeSegment();
		String segmentData = s.getData();
		ret.setOri(s.getOri());
		ret.setReportActionType(s.getActionType());
		int length = s.getSegmentLength();
		if (length == 66) {
			ret.setMonthOfTape(getIntValueFromSegment(s, 7, 8, errorList, NIBRSErrorCode._701));
			ret.setYearOfTape(getIntValueFromSegment(s, 9, 12, errorList, NIBRSErrorCode._701));
			ret.setCityIndicator(StringUtils.getStringBetween(13, 16, segmentData));
			arrestee.setArresteeSequenceNumber(StringUtils.getIntegerBetween(38, 39, segmentData));
			arrestee.setArrestTransactionNumber(StringUtils.getStringBetween(26, 37, segmentData));
			arrestee.setArrestDate(StringUtils.getDateBetween(40, 47, segmentData));
			arrestee.setTypeOfArrest(StringUtils.getStringBetween(48, 48, segmentData));
			arrestee.setUcrArrestOffenseCode(StringUtils.getStringBetween(49, 51, segmentData));
			for (int i = 0; i < 2; i++) {
				arrestee.setArresteeArmedWith(i, StringUtils.getStringBetween(52 + 3 * i, 53 + 3 * i, segmentData));
				arrestee.setAutomaticWeaponIndicator(i, StringUtils.getStringBetween(54 + 3 * i, 54 + 3 * i, segmentData));
			}
			arrestee.setAgeString(StringUtils.getStringBetween(58, 61, segmentData));
			arrestee.setSex(StringUtils.getStringBetween(62, 62, segmentData));
			arrestee.setRace(StringUtils.getStringBetween(63, 63, segmentData));
			arrestee.setEthnicity(StringUtils.getStringBetween(64, 64, segmentData));
			arrestee.setResidentStatusOfArrestee(StringUtils.getStringBetween(65, 65, segmentData));
			arrestee.setDispositionOfArresteeUnder18(StringUtils.getStringBetween(66, 66, segmentData));
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getLineNumber());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._701);
			errorList.add(e);
		}
		
		ret.addArrestee(arrestee);
	
		return ret;
	}

	private final void handleNewReport(AbstractReport newReport) {
		if (newReport != null) {
			for (Iterator<ReportListener> it = listeners.iterator(); it.hasNext();) {
				ReportListener listener = it.next();
				listener.newReport(newReport);
			}
		}
	}

	private final AbstractReport buildGroupAIncidentSegment(Segment s, List<NIBRSError> errorList) {
		GroupAIncidentReport newIncident = new GroupAIncidentReport();
		newIncident.setIncidentNumber(s.getSegmentUniqueIdentifier());
		newIncident.setOri(s.getOri());
		newIncident.setReportActionType(s.getActionType());
		String segmentData = s.getData();
		int length = s.getSegmentLength();
		if (length == 87 || length == 88) {
			newIncident.setMonthOfTape(getIntValueFromSegment(s, 7, 8, errorList, NIBRSErrorCode._101));
			newIncident.setYearOfTape(getIntValueFromSegment(s, 9, 12, errorList, NIBRSErrorCode._101));
			newIncident.setCityIndicator(StringUtils.getStringBetween(13, 16, segmentData));
			int incidentYear = getIntValueFromSegment(s, 38, 41, errorList, NIBRSErrorCode._105);
			int incidentMonthOrig = getIntValueFromSegment(s, 42, 43, errorList, NIBRSErrorCode._105);
			int incidentMonth = DateUtils.convertMonthValue(incidentMonthOrig);
			int incidentDay = getIntValueFromSegment(s, 44, 45, errorList, NIBRSErrorCode._105);
			newIncident.setIncidentDate(DateUtils.makeDate(incidentYear, incidentMonth, incidentDay));
			newIncident.setReportDateIndicator(StringUtils.getStringBetween(46, 46, segmentData));
			String hourString = StringUtils.getStringBetween(47, 48, segmentData);
			if (hourString != null) {
				newIncident.setIncidentHour(new Integer(hourString));
			}
			newIncident.setExceptionalClearanceCode(StringUtils.getStringBetween(49, 49, segmentData));
			String clearanceYearString = StringUtils.getStringBetween(50, 53, segmentData);
			if (clearanceYearString != null) {
				int clearanceYear = getIntValueFromSegment(s, 50, 53, errorList, NIBRSErrorCode._105);
				int clearanceMonthOrig = getIntValueFromSegment(s, 54, 55, errorList, NIBRSErrorCode._105);
				int clearanceMonth = DateUtils.convertMonthValue(clearanceMonthOrig);
				int clearanceDay = getIntValueFromSegment(s, 56, 57, errorList, NIBRSErrorCode._105);
				newIncident.setExceptionalClearanceDate(DateUtils.makeDate(clearanceYear, clearanceMonth, clearanceDay));
			}
			boolean cargoTheft = length == 88;
			if (cargoTheft) {
				String cargoTheftYN = StringUtils.getStringBetween(88, 88, segmentData);
				newIncident.setCargoTheftIndicator(cargoTheftYN);
				newIncident.setIncludesCargoTheft(true);
			}
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getLineNumber());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._101);
			errorList.add(e);
		}
		return newIncident;
	}

	private Integer getIntValueFromSegment(Segment s, int startPos, int endPos, List<NIBRSError> errorList, NIBRSErrorCode errorCode) {
		String sv = StringUtils.getStringBetween(startPos, endPos, s.getData());
		Integer i = null;
		try {
			i = new Integer(sv);
		} catch (NumberFormatException nfe) {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getLineNumber());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setNIBRSErrorCode(errorCode);
			e.setValue(sv);
			e.setSegmentType(s.getSegmentType());
			errorList.add(e);
			LOG.debug("Error in int conversion: lineNumber=" + s.getLineNumber() + ", value=" + sv);
		}
		return i;
	}

	private final void addSegmentToIncident(GroupAIncidentReport currentIncident, Segment s, List<NIBRSError> errorList) {
		char segmentType = s.getSegmentType();
		switch (segmentType) {
		case OffenseSegment.OFFENSE_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addOffense(buildOffenseSegment(s, errorList));
			break;
		case PropertySegment.PROPERTY_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addProperty(buildPropertySegment(s, errorList));
			break;
		case VictimSegment.VICTIM_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addVictim(buildVictimSegment(s, currentIncident, errorList));
			break;
		case OffenderSegment.OFFENDER_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addOffender(buildOffenderSegment(s, errorList));
			break;
		case ArresteeSegment.GROUP_A_ARRESTEE_SEGMENT_TYPE_IDENTIFIER:
			currentIncident.addArrestee(buildArresteeSegment(s, errorList));
			break;
		default:
			NIBRSError error = new NIBRSError();
			error.setContext(s.getLineNumber());
			error.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			error.setNIBRSErrorCode(NIBRSErrorCode._051);
			error.setValue(segmentType);
			errorList.add(error);
		}
	}

	private ArresteeSegment buildArresteeSegment(Segment s, List<NIBRSError> errorList) {
		ArresteeSegment newArrestee = new ArresteeSegment();
		String segmentData = s.getData();
		int length = s.getSegmentLength();
		if (length == 110) {
			newArrestee.setArresteeSequenceNumber(StringUtils.getIntegerBetween(38, 39, segmentData));
			newArrestee.setArrestTransactionNumber(StringUtils.getStringBetween(40, 51, segmentData));
			newArrestee.setArrestDate(StringUtils.getDateBetween(52, 59, segmentData));
			newArrestee.setTypeOfArrest(StringUtils.getStringBetween(60, 60, segmentData));
			newArrestee.setMultipleArresteeSegmentsIndicator(StringUtils.getStringBetween(61, 61, segmentData));
			newArrestee.setUcrArrestOffenseCode(StringUtils.getStringBetween(62, 64, segmentData));
			for (int i = 0; i < ArresteeSegment.ARRESTEE_ARMED_WITH_COUNT; i++) {
				newArrestee.setArresteeArmedWith(i, StringUtils.getStringBetween(65 + 3 * i, 66 + 3 * i, segmentData));
			}
			for (int i = 0; i < ArresteeSegment.AUTOMATIC_WEAPON_INDICATOR_COUNT; i++) {
				newArrestee.setAutomaticWeaponIndicator(i, StringUtils.getStringBetween(67 + 3 * i, 67 + 3 * i, segmentData));
			}
			newArrestee.setAgeString(StringUtils.getStringBetween(71, 74, segmentData));
			newArrestee.setSex(StringUtils.getStringBetween(75, 75, segmentData));
			newArrestee.setRace(StringUtils.getStringBetween(76, 76, segmentData));
			newArrestee.setEthnicity(StringUtils.getStringBetween(77, 77, segmentData));
			newArrestee.setResidentStatusOfArrestee(StringUtils.getStringBetween(78, 78, segmentData));
			newArrestee.setDispositionOfArresteeUnder18(StringUtils.getStringBetween(79, 79, segmentData));
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getLineNumber());
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
			newOffender.setOffenderSequenceNumber(StringUtils.getIntegerBetween(38, 39, segmentData));
			newOffender.setAgeString(StringUtils.getStringBetween(40, 43, segmentData));
			newOffender.setSex(StringUtils.getStringBetween(44, 44, segmentData));
			newOffender.setRace(StringUtils.getStringBetween(45, 45, segmentData));
			boolean hasOffenderEthnicity = length == 46;
			if (hasOffenderEthnicity) {
				newOffender.setEthnicity(StringUtils.getStringBetween(46, 46, segmentData));
			}
		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getLineNumber());
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

		if (length == 129 || length == 141) {

			newVictim.setVictimSequenceNumber(StringUtils.getIntegerBetween(38, 40, segmentData));

			for (int i = 0; i < 10; i++) {
				newVictim.setUcrOffenseCodeConnection(i, StringUtils.getStringBetween(41 + 3 * i, 43 + 3 * i, segmentData));
				newVictim.setOffenderNumberRelated(i, StringUtils.getIntegerBetween(90 + 4 * i, 91 + 4 * i, segmentData));
				newVictim.setVictimOffenderRelationship(i, StringUtils.getStringBetween(92 + 4 * i, 93 + 4 * i, segmentData));
			}

			newVictim.setTypeOfVictim(StringUtils.getStringBetween(71, 71, segmentData));
			newVictim.setAgeString(StringUtils.getStringBetween(72, 75, segmentData));
			newVictim.setSex(StringUtils.getStringBetween(76, 76, segmentData));
			newVictim.setRace(StringUtils.getStringBetween(77, 77, segmentData));
			newVictim.setEthnicity(StringUtils.getStringBetween(78, 78, segmentData));
			newVictim.setResidentStatusOfVictim(StringUtils.getStringBetween(79, 79, segmentData));
			newVictim.setAggravatedAssaultHomicideCircumstances(0, StringUtils.getStringBetween(80, 81, segmentData));
			newVictim.setAggravatedAssaultHomicideCircumstances(1, StringUtils.getStringBetween(82, 83, segmentData));
			newVictim.setAdditionalJustifiableHomicideCircumstances(StringUtils.getStringBetween(84, 84, segmentData));

			for (int i = 0; i < 5; i++) {
				newVictim.setTypeOfInjury(i, StringUtils.getStringBetween(85 + i, 85 + i, segmentData));
			}

			boolean leoka = length == 141;

			if (leoka) {
				newVictim.setTypeOfOfficerActivityCircumstance(StringUtils.getStringBetween(130, 131, segmentData));
				newVictim.setOfficerAssignmentType(StringUtils.getStringBetween(132, 132, segmentData));
				newVictim.setOfficerOtherJurisdictionORI(StringUtils.getStringBetween(133, 141, segmentData));
			}
			
			parentIncident.setIncludesLeoka(leoka);

		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getLineNumber());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._501);
			errorList.add(e);
		}

		return newVictim;

	}

	private PropertySegment buildPropertySegment(Segment s, List<NIBRSError> errorList) {

		PropertySegment newProperty = new PropertySegment();
		String segmentData = s.getData();
		int length = s.getSegmentLength();

		if (length == 307) {

			newProperty.setTypeOfPropertyLoss(StringUtils.getStringBetween(38, 38, segmentData));

			for (int i = 0; i < 10; i++) {
				newProperty.setPropertyDescription(i, StringUtils.getStringBetween(39 + 19 * i, 40 + 19 * i, segmentData));
				newProperty.setValueOfProperty(i, StringUtils.getIntegerBetween(41 + 19 * i, 49 + 19 * i, segmentData));
				newProperty.setDateRecovered(i, StringUtils.getDateBetween(50 + 19 * i, 57 + 19 * i, segmentData));
			}

			newProperty.setNumberOfStolenMotorVehicles(StringUtils.getIntegerBetween(229, 230, segmentData));
			newProperty.setNumberOfRecoveredMotorVehicles(StringUtils.getIntegerBetween(231, 232, segmentData));

			for (int i = 0; i < 3; i++) {
				newProperty.setSuspectedDrugType(i, StringUtils.getStringBetween(233 + 15 * i, 233 + 15 * i, segmentData));
				String drugQuantityWholePartString = StringUtils.getStringBetween(234 + 15 * i, 242 + 15 * i, segmentData);
				String drugQuantityFractionalPartString = StringUtils.getStringBetween(243 + 15 * i, 245 + 15 * i, segmentData);
				if (drugQuantityWholePartString != null) {
					String fractionalValueString = "000";
					if (drugQuantityFractionalPartString != null) {
						fractionalValueString = drugQuantityFractionalPartString;
					}
					String drugQuantityFullValueString = drugQuantityWholePartString + "." + fractionalValueString;
					newProperty.setEstimatedDrugQuantity(i, new Double(drugQuantityFullValueString));
				}
				newProperty.setTypeDrugMeasurement(i, StringUtils.getStringBetween(246 + 15 * i, 247 + 15 * i, segmentData));
			}

		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getLineNumber());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._401);
			errorList.add(e);
		}

		return newProperty;

	}

	private OffenseSegment buildOffenseSegment(Segment s, List<NIBRSError> errorList) {

		OffenseSegment newOffense = new OffenseSegment();

		String segmentData = s.getData();
		int length = s.getSegmentLength();

		if (length == 63 || length == 71) {

			newOffense.setUcrOffenseCode(StringUtils.getStringBetween(38, 40, segmentData));
			newOffense.setOffenseAttemptedCompleted(StringUtils.getStringBetween(41, 41, segmentData));
			newOffense.setLocationType(StringUtils.getStringBetween(45, 46, segmentData));
			newOffense.setNumberOfPremisesEntered(StringUtils.getIntegerBetween(47, 48, segmentData));
			newOffense.setMethodOfEntry(StringUtils.getStringBetween(49, 49, segmentData));

			int biasMotivationFields = length == 63 ? 1 : OffenseSegment.BIAS_MOTIVATION_COUNT;

			for (int i = 0; i < biasMotivationFields; i++) {
				newOffense.setBiasMotivation(i, StringUtils.getStringBetween(62 + i, 63 + i, segmentData));
			}

			for (int i = 0; i < OffenseSegment.OFFENDERS_SUSPECTED_OF_USING_COUNT; i++) {
				newOffense.setOffendersSuspectedOfUsing(i, StringUtils.getStringBetween(42 + i, 42 + i, segmentData));
			}
			for (int i = 0; i < OffenseSegment.TYPE_OF_CRIMINAL_ACTIVITY_COUNT; i++) {
				newOffense.setTypeOfCriminalActivity(i, StringUtils.getStringBetween(50 + i, 50 + i, segmentData));
			}
			for (int i = 0; i < OffenseSegment.TYPE_OF_WEAPON_FORCE_INVOLVED_COUNT; i++) {
				newOffense.setTypeOfWeaponForceInvolved(i, StringUtils.getStringBetween(53 + 3 * i, 54 + 3 * i, segmentData));
			}
			for (int i = 0; i < OffenseSegment.AUTOMATIC_WEAPON_INDICATOR_COUNT; i++) {
				newOffense.setAutomaticWeaponIndicator(i, StringUtils.getStringBetween(55 + 3 * i, 55 + 3 * i, segmentData));
			}

		} else {
			NIBRSError e = new NIBRSError();
			e.setContext(s.getLineNumber());
			e.setReportUniqueIdentifier(s.getSegmentUniqueIdentifier());
			e.setSegmentType(s.getSegmentType());
			e.setValue(length);
			e.setNIBRSErrorCode(NIBRSErrorCode._201);
			errorList.add(e);
		}

		return newOffense;

	}

}
