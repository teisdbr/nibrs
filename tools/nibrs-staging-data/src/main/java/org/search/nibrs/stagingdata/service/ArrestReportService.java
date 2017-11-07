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
package org.search.nibrs.stagingdata.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.ArresteeSegment;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.ArrestReportSegmentWasArmedWith;
import org.search.nibrs.stagingdata.model.ArresteeWasArmedWithType;
import org.search.nibrs.stagingdata.model.DispositionOfArresteeUnder18Type;
import org.search.nibrs.stagingdata.model.EthnicityOfPersonType;
import org.search.nibrs.stagingdata.model.RaceOfPersonType;
import org.search.nibrs.stagingdata.model.ResidentStatusOfPersonType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.SexOfPersonType;
import org.search.nibrs.stagingdata.model.TypeOfArrestType;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.search.nibrs.stagingdata.repository.AdditionalJustifiableHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.AggravatedAssaultHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.ArresteeWasArmedWithTypeRepository;
import org.search.nibrs.stagingdata.repository.BiasMotivationTypeRepository;
import org.search.nibrs.stagingdata.repository.ClearedExceptionallyTypeRepository;
import org.search.nibrs.stagingdata.repository.DispositionOfArresteeUnder18TypeRepository;
import org.search.nibrs.stagingdata.repository.EthnicityOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.LocationTypeRepository;
import org.search.nibrs.stagingdata.repository.MethodOfEntryTypeRepository;
import org.search.nibrs.stagingdata.repository.MultipleArresteeSegmentsIndicatorTypeRepository;
import org.search.nibrs.stagingdata.repository.OffenderSuspectedOfUsingTypeRepository;
import org.search.nibrs.stagingdata.repository.OfficerActivityCircumstanceTypeRepository;
import org.search.nibrs.stagingdata.repository.OfficerAssignmentTypeTypeRepository;
import org.search.nibrs.stagingdata.repository.PropertyDescriptionTypeRepository;
import org.search.nibrs.stagingdata.repository.RaceOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.ResidentStatusOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeRepository;
import org.search.nibrs.stagingdata.repository.SexOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.SuspectedDrugTypeTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeDrugMeasurementTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeInjuryTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfArrestTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfCriminalActivityTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfVictimTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfWeaponForceInvolvedTypeRepository;
import org.search.nibrs.stagingdata.repository.TypePropertyLossEtcTypeRepository;
import org.search.nibrs.stagingdata.repository.UcrOffenseCodeTypeRepository;
import org.search.nibrs.stagingdata.repository.VictimOffenderRelationshipTypeRepository;
import org.search.nibrs.stagingdata.repository.segment.ArrestReportSegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to process Group B Arrest Report.  
 *
 */
@Service
public class ArrestReportService {
	private static final Log log = LogFactory.getLog(ArrestReportService.class);

