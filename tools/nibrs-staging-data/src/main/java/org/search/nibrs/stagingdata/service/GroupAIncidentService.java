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
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.ArresteeSegmentWasArmedWith;
import org.search.nibrs.stagingdata.model.ArresteeWasArmedWithType;
import org.search.nibrs.stagingdata.model.BiasMotivationType;
import org.search.nibrs.stagingdata.model.ClearedExceptionallyType;
import org.search.nibrs.stagingdata.model.DispositionOfArresteeUnder18Type;
import org.search.nibrs.stagingdata.model.EthnicityOfPersonType;
import org.search.nibrs.stagingdata.model.LocationType;
import org.search.nibrs.stagingdata.model.MethodOfEntryType;
import org.search.nibrs.stagingdata.model.MultipleArresteeSegmentsIndicatorType;
import org.search.nibrs.stagingdata.model.OffenderSuspectedOfUsingType;
import org.search.nibrs.stagingdata.model.RaceOfPersonType;
import org.search.nibrs.stagingdata.model.ResidentStatusOfPersonType;
import org.search.nibrs.stagingdata.model.SexOfPersonType;
import org.search.nibrs.stagingdata.model.TypeOfArrestType;
import org.search.nibrs.stagingdata.model.TypeOfCriminalActivityType;
import org.search.nibrs.stagingdata.model.TypeOfWeaponForceInvolved;
import org.search.nibrs.stagingdata.model.TypeOfWeaponForceInvolvedType;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenderSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
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
import org.search.nibrs.stagingdata.repository.segment.AdministrativeSegmentRepository;
import org.search.nibrs.stagingdata.repository.segment.OffenseSegmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to process Group B Arrest Report.  
 *
 */
@Service
public class GroupAIncidentService {
	@Autowired
	AdministrativeSegmentRepository administrativeSegmentRepository;
	@Autowired
	OffenseSegmentRepository offenseSegmentRepository;
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
	public AdministrativeSegment saveAdministrativeSegment(AdministrativeSegment administrativeSegment){
		return administrativeSegmentRepository.save(administrativeSegment);
	}
	
	public AdministrativeSegment findAdministrativeSegment(Integer id){
		return administrativeSegmentRepository.findOne(id);
	}
	
	public List<AdministrativeSegment> findAllAdministrativeSegments(){
		List<AdministrativeSegment> administrativeSegments = new ArrayList<>();
		administrativeSegmentRepository.findAll().forEach(administrativeSegments::add);
		return administrativeSegments;
	}
	
	public OffenseSegment saveOffenseSegment(OffenseSegment offenseSegment){
		return offenseSegmentRepository.save(offenseSegment);
	}
	
	public Iterable<OffenseSegment> saveOffenseSegment(List<OffenseSegment> offenseSegments){
		return offenseSegmentRepository.save(offenseSegments);
	}
	