	@Autowired
	ArrestReportSegmentRepository arrestReportSegmentRepository;
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	public SegmentActionTypeRepository segmentActionTypeRepository; 
	@Autowired
	public ClearedExceptionallyTypeRepository clearedExceptionallyTypeRepository; 
	@Autowired
	public UcrOffenseCodeTypeRepository ucrOffenseCodeTypeRepository; 
	@Autowired
	public LocationTypeRepository locationTypeRepository; 
	@Autowired
	public MethodOfEntryTypeRepository methodOfEntryTypeRepository; 
	@Autowired
	public BiasMotivationTypeRepository biasMotivationTypeRepository; 
	@Autowired
	public TypeOfWeaponForceInvolvedTypeRepository typeOfWeaponForceInvolvedTypeRepository; 
	@Autowired
	public OffenderSuspectedOfUsingTypeRepository offenderSuspectedOfUsingTypeRepository; 
	@Autowired
	public TypeOfCriminalActivityTypeRepository typeOfCriminalActivityTypeRepository; 
	@Autowired
	public TypePropertyLossEtcTypeRepository typePropertyLossEtcTypeRepository; 
	@Autowired
	public TypeDrugMeasurementTypeRepository typeDrugMeasurementTypeRepository; 
	@Autowired
	public PropertyDescriptionTypeRepository propertyDescriptionTypeRepository; 
	@Autowired
	public SuspectedDrugTypeTypeRepository suspectedDrugTypeTypeRepository; 
	@Autowired
	public DispositionOfArresteeUnder18TypeRepository dispositionOfArresteeUnder18TypeRepository; 
	@Autowired
	public EthnicityOfPersonTypeRepository ethnicityOfPersonTypeRepository; 
	@Autowired
	public RaceOfPersonTypeRepository raceOfPersonTypeRepository; 
	@Autowired
	public SexOfPersonTypeRepository sexOfPersonTypeRepository; 
	@Autowired
	public TypeOfArrestTypeRepository typeOfArrestTypeRepository; 
	@Autowired
	public ResidentStatusOfPersonTypeRepository residentStatusOfPersonTypeRepository; 
	@Autowired
	public MultipleArresteeSegmentsIndicatorTypeRepository multipleArresteeSegmentsIndicatorTypeRepository; 
	@Autowired
	public ArresteeWasArmedWithTypeRepository arresteeWasArmedWithTypeRepository; 
	@Autowired
	public TypeOfVictimTypeRepository typeOfVictimTypeRepository; 
	@Autowired
	public OfficerActivityCircumstanceTypeRepository officerActivityCircumstanceTypeRepository; 
	@Autowired
	public OfficerAssignmentTypeTypeRepository officerAssignmentTypeTypeRepository; 
	@Autowired
	public AdditionalJustifiableHomicideCircumstancesTypeRepository additionalJustifiableHomicideCircumstancesTypeRepository; 
	@Autowired
	public TypeInjuryTypeRepository typeInjuryTypeRepository; 
	@Autowired
	public AggravatedAssaultHomicideCircumstancesTypeRepository aggravatedAssaultHomicideCircumstancesTypeRepository; 
	@Autowired
	public VictimOffenderRelationshipTypeRepository victimOffenderRelationshipTypeRepository; 
	@Autowired
	public CodeTableService codeTableService; 
	
	@Transactional
	public ArrestReportSegment saveArrestReportSegment(ArrestReportSegment arrestReportSegment){
		return arrestReportSegmentRepository.save(arrestReportSegment);
	}
	
	public ArrestReportSegment findArrestReportSegment(Integer id){
		return arrestReportSegmentRepository.findOne(id);
	}
	
	public List<ArrestReportSegment> findAllArrestReportSegment(){
		List<ArrestReportSegment> arrestReportSegments = new ArrayList<>();
		arrestReportSegmentRepository.findAll()
			.forEach(arrestReportSegments::add);
		return arrestReportSegments;
	}
	
	public ArrestReportSegment processGroupBArrestReport(GroupBArrestReport groupBArrestReport){
		
		ArresteeSegment arrestee = groupBArrestReport.getArrestee(); 
		if (arrestee == null){
			log.error("The Group B Report is not persisted because it misses the arrestee info. "); 
			return null;
		}
		
		ArrestReportSegment arrestReportSegment = new ArrestReportSegment(); 
		arrestReportSegment.setAgency(agencyRepository.findFirstByAgencyOri(groupBArrestReport.getOri()));
		
		String reportActionType = String.valueOf(groupBArrestReport.getReportActionType()).trim();
		SegmentActionTypeType segmentActionType = codeTableService.getCodeTableType(reportActionType, 
				segmentActionTypeRepository::findFirstBySegmentActionTypeCode, SegmentActionTypeType::new);
		arrestReportSegment.setSegmentActionType(segmentActionType);
		
		Optional<Integer> monthOfTape = Optional.ofNullable(groupBArrestReport.getMonthOfTape());
		monthOfTape.ifPresent( m-> {
			arrestReportSegment.setMonthOfTape(StringUtils.leftPad(String.valueOf(m), 2, '0'));
		});
		
		if (groupBArrestReport.getYearOfTape() != null){
			arrestReportSegment.setYearOfTape(String.valueOf(groupBArrestReport.getYearOfTape()));
		}
		
		arrestReportSegment.setCityIndicator(groupBArrestReport.getCityIndicator());
		arrestReportSegment.setOri(groupBArrestReport.getOri());
		Agency agency = codeTableService.getCodeTableType(groupBArrestReport.getOri(), 
				agencyRepository::findFirstByAgencyOri, Agency::new); 
		arrestReportSegment.setAgency(agency);

		arrestReportSegment.setArrestTransactionNumber(groupBArrestReport.getIdentifier());
		arrestReportSegment.setArresteeSequenceNumber(groupBArrestReport.getArresteeSequenceNumber());
		
		arrestReportSegment.setArrestDate(groupBArrestReport.getArrestDate());
		arrestReportSegment.setArrestDateType(codeTableService.getDateType(groupBArrestReport.getArrestDate()));
		
		TypeOfArrestType typeOfArrestType = codeTableService.getCodeTableType(
				arrestee.getTypeOfArrest(), typeOfArrestTypeRepository::findFirstByTypeOfArrestCode, TypeOfArrestType::new);
		arrestReportSegment.setTypeOfArrestType(typeOfArrestType );
		
		arrestReportSegment.setAgeOfArresteeMin(arrestee.getAge().getAgeMin());
		arrestReportSegment.setAgeOfArresteeMax(arrestee.getAge().getAgeMax());

		SexOfPersonType sexOfPersonType = codeTableService.getCodeTableType(
				arrestee.getSex(), sexOfPersonTypeRepository::findFirstBySexOfPersonCode, SexOfPersonType::new);
		arrestReportSegment.setSexOfPersonType(sexOfPersonType);
		
		RaceOfPersonType raceOfPersonType = codeTableService.getCodeTableType(
				arrestee.getRace(), raceOfPersonTypeRepository::findFirstByRaceOfPersonCode, RaceOfPersonType::new);
		arrestReportSegment.setRaceOfPersonType(raceOfPersonType);
		
		EthnicityOfPersonType ethnicityOfPersonType = codeTableService.getCodeTableType(
				arrestee.getEthnicity(), ethnicityOfPersonTypeRepository::findFirstByEthnicityOfPersonCode, EthnicityOfPersonType::new);
		arrestReportSegment.setEthnicityOfPersonType(ethnicityOfPersonType);
		
		ResidentStatusOfPersonType residentStatusOfPersonType = codeTableService.getCodeTableType(
				arrestee.getResidentStatus(), 
				residentStatusOfPersonTypeRepository::findFirstByResidentStatusOfPersonCode, 
				ResidentStatusOfPersonType::new);
		arrestReportSegment.setResidentStatusOfPersonType(residentStatusOfPersonType);
		
		DispositionOfArresteeUnder18Type dispositionOfArresteeUnder18Type = codeTableService.getCodeTableType(
				arrestee.getDispositionOfArresteeUnder18(), 
				dispositionOfArresteeUnder18TypeRepository::findFirstByDispositionOfArresteeUnder18Code, 
				DispositionOfArresteeUnder18Type::new);
		arrestReportSegment.setDispositionOfArresteeUnder18Type(dispositionOfArresteeUnder18Type );
		
		UcrOffenseCodeType ucrOffenseCodeType = codeTableService.getCodeTableType(
				arrestee.getUcrArrestOffenseCode(), 
				ucrOffenseCodeTypeRepository::findFirstByUcrOffenseCode, 
				UcrOffenseCodeType::new);;
		arrestReportSegment.setUcrOffenseCodeType(ucrOffenseCodeType);
		
		setArrestReportSegmentArmedWiths(arrestReportSegment, arrestee);
		
		return this.saveArrestReportSegment(arrestReportSegment);
	}

	private void setArrestReportSegmentArmedWiths(ArrestReportSegment arrestReportSegment, ArresteeSegment arrestee) {
		Set<ArrestReportSegmentWasArmedWith> armedWiths = new HashSet<>();  
		
		if (arrestee.containsArresteeArmedWith()){
			for (int i = 0; i < ArresteeSegment.ARRESTEE_ARMED_WITH_COUNT; i++){
				String arresteeArmedWithCode = 
						StringUtils.trimToNull(arrestee.getArresteeArmedWith(i));
				String automaticWeaponIndicator = 
						StringUtils.trimToNull(arrestee.getAutomaticWeaponIndicator(i));
				
				if (StringUtils.isNotBlank(arresteeArmedWithCode)){
					Optional<ArresteeWasArmedWithType> arresteeWasArmedWithType = 
							Optional.ofNullable(codeTableService.getCodeTableType(arresteeArmedWithCode,
									arresteeWasArmedWithTypeRepository::findFirstByArresteeWasArmedWithCode, 
									null));
					arresteeWasArmedWithType.ifPresent( type ->
						armedWiths.add(new ArrestReportSegmentWasArmedWith(
								null, arrestReportSegment, type, automaticWeaponIndicator))
					);
				}
			}
		}
		
		if (!armedWiths.isEmpty()){
			arrestReportSegment.setArrestReportSegmentWasArmedWiths(armedWiths);
		}
	}


}