	public AdministrativeSegment saveGroupAIncidentReport(GroupAIncidentReport groupAIncidentReport){
		AdministrativeSegment administrativeSegment = new AdministrativeSegment(); 
		administrativeSegment.setAgency(agencyRepository.findFirstByAgencyOri(groupAIncidentReport.getOri()));
		
		String reportActionType = String.valueOf(groupAIncidentReport.getReportActionType()).trim();
		administrativeSegment.setSegmentActionType(segmentActionTypeRepository.findFirstBySegmentActionTypeCode(reportActionType));
		
		Optional<Integer> monthOfTape = Optional.ofNullable(groupAIncidentReport.getMonthOfTape());
		monthOfTape.ifPresent( m-> {
			administrativeSegment.setMonthOfTape(StringUtils.leftPad(String.valueOf(m), 2, '0'));
		});
		
		if (groupAIncidentReport.getYearOfTape() != null){
			administrativeSegment.setYearOfTape(String.valueOf(groupAIncidentReport.getYearOfTape()));
		}
		
		administrativeSegment.setCityIndicator(groupAIncidentReport.getCityIndicator());
		administrativeSegment.setOri(groupAIncidentReport.getOri());
		administrativeSegment.setIncidentNumber(groupAIncidentReport.getIncidentNumber());
		administrativeSegment.setIncidentDate(groupAIncidentReport.getIncidentDate().getValue());
		administrativeSegment.setIncidentDateType(codeTableService.getDateType(groupAIncidentReport.getIncidentDate().getValue()));
		administrativeSegment.setReportDateIndicator(groupAIncidentReport.getReportDateIndicator());
		administrativeSegment.setReportDateIndicator(groupAIncidentReport.getReportDateIndicator());
		
		Optional<Integer> incidentHour = Optional.ofNullable(groupAIncidentReport.getIncidentHour().getValue());
		administrativeSegment.setIncidentHour(incidentHour.map(String::valueOf).orElse(""));
		
		ClearedExceptionallyType clearedExceptionallyType = 
				codeTableService.getCodeTableType(groupAIncidentReport.getExceptionalClearanceCode(), 
						clearedExceptionallyTypeRepository::findFirstByClearedExceptionallyCode, 
						ClearedExceptionallyType::new); 
		administrativeSegment.setClearedExceptionallyType(clearedExceptionallyType);
		
		Agency agency = codeTableService.getCodeTableType(groupAIncidentReport.getOri(), agencyRepository::findFirstByAgencyOri, Agency::new); 
		administrativeSegment.setAgency(agency);
		
		processOffenses(administrativeSegment, groupAIncidentReport);
		processOffenders(administrativeSegment, groupAIncidentReport);
		processArrestees(administrativeSegment, groupAIncidentReport);
		return this.saveAdministrativeSegment(administrativeSegment);
	}

	private void processArrestReportSegmentArmedWiths(ArresteeSegment arresteeSegment, org.search.nibrs.model.ArresteeSegment arrestee) {
		Set<ArresteeSegmentWasArmedWith> armedWiths = new HashSet<>();  
		
		if (arrestee.containsArresteeArmedWith()){
			for (int i = 0; i < org.search.nibrs.model.ArresteeSegment.ARRESTEE_ARMED_WITH_COUNT; i++){
				String arresteeArmedWithCode = 
						StringUtils.trimToNull(arrestee.getArresteeArmedWith(i));
				String automaticWeaponIndicator = 
						StringUtils.trimToEmpty(arrestee.getAutomaticWeaponIndicator(i));
				
				if (StringUtils.isNotBlank(arresteeArmedWithCode)){
					Optional<ArresteeWasArmedWithType> arresteeWasArmedWithType = 
							Optional.ofNullable(codeTableService.getCodeTableType(arresteeArmedWithCode,
									arresteeWasArmedWithTypeRepository::findFirstByArresteeWasArmedWithCode, 
									null));
					arresteeWasArmedWithType.ifPresent( type ->
						armedWiths.add(new ArresteeSegmentWasArmedWith(
								null, arresteeSegment, type, automaticWeaponIndicator))
					);
				}
			}
		}
		
		if (!armedWiths.isEmpty()){
			arresteeSegment.setArresteeSegmentWasArmedWiths(armedWiths);
		}
	}
	
	private void processArrestees(AdministrativeSegment administrativeSegment,
			GroupAIncidentReport groupAIncidentReport) {
		if (groupAIncidentReport.getArresteeCount() > 0){
			Set<ArresteeSegment> arresteeSegments = new HashSet<>();
			for (org.search.nibrs.model.ArresteeSegment arrestee: groupAIncidentReport.getArrestees()){
				ArresteeSegment arresteeSegment = new ArresteeSegment();
				arresteeSegment.setSegmentActionType(administrativeSegment.getSegmentActionType());
				arresteeSegment.setAdministrativeSegment(administrativeSegment);
				arresteeSegment.setArresteeSequenceNumber(arrestee.getArresteeSequenceNumber().getValue());
				arresteeSegment.setArrestTransactionNumber(arrestee.getArrestTransactionNumber());
				arresteeSegment.setArrestDate(arrestee.getArrestDate().getValue());
				arresteeSegment.setArrestDateType(codeTableService.getDateType(arrestee.getArrestDate().getValue()));
				
				TypeOfArrestType typeOfArrestType = codeTableService.getCodeTableType(
						arrestee.getTypeOfArrest(), typeOfArrestTypeRepository::findFirstByTypeOfArrestCode, TypeOfArrestType::new);
				arresteeSegment.setTypeOfArrestType(typeOfArrestType );
				
				MultipleArresteeSegmentsIndicatorType multipleArresteeSegmentsIndicatorType = 
						codeTableService.getCodeTableType(
							arrestee.getMultipleArresteeSegmentsIndicator(), 
							multipleArresteeSegmentsIndicatorTypeRepository::findFirstByMultipleArresteeSegmentsIndicatorCode, 
							MultipleArresteeSegmentsIndicatorType::new);
				arresteeSegment.setMultipleArresteeSegmentsIndicatorType(multipleArresteeSegmentsIndicatorType);
				
				arresteeSegment.setAgeOfArresteeMin(arrestee.getAge().getAgeMin());
				arresteeSegment.setAgeOfArresteeMax(arrestee.getAge().getAgeMax());

				SexOfPersonType sexOfPersonType = codeTableService.getCodeTableType(
						arrestee.getSex(), sexOfPersonTypeRepository::findFirstBySexOfPersonCode, SexOfPersonType::new);
				arresteeSegment.setSexOfPersonType(sexOfPersonType);
				
				RaceOfPersonType raceOfPersonType = codeTableService.getCodeTableType(
						arrestee.getRace(), raceOfPersonTypeRepository::findFirstByRaceOfPersonCode, RaceOfPersonType::new);
				arresteeSegment.setRaceOfPersonType(raceOfPersonType);
				
				EthnicityOfPersonType ethnicityOfPersonType = codeTableService.getCodeTableType(
						arrestee.getEthnicity(), ethnicityOfPersonTypeRepository::findFirstByEthnicityOfPersonCode, EthnicityOfPersonType::new);
				arresteeSegment.setEthnicityOfPersonType(ethnicityOfPersonType);
				
				ResidentStatusOfPersonType residentStatusOfPersonType = codeTableService.getCodeTableType(
						arrestee.getResidentStatus(), 
						residentStatusOfPersonTypeRepository::findFirstByResidentStatusOfPersonCode, 
						ResidentStatusOfPersonType::new);
				arresteeSegment.setResidentStatusOfPersonType(residentStatusOfPersonType);
				
				DispositionOfArresteeUnder18Type dispositionOfArresteeUnder18Type = codeTableService.getCodeTableType(
						arrestee.getDispositionOfArresteeUnder18(), 
						dispositionOfArresteeUnder18TypeRepository::findFirstByDispositionOfArresteeUnder18Code, 
						DispositionOfArresteeUnder18Type::new);
				arresteeSegment.setDispositionOfArresteeUnder18Type(dispositionOfArresteeUnder18Type );
				
				UcrOffenseCodeType ucrOffenseCodeType = codeTableService.getCodeTableType(
						arrestee.getUcrArrestOffenseCode(), 
						ucrOffenseCodeTypeRepository::findFirstByUcrOffenseCode, 
						UcrOffenseCodeType::new);;
				arresteeSegment.setUcrOffenseCodeType(ucrOffenseCodeType);
	
				processArrestReportSegmentArmedWiths(arresteeSegment, arrestee);
				arresteeSegments.add(arresteeSegment);
			}
			
			administrativeSegment.setArresteeSegments(arresteeSegments);
		}
	}

	private void processOffenders(AdministrativeSegment administrativeSegment,
			GroupAIncidentReport groupAIncidentReport) {
		if (groupAIncidentReport.getOffenderCount() > 0){
			Set<OffenderSegment> offenderSegments = new HashSet<>();
			for (org.search.nibrs.model.OffenderSegment offender: groupAIncidentReport.getOffenders()){
				OffenderSegment offenderSegment = new OffenderSegment();
				offenderSegment.setSegmentActionType(administrativeSegment.getSegmentActionType());
				offenderSegment.setAdministrativeSegment(administrativeSegment);
				
				offenderSegment.setAgeOfOffenderMax(offender.getAge().getAgeMax());
				offenderSegment.setAgeOfOffenderMin(offender.getAge().getAgeMin());
				offenderSegment.setOffenderSequenceNumber(offender.getOffenderSequenceNumber().getValue());
				
				SexOfPersonType sexOfPersonType = codeTableService.getCodeTableType(
						offender.getSex(), sexOfPersonTypeRepository::findFirstBySexOfPersonCode, SexOfPersonType::new);
				offenderSegment.setSexOfPersonType(sexOfPersonType);
				
				RaceOfPersonType raceOfPersonType = codeTableService.getCodeTableType(
						offender.getRace(), raceOfPersonTypeRepository::findFirstByRaceOfPersonCode, RaceOfPersonType::new);
				offenderSegment.setRaceOfPersonType(raceOfPersonType);
				
				EthnicityOfPersonType ethnicityOfPersonType = codeTableService.getCodeTableType(
						offender.getEthnicity(), ethnicityOfPersonTypeRepository::findFirstByEthnicityOfPersonCode, EthnicityOfPersonType::new);
				offenderSegment.setEthnicityOfPersonType(ethnicityOfPersonType);

				offenderSegments.add(offenderSegment);
			}
			
			administrativeSegment.setOffenderSegments(offenderSegments);
		}
	}

	private void processOffenses(AdministrativeSegment administrativeSegment, GroupAIncidentReport groupAIncidentReport) {
		if (groupAIncidentReport.getOffenseCount() > 0){
			Set<OffenseSegment> offenseSegments = new HashSet<>();
			
			for(org.search.nibrs.model.OffenseSegment offense: groupAIncidentReport.getOffenses()){
				OffenseSegment offenseSegment = new OffenseSegment();
				offenseSegment.setSegmentActionType(administrativeSegment.getSegmentActionType());
				offenseSegment.setAdministrativeSegment(administrativeSegment);
				
				UcrOffenseCodeType ucrOffenseCodeType = 
						codeTableService.getCodeTableType(offense.getUcrOffenseCode(), 
								ucrOffenseCodeTypeRepository::findFirstByUcrOffenseCode, UcrOffenseCodeType::new);
				offenseSegment.setUcrOffenseCodeType(ucrOffenseCodeType);
				offenseSegment.setOffenseAttemptedCompleted(offense.getOffenseAttemptedCompleted());
				
				LocationType locationType = 
						codeTableService.getCodeTableType(offense.getLocationType(), 
								locationTypeRepository::findFirstByLocationTypeCode, LocationType::new);
				offenseSegment.setLocationType(locationType);
				
				offenseSegment.setNumberOfPremisesEntered(offense.getNumberOfPremisesEntered().getValue());
				
				MethodOfEntryType methodOfEntryType = 
						codeTableService.getCodeTableType(offense.getMethodOfEntry(), 
								methodOfEntryTypeRepository::findFirstByMethodOfEntryCode, MethodOfEntryType::new);
				offenseSegment.setMethodOfEntryType(methodOfEntryType);
				processTypeOfWeaponForceInvolved(offenseSegment, offense); 
				processTypeOfCriminalActivityCount(offenseSegment, offense); 
				processOffendersSuspectedOfUsing(offenseSegment, offense);
				
				//TODO offense segment and BiasMotivationType should be a many to many relationship.  
				//A joiner table is reqruied. 
				BiasMotivationType biasMotivationType = 
						codeTableService.getCodeTableType(offense.getBiasMotivation(0), 
								biasMotivationTypeRepository::findFirstByBiasMotivationCode, BiasMotivationType::new);
				offenseSegment.setBiasMotivationType(biasMotivationType);
				
				offenseSegments.add(offenseSegment);
			}
			administrativeSegment.setOffenseSegments(offenseSegments);
		}
		
	}

	private void processOffendersSuspectedOfUsing(OffenseSegment offenseSegment,
			org.search.nibrs.model.OffenseSegment offense) {
		if (offense.getPopulatedOffendersSuspectedOfUsingCount() > 0){
			Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes = new HashSet<>(); 
			
			for (int i = 0; i < offense.getPopulatedOffendersSuspectedOfUsingCount(); i++){
				String offenderSuspectedUsingCode = StringUtils.trimToNull(offense.getOffendersSuspectedOfUsing(i));
				OffenderSuspectedOfUsingType offenderSuspectedOfUsingType = 
						codeTableService.getCodeTableType(offenderSuspectedUsingCode, offenderSuspectedOfUsingTypeRepository::findFirstByOffenderSuspectedOfUsingCode, null);
				if (offenderSuspectedOfUsingType != null){
					offenderSuspectedOfUsingTypes.add(offenderSuspectedOfUsingType); 
				}
			}
			offenseSegment.setOffenderSuspectedOfUsingTypes(offenderSuspectedOfUsingTypes);
		}
	}

	private void processTypeOfCriminalActivityCount(OffenseSegment offenseSegment,
			org.search.nibrs.model.OffenseSegment offense) {
		if (offense.getPopulatedTypeOfCriminalActivityCount() > 0){
			Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes = new HashSet<>(); 
			
			for (int i = 0; i < offense.getPopulatedTypeOfCriminalActivityCount(); i++){
				String typeOfCriminalActivityCode = StringUtils.trimToNull(offense.getTypeOfCriminalActivity(i));
				TypeOfCriminalActivityType typeOfCriminalActivityType = 
						codeTableService.getCodeTableType(typeOfCriminalActivityCode, typeOfCriminalActivityTypeRepository::findFirstByTypeOfCriminalActivityCode, null);
				if (typeOfCriminalActivityType != null){
					typeOfCriminalActivityTypes.add(typeOfCriminalActivityType); 
				}
			}
			offenseSegment.setTypeOfCriminalActivityTypes(typeOfCriminalActivityTypes);
		}
	}

	private void processTypeOfWeaponForceInvolved(OffenseSegment offenseSegment,
			org.search.nibrs.model.OffenseSegment offense) {
		Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds = new HashSet<>(); 
		if (offense.getPopulatedTypeOfWeaponForceInvolvedCount() > 0){
			for (int i = 0; i < offense.getPopulatedTypeOfWeaponForceInvolvedCount(); i++){
				String typeOfWeaponForceInvolvedCode = 
						StringUtils.trimToNull(offense.getTypeOfWeaponForceInvolved(i));
				String automaticWeaponIndicator = 
						StringUtils.trimToEmpty(offense.getAutomaticWeaponIndicator(i));
				
				if (StringUtils.isNotBlank(typeOfWeaponForceInvolvedCode)){
					Optional<TypeOfWeaponForceInvolvedType> typeOfWeaponForceInvolvedType = 
							Optional.ofNullable(codeTableService.getCodeTableType(typeOfWeaponForceInvolvedCode,
									typeOfWeaponForceInvolvedTypeRepository::findFirstByTypeOfWeaponForceInvolvedCode, 
									null));
					typeOfWeaponForceInvolvedType.ifPresent( type ->
						typeOfWeaponForceInvolveds.add(new TypeOfWeaponForceInvolved(
								offenseSegment, type, automaticWeaponIndicator))
					);
				}
			}
		}
		
		if (!typeOfWeaponForceInvolveds.isEmpty()){
			offenseSegment.setTypeOfWeaponForceInvolveds(typeOfWeaponForceInvolveds);
		}
	}

}
