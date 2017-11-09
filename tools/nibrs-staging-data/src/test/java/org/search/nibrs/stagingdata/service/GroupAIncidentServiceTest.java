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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.model.GroupAIncidentReport;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.AggravatedAssaultHomicideCircumstancesType;
import org.search.nibrs.stagingdata.model.ArresteeSegmentWasArmedWith;
import org.search.nibrs.stagingdata.model.BiasMotivationType;
import org.search.nibrs.stagingdata.model.ClearedExceptionallyType;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.model.LocationType;
import org.search.nibrs.stagingdata.model.MethodOfEntryType;
import org.search.nibrs.stagingdata.model.OffenderSuspectedOfUsingType;
import org.search.nibrs.stagingdata.model.PropertyType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.SuspectedDrugType;
import org.search.nibrs.stagingdata.model.TypeInjuryType;
import org.search.nibrs.stagingdata.model.TypeOfCriminalActivityType;
import org.search.nibrs.stagingdata.model.TypeOfWeaponForceInvolved;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;
import org.search.nibrs.stagingdata.model.VictimOffenderAssociation;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.ArresteeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenderSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.model.segment.PropertySegment;
import org.search.nibrs.stagingdata.model.segment.VictimSegment;
import org.search.nibrs.stagingdata.repository.AdditionalJustifiableHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.AggravatedAssaultHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.ArresteeWasArmedWithTypeRepository;
import org.search.nibrs.stagingdata.repository.BiasMotivationTypeRepository;
import org.search.nibrs.stagingdata.repository.ClearedExceptionallyTypeRepository;
import org.search.nibrs.stagingdata.repository.DateTypeRepository;
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
import org.search.nibrs.stagingdata.util.BaselineIncidentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupAIncidentServiceTest {
	@Autowired
	public DateTypeRepository dateTypeRepository; 
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
	public GroupAIncidentService groupAIncidentService; 

	@Test
	public void test() {
		AdministrativeSegment administrativeSegment = getBasicAdministrativeSegment();
		groupAIncidentService.saveAdministrativeSegment(administrativeSegment); 
		
		AdministrativeSegment persisted = 
				groupAIncidentService.findAdministrativeSegment(administrativeSegment.getAdministrativeSegmentId());
		assertThat(persisted.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
		assertThat(persisted.getMonthOfTape(), equalTo("12"));
		assertThat(persisted.getYearOfTape(), equalTo("2016"));
		assertThat(persisted.getCityIndicator(), equalTo("Y"));
		assertThat(persisted.getOri(), equalTo("ori"));
		assertThat(persisted.getAgency().getAgencyOri(), equalTo("agencyORI"));
		assertThat(persisted.getIncidentNumber(), equalTo("1234568910"));
		assertTrue(DateUtils.isSameDay(persisted.getIncidentDate(), Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
		assertThat(persisted.getIncidentDateType().getDateTypeId(), equalTo(2355));
		assertNull(persisted.getReportDateIndicator());
		assertThat(persisted.getIncidentHour(), equalTo("13"));
		assertThat(persisted.getClearedExceptionallyType().getClearedExceptionallyCode(), equalTo("B"));
		
		Set<OffenseSegment> offenseSegments = persisted.getOffenseSegments();
		assertThat(offenseSegments.size(), equalTo(2));
		
		for (OffenseSegment offenseSegment: offenseSegments){
			if (offenseSegment.getBiasMotivationType().getBiasMotivationCode().equals("11")){
				assertThat(offenseSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
				assertThat(offenseSegment.getUcrOffenseCodeType().getUcrOffenseCode(), equalTo("23D"));
				assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("C"));
				assertThat(offenseSegment.getLocationType().getLocationTypeCode(), equalTo("04"));
				assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(2));
				assertThat(offenseSegment.getMethodOfEntryType().getMethodOfEntryCode(), equalTo("F"));
				
				Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes = 
						offenseSegment.getOffenderSuspectedOfUsingTypes();  
				assertThat(offenderSuspectedOfUsingTypes.size(), equalTo(2));
				assertTrue(offenderSuspectedOfUsingTypes.contains(offenderSuspectedOfUsingTypeRepository.findOne(1)));
				assertTrue(offenderSuspectedOfUsingTypes.contains(offenderSuspectedOfUsingTypeRepository.findOne(2)));
				
				Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes = 
						offenseSegment.getTypeOfCriminalActivityTypes(); 
				assertThat(typeOfCriminalActivityTypes.size(), equalTo(0));

				Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds = 
						offenseSegment.getTypeOfWeaponForceInvolveds();
				assertThat(typeOfWeaponForceInvolveds.size(), equalTo(2));
				
				for (TypeOfWeaponForceInvolved typeOfWeaponForceInvolved: typeOfWeaponForceInvolveds){
					assertThat(typeOfWeaponForceInvolved.getOffenseSegment().getOffenseSegmentId(), equalTo(offenseSegment.getOffenseSegmentId()));
					if (typeOfWeaponForceInvolved.getAutomaticWeaponIndicator().equals("A")){
						assertThat(typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getTypeOfWeaponForceInvolvedTypeId(), equalTo(120));
					}
					else{
						assertThat(typeOfWeaponForceInvolved.getAutomaticWeaponIndicator(), equalTo(""));
						assertThat(typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getTypeOfWeaponForceInvolvedTypeId(), equalTo(110));
					}
				}
				
			}
			else {
				assertThat(offenseSegment.getBiasMotivationType().getBiasMotivationCode(), equalTo("12"));
				assertThat(offenseSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
				assertThat(offenseSegment.getUcrOffenseCodeType().getUcrOffenseCode(), equalTo("26B"));
				assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("A"));
				assertThat(offenseSegment.getLocationType().getLocationTypeCode(), equalTo("02"));
				assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(1));
				assertThat(offenseSegment.getMethodOfEntryType().getMethodOfEntryCode(), equalTo("F"));
				Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes = 
						offenseSegment.getOffenderSuspectedOfUsingTypes();  
				assertThat(offenderSuspectedOfUsingTypes.size(), equalTo(0));
				
				Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes = 
						offenseSegment.getTypeOfCriminalActivityTypes(); 
				assertThat(typeOfCriminalActivityTypes.size(), equalTo(2));
				assertTrue(typeOfCriminalActivityTypes.contains(typeOfCriminalActivityTypeRepository.findOne(3)));
				assertTrue(typeOfCriminalActivityTypes.contains(typeOfCriminalActivityTypeRepository.findOne(4)));
				
				Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds = 
						offenseSegment.getTypeOfWeaponForceInvolveds();
				assertThat(typeOfWeaponForceInvolveds.size(), equalTo(0));
			}
		}
		
		Set<PropertySegment> propertySegments = persisted.getPropertySegments();
		assertThat(propertySegments.size(), equalTo(2));
		
		List<String> typeOfPropertyLossCodes = propertySegments.stream()
				.map(i->i.getTypePropertyLossEtcType().getTypePropertyLossEtcCode())
				.collect(Collectors.toList()); 
		Collections.sort(typeOfPropertyLossCodes);
		assertThat(typeOfPropertyLossCodes, equalTo(Arrays.asList("4", "7")));
		
		for (PropertySegment propertySegment: propertySegments){
			Set<PropertyType> propertyTypes = propertySegment.getPropertyTypes();
			Set<SuspectedDrugType> suspectedDrugTypes = propertySegment.getSuspectedDrugTypes();
			
			switch (propertySegment.getTypePropertyLossEtcType().getTypePropertyLossEtcCode()){
			case "7":
				assertThat(propertySegment.getAdministrativeSegment(), equalTo(persisted));
				assertThat(propertySegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(0));
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(1));
				
				assertThat(propertyTypes.size(), equalTo(1));
				PropertyType propertyType = propertyTypes.toArray(new PropertyType[]{})[0];
				assertNotNull(propertyType);
				assertThat(propertyType.getPropertyDescriptionType().getPropertyDescriptionCode(), equalTo("03")); 
				assertNull(propertyType.getRecoveredDate()); 
				assertThat(propertyType.getRecoveredDateType().getDateTypeId(), equalTo(99998));
				assertThat(propertyType.getValueOfProperty(), equalTo(10000.0));
				
				assertThat(suspectedDrugTypes.size(), equalTo(0));
				break; 
			case "4": 
				assertThat(propertySegment.getAdministrativeSegment(), equalTo(persisted));
				assertThat(propertySegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
				assertThat(propertySegment.getNumberOfRecoveredMotorVehicles(), equalTo(0));
				assertThat(propertySegment.getNumberOfStolenMotorVehicles(), equalTo(0));
				
				assertThat(propertyTypes.size(), equalTo(0));
				assertThat(suspectedDrugTypes.size(), equalTo(2));
				
				for (SuspectedDrugType suspectedDrugType: suspectedDrugTypes){
					if (suspectedDrugType.getSuspectedDrugTypeType().getSuspectedDrugTypeCode().equals("A")){
						assertThat(suspectedDrugType.getPropertySegment(), equalTo(propertySegment));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(1.0));
						assertThat(suspectedDrugType.getTypeDrugMeasurementType().getTypeDrugMeasurementTypeId(), equalTo(1)); 
					}
					else{
						assertThat(suspectedDrugType.getPropertySegment(), equalTo(propertySegment));
						assertThat(suspectedDrugType.getSuspectedDrugTypeType().getSuspectedDrugTypeCode(), equalTo("D"));
						assertThat(suspectedDrugType.getEstimatedDrugQuantity(), equalTo(1.0));
						assertThat(suspectedDrugType.getTypeDrugMeasurementType().getTypeDrugMeasurementCode(), equalTo("OZ")); 
					}
				}
				break; 
			default: 
				fail("Unexpected property loss type"); 
			}
		}
		
		Set<ArresteeSegment> arresteeSegments = persisted.getArresteeSegments();
		assertThat(arresteeSegments.size(), equalTo(1));
		
		for (ArresteeSegment arresteeSegment: arresteeSegments){
			if (arresteeSegment.getArrestTransactionNumber().equals("201040889")){
//				ArresteeSegment[arresteeSequenceNumber=01,arrestTransactionNumber=201040889,arrestDate=Tue Aug 23 00:00:00 CDT 2016,
				//typeOfArrest=S,multipleArresteeSegmentsIndicator=N,ucrArrestOffenseCode=23D,arresteeArmedWith={01,<null>},
				//automaticWeaponIndicator={<null>,<null>},residentStatus=U,dispositionOfArresteeUnder18=<null>,
				//age=33,sex=M,race=B,ethnicity=U
				assertThat(arresteeSegment.getArrestTransactionNumber(), equalTo("201040889"));
				assertThat(arresteeSegment.getArresteeSequenceNumber(), equalTo(1));
				assertTrue(DateUtils.isSameDay(arresteeSegment.getArrestDate(), Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
				
				assertThat(arresteeSegment.getArrestDateType().getDateTypeId(), equalTo(2355));
				assertThat(arresteeSegment.getArrestDateType().getDateMMDDYYYY(), equalTo("06122016"));
				assertThat(arresteeSegment.getTypeOfArrestType().getTypeOfArrestCode(), equalTo("S"));
				assertThat(arresteeSegment.getMultipleArresteeSegmentsIndicatorType().getMultipleArresteeSegmentsIndicatorCode(), 
						equalTo("N"));
				assertThat(arresteeSegment.getUcrOffenseCodeType().getUcrOffenseCode(), equalTo("23D"));
				assertThat(arresteeSegment.getResidentStatusOfPersonType().getResidentStatusOfPersonCode(), equalTo("U"));
				assertThat(arresteeSegment.getDispositionOfArresteeUnder18Type().getDispositionOfArresteeUnder18Code(), equalTo("9"));
				assertThat(arresteeSegment.getAgeOfArresteeMax(), equalTo(33));
				assertThat(arresteeSegment.getAgeOfArresteeMin(), equalTo(33));
				assertThat(arresteeSegment.getSexOfPersonType().getSexOfPersonCode(), equalTo("M"));
				assertThat(arresteeSegment.getRaceOfPersonType().getRaceOfPersonCode(), equalTo("B"));
				assertThat(arresteeSegment.getEthnicityOfPersonType().getEthnicityOfPersonCode(), equalTo("U"));
				
				Set<ArresteeSegmentWasArmedWith> arresteeSegmentWasArmedWiths = 
						arresteeSegment.getArresteeSegmentWasArmedWiths();
				assertThat(arresteeSegmentWasArmedWiths.size(), equalTo(2));
				
				for (ArresteeSegmentWasArmedWith arresteeSegmentWasArmedWith: arresteeSegmentWasArmedWiths){
					if (arresteeSegmentWasArmedWith.getAutomaticWeaponIndicator().equals("A")){
						assertThat(arresteeSegmentWasArmedWith.getArresteeWasArmedWithType().getArresteeWasArmedWithCode(), equalTo("12"));
					}
					else if (arresteeSegmentWasArmedWith.getAutomaticWeaponIndicator().equals("")){
						assertThat(arresteeSegmentWasArmedWith.getArresteeWasArmedWithType().getArresteeWasArmedWithCode(), equalTo("11"));
					}
					else {
						fail("Unexpected reportSegmentWasArmedWith.getAutomaticWeaponIndicator() value");
					}
				}

			}
			else {
				fail("Unexpected arrestee arrest transaction number");
			}

		}
		
//		2 OffenderSegment Segments:
//		OffenderSegment [age=00, sex=M, race=B, ethnicity=null, offenderSequenceNumber=01]
//		OffenderSegment [age=33, sex=M, race=B, ethnicity=null, offenderSequenceNumber=02]
		Set<OffenderSegment> offenderSegments = persisted.getOffenderSegments();
		assertThat(offenderSegments.size(), equalTo(2));
		
		for (OffenderSegment offenderSegment: offenderSegments){
			if (offenderSegment.getOffenderSequenceNumber().equals(1)){
				assertThat(offenderSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
				assertThat(offenderSegment.getAgeOfOffenderMax(), equalTo(0));
				assertThat(offenderSegment.getAgeOfOffenderMin(), equalTo(0));
				assertThat(offenderSegment.getSexOfPersonType().getSexOfPersonCode(), equalTo("M"));
				assertThat(offenderSegment.getRaceOfPersonType().getRaceOfPersonCode(), equalTo("B"));
				assertThat(offenderSegment.getEthnicityOfPersonType().getEthnicityOfPersonCode(), equalTo("U"));
			}
			else if (offenderSegment.getOffenderSequenceNumber().equals(2)){
				assertThat(offenderSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
				assertThat(offenderSegment.getAgeOfOffenderMax(), equalTo(33));
				assertThat(offenderSegment.getAgeOfOffenderMin(), equalTo(33));
				assertThat(offenderSegment.getSexOfPersonType().getSexOfPersonCode(), equalTo("M"));
				assertThat(offenderSegment.getRaceOfPersonType().getRaceOfPersonCode(), equalTo("B"));
				assertThat(offenderSegment.getEthnicityOfPersonType().getEthnicityOfPersonCode(), equalTo("U"));
			}
			else {
				fail("Unexpected offender sequence number"); 
			}
		}
		
		Set<VictimSegment> victimSegments = persisted.getVictimSegments();
		assertThat(victimSegments.size(), equalTo(1));
		
		for (VictimSegment victimSegment: victimSegments){
			if (victimSegment.getVictimSequenceNumber().equals(1)){
				assertThat(victimSegment.getAdministrativeSegment().getAdministrativeSegmentId(), equalTo(1)); 
				assertThat(victimSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I")); 
				assertThat(victimSegment.getTypeOfVictimType().getTypeOfVictimCode(), equalTo("B")); 
				assertThat(victimSegment.getOfficerActivityCircumstanceType().getOfficerActivityCircumstanceCode(), equalTo("99")); 
				assertThat(victimSegment.getOfficerAssignmentTypeType().getOfficerAssignmentTypeCode(), equalTo("9")); 
				
				assertNull(victimSegment.getAgeOfVictimMax());
				assertNull(victimSegment.getAgeOfVictimMin());
				assertThat(victimSegment.getAgeNeonateIndicator(), equalTo(0));
				assertThat(victimSegment.getAgeFirstWeekIndicator(), equalTo(0));
				assertThat(victimSegment.getAgeFirstYearIndicator(), equalTo(0));
				
				assertThat(victimSegment.getSexOfPersonType().getSexOfPersonCode(), equalTo("U")); 
				assertThat(victimSegment.getRaceOfPersonType().getRaceOfPersonCode(), equalTo("U")); 
				assertThat(victimSegment.getEthnicityOfPersonType().getEthnicityOfPersonCode(), equalTo("U")); 
				assertThat(victimSegment.getResidentStatusOfPersonType().getResidentStatusOfPersonCode(), equalTo("U")); 
				assertThat(victimSegment.getAdditionalJustifiableHomicideCircumstancesType()
						.getAdditionalJustifiableHomicideCircumstancesCode(), equalTo("9"));

				assertThat(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().size(), equalTo(2)); 
				assertTrue(victimSegment.getAggravatedAssaultHomicideCircumstancesTypes().containsAll(
						Arrays.asList(
								aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByAggravatedAssaultHomicideCircumstancesCode("01"), 
								aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByAggravatedAssaultHomicideCircumstancesCode("02") ))); 
				assertThat(victimSegment.getTypeInjuryTypes().size(), equalTo(1)); 
				assertTrue(victimSegment.getTypeInjuryTypes().contains(typeInjuryTypeRepository.findFirstByTypeInjuryCode("O"))); 
				
				assertTrue(victimSegment.getOffenseSegments().containsAll(persisted.getOffenseSegments()));
				
				Set<VictimOffenderAssociation> victimOffenderAssociations = victimSegment.getVictimOffenderAssociations();
				assertThat(victimOffenderAssociations.size(), equalTo(1));
				
				VictimOffenderAssociation victimOffenderAssociation = new ArrayList<>(victimOffenderAssociations).get(0);
				
				assertThat(victimOffenderAssociation.getVictimSegment().getVictimSegmentId(), equalTo(victimSegment.getVictimSegmentId()));
				assertThat(victimOffenderAssociation.getOffenderSegment().getOffenderSequenceNumber(), equalTo(1));
				assertThat(victimOffenderAssociation.getVictimOffenderRelationshipType().getVictimOffenderRelationshipCode(), equalTo("AQ"));

			}
			else {
				fail("Unexpected victim sequence number"); 
			}
		}
	}
	
	@SuppressWarnings("serial")
	public AdministrativeSegment getBasicAdministrativeSegment(){
		
		AdministrativeSegment administrativeSegment = new AdministrativeSegment();
		SegmentActionTypeType segmentActionTypeType = segmentActionTypeRepository.findFirstBySegmentActionTypeCode("I");
		administrativeSegment.setSegmentActionType(segmentActionTypeType);
		administrativeSegment.setMonthOfTape("12");
		administrativeSegment.setYearOfTape("2016");
		administrativeSegment.setCityIndicator("Y");
		administrativeSegment.setOri("ori");;
		
		Agency agency = agencyRepository.findFirstByAgencyOri("agencyORI");
		administrativeSegment.setAgency(agency);
		administrativeSegment.setIncidentNumber("1234568910");
		administrativeSegment.setIncidentDate(Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		DateType incidentDateType = dateTypeRepository.findFirstByDateMMDDYYYY("06122016");
		administrativeSegment.setIncidentDateType(incidentDateType);
		
		administrativeSegment.setReportDateIndicator(null);  //'R' for report. Must be empty when incident is known.    
		administrativeSegment.setIncidentHour("13");  // allowed value 0-23.  
		ClearedExceptionallyType clearedExceptionallyType
			= clearedExceptionallyTypeRepository.findFirstByClearedExceptionallyCode("B");
		administrativeSegment.setClearedExceptionallyType(clearedExceptionallyType);
		
		
//		Offense segment 1 
		Set<OffenseSegment> offenseSegments = new HashSet<>();
		OffenseSegment offenseSegment = new OffenseSegment();
		offenseSegment.setSegmentActionType(segmentActionTypeType);
		
		UcrOffenseCodeType ucrOffenseCode = ucrOffenseCodeTypeRepository.findFirstByUcrOffenseCode("23D");
		offenseSegment.setUcrOffenseCodeType(ucrOffenseCode);
		
		offenseSegment.setOffenseAttemptedCompleted("C");  //Allowed values C or A 
		
		LocationType locationType = locationTypeRepository.findFirstByLocationTypeCode("04");
		offenseSegment.setLocationType(locationType);
		
		offenseSegment.setNumberOfPremisesEntered(2);
		
		MethodOfEntryType methodOfEntryType = methodOfEntryTypeRepository.findFirstByMethodOfEntryCode("F");
		offenseSegment.setMethodOfEntryType(methodOfEntryType);
		BiasMotivationType biasMotivationType = biasMotivationTypeRepository.findFirstByBiasMotivationCode("11");
		offenseSegment.setBiasMotivationType(biasMotivationType);;
		offenseSegment.setAdministrativeSegment(administrativeSegment);
		
		offenseSegment.setOffenderSuspectedOfUsingTypes(new HashSet<OffenderSuspectedOfUsingType>(){{
			add(offenderSuspectedOfUsingTypeRepository.findOne(1));
			add(offenderSuspectedOfUsingTypeRepository.findOne(2));
		}});
		
		TypeOfWeaponForceInvolved typeOfWeaponForceInvolved1 = new TypeOfWeaponForceInvolved();
		typeOfWeaponForceInvolved1.setOffenseSegment(offenseSegment);
		typeOfWeaponForceInvolved1.setAutomaticWeaponIndicator("A");
		typeOfWeaponForceInvolved1.setTypeOfWeaponForceInvolvedType(typeOfWeaponForceInvolvedTypeRepository.findOne(120));
		
		TypeOfWeaponForceInvolved typeOfWeaponForceInvolved2 = new TypeOfWeaponForceInvolved();
		typeOfWeaponForceInvolved2.setOffenseSegment(offenseSegment);
		typeOfWeaponForceInvolved2.setAutomaticWeaponIndicator("");
		typeOfWeaponForceInvolved2.setTypeOfWeaponForceInvolvedType(typeOfWeaponForceInvolvedTypeRepository.findOne(110));
		
		offenseSegment.setTypeOfWeaponForceInvolveds(new HashSet<TypeOfWeaponForceInvolved>(){{
			add(typeOfWeaponForceInvolved1);
			add(typeOfWeaponForceInvolved2);
		}});
		
		offenseSegments.add(offenseSegment);
//		Offense segment 2 
		OffenseSegment offenseSegment2 = new OffenseSegment();
		offenseSegment2.setSegmentActionType(segmentActionTypeType);
		
		UcrOffenseCodeType ucrOffenseCode2 = ucrOffenseCodeTypeRepository.findFirstByUcrOffenseCode("26B");
		offenseSegment2.setUcrOffenseCodeType(ucrOffenseCode2);
		
		offenseSegment2.setOffenseAttemptedCompleted("A");  //Allowed values C or A 
		
		LocationType locationType2 = locationTypeRepository.findFirstByLocationTypeCode("02");
		offenseSegment2.setLocationType(locationType2);
		
		offenseSegment2.setNumberOfPremisesEntered(1);
		
		MethodOfEntryType methodOfEntryType2 = methodOfEntryTypeRepository.findFirstByMethodOfEntryCode("F");
		offenseSegment2.setMethodOfEntryType(methodOfEntryType2);
		BiasMotivationType biasMotivationType2 = biasMotivationTypeRepository.findFirstByBiasMotivationCode("12");
		offenseSegment2.setBiasMotivationType(biasMotivationType2);;
		offenseSegment2.setAdministrativeSegment(administrativeSegment);

		offenseSegment2.setTypeOfCriminalActivityTypes(new HashSet<TypeOfCriminalActivityType>(){{
			add(typeOfCriminalActivityTypeRepository.findOne(3));
			add(typeOfCriminalActivityTypeRepository.findOne(4));
		}});

		offenseSegments.add(offenseSegment2);
		administrativeSegment.setOffenseSegments(offenseSegments);
		
//		PropertySegment 1
		PropertySegment propertySegment1 = new PropertySegment();
		propertySegment1.setSegmentActionType(segmentActionTypeType);
		propertySegment1.setAdministrativeSegment(administrativeSegment);
		propertySegment1.setTypePropertyLossEtcType(typePropertyLossEtcTypeRepository.findOne(7));
		propertySegment1.setNumberOfRecoveredMotorVehicles(0);
		propertySegment1.setNumberOfStolenMotorVehicles(1);
		
		PropertyType propertyType1 = new PropertyType();
		propertyType1.setPropertySegment(propertySegment1);
		propertyType1.setPropertyDescriptionType(propertyDescriptionTypeRepository.findFirstByPropertyDescriptionCode("03"));
		propertyType1.setValueOfProperty(10000.0);
		propertyType1.setRecoveredDateType(dateTypeRepository.findOne(99998));
		
		propertySegment1.setPropertyTypes(new HashSet<PropertyType>(){{
			add(propertyType1);
		}});
		
		Set<PropertySegment> propertySegments = new HashSet<>();
		propertySegments.add(propertySegment1);
		
//		PropertySegment 2
		PropertySegment propertySegment2 = new PropertySegment();
		propertySegment2.setSegmentActionType(segmentActionTypeType);
		propertySegment2.setAdministrativeSegment(administrativeSegment);
		propertySegment2.setTypePropertyLossEtcType(typePropertyLossEtcTypeRepository.findOne(4));
		propertySegment2.setNumberOfRecoveredMotorVehicles(0);
		propertySegment2.setNumberOfStolenMotorVehicles(0);
		
		SuspectedDrugType suspectedDrugType1 = new SuspectedDrugType();
		suspectedDrugType1.setPropertySegment(propertySegment2);
		suspectedDrugType1.setSuspectedDrugTypeType(suspectedDrugTypeTypeRepository.findFirstBySuspectedDrugTypeCode("A"));
		suspectedDrugType1.setEstimatedDrugQuantity(1.0);
		suspectedDrugType1.setTypeDrugMeasurementType(typeDrugMeasurementTypeRepository.findOne(1));
		
		SuspectedDrugType suspectedDrugType2 = new SuspectedDrugType();
		suspectedDrugType2.setPropertySegment(propertySegment2);
		suspectedDrugType2.setSuspectedDrugTypeType(suspectedDrugTypeTypeRepository.findFirstBySuspectedDrugTypeCode("D"));
		suspectedDrugType2.setEstimatedDrugQuantity(1.0);
		suspectedDrugType2.setTypeDrugMeasurementType(typeDrugMeasurementTypeRepository.findFirstByTypeDrugMeasurementCode("OZ"));
		
		propertySegment2.setSuspectedDrugTypes(new HashSet<SuspectedDrugType>(){{
			add(suspectedDrugType1);
			add(suspectedDrugType2);
		}});
		
		propertySegments.add(propertySegment2);
		
		administrativeSegment.setPropertySegments(propertySegments);
		
		
//		1 arrestee segment 
//		ArresteeSegment[arresteeSequenceNumber=01,arrestTransactionNumber=201040889,arrestDate=Tue Aug 23 00:00:00 CDT 2016,
		//typeOfArrest=S,multipleArresteeSegmentsIndicator=N,ucrArrestOffenseCode=23D,arresteeArmedWith={01,<null>},
		//automaticWeaponIndicator={<null>,<null>},residentStatus=U,dispositionOfArresteeUnder18=<null>,
		//age=33,sex=M,race=B,ethnicity=U
		
		ArresteeSegment arresteeSegment = new ArresteeSegment();
		arresteeSegment.setAdministrativeSegment(administrativeSegment);
		arresteeSegment.setAgeOfArresteeMax(33);
		arresteeSegment.setAgeOfArresteeMin(33);
		arresteeSegment.setArrestDate(Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		
		DateType arrestDateType = dateTypeRepository.findFirstByDateMMDDYYYY("06122016");
		arresteeSegment.setArrestDateType(arrestDateType);
		
		arresteeSegment.setArresteeSequenceNumber(1);
		arresteeSegment.setArrestTransactionNumber("201040889");
		
		arresteeSegment.setDispositionOfArresteeUnder18Type(
				dispositionOfArresteeUnder18TypeRepository.findFirstByDispositionOfArresteeUnder18Code("9"));
		arresteeSegment.setEthnicityOfPersonType(ethnicityOfPersonTypeRepository.findFirstByEthnicityOfPersonCode("U"));
		arresteeSegment.setRaceOfPersonType(raceOfPersonTypeRepository.findFirstByRaceOfPersonCode("B"));
		arresteeSegment.setResidentStatusOfPersonType(residentStatusOfPersonTypeRepository.findFirstByResidentStatusOfPersonCode("U"));
		arresteeSegment.setSegmentActionType(segmentActionTypeRepository.findFirstBySegmentActionTypeCode("I"));
		arresteeSegment.setSexOfPersonType(sexOfPersonTypeRepository.findFirstBySexOfPersonCode("M"));
		arresteeSegment.setTypeOfArrestType(typeOfArrestTypeRepository.findFirstByTypeOfArrestCode("S"));
		arresteeSegment.setUcrOffenseCodeType(ucrOffenseCodeTypeRepository.findFirstByUcrOffenseCode("23D"));
		arresteeSegment.setMultipleArresteeSegmentsIndicatorType(
				multipleArresteeSegmentsIndicatorTypeRepository.findFirstByMultipleArresteeSegmentsIndicatorCode("N"));
		
		ArresteeSegmentWasArmedWith arresteeSegmentWasArmedWith1 = new ArresteeSegmentWasArmedWith();
		arresteeSegmentWasArmedWith1.setArresteeSegment(arresteeSegment);
		arresteeSegmentWasArmedWith1.setAutomaticWeaponIndicator("A");
		arresteeSegmentWasArmedWith1.setArresteeWasArmedWithType(arresteeWasArmedWithTypeRepository.findOne(120));
		
		ArresteeSegmentWasArmedWith arresteeSegmentWasArmedWith2 = new ArresteeSegmentWasArmedWith();
		arresteeSegmentWasArmedWith2.setArresteeSegment(arresteeSegment);
		arresteeSegmentWasArmedWith2.setAutomaticWeaponIndicator("");
		arresteeSegmentWasArmedWith2.setArresteeWasArmedWithType(arresteeWasArmedWithTypeRepository.findOne(110));
		
		arresteeSegment.setArresteeSegmentWasArmedWiths(new HashSet<ArresteeSegmentWasArmedWith>(){{
			add(arresteeSegmentWasArmedWith1);
			add(arresteeSegmentWasArmedWith2);
		}});
		
		administrativeSegment.setArresteeSegments(new HashSet<ArresteeSegment>(){{
			add(arresteeSegment);
		}});

//		2 OffenderSegment Segments:
//		OffenderSegment [age=00, sex=M, race=B, ethnicity=null, offenderSequenceNumber=01]
//		OffenderSegment [age=33, sex=M, race=B, ethnicity=null, offenderSequenceNumber=02]
		OffenderSegment offenderSegment1 = new OffenderSegment();
		offenderSegment1.setAdministrativeSegment(administrativeSegment);
		offenderSegment1.setSegmentActionType(segmentActionTypeType);
		offenderSegment1.setAgeOfOffenderMax(0);
		offenderSegment1.setAgeOfOffenderMin(0);
		
		offenderSegment1.setSexOfPersonType(sexOfPersonTypeRepository.findFirstBySexOfPersonCode("M"));
		offenderSegment1.setRaceOfPersonType(raceOfPersonTypeRepository.findFirstByRaceOfPersonCode("B"));
		offenderSegment1.setEthnicityOfPersonType(ethnicityOfPersonTypeRepository.findFirstByEthnicityOfPersonCode("U"));
		offenderSegment1.setOffenderSequenceNumber(1);

		OffenderSegment offenderSegment2 = new OffenderSegment();
		offenderSegment2.setAdministrativeSegment(administrativeSegment);
		offenderSegment2.setSegmentActionType(segmentActionTypeType);
		offenderSegment2.setAgeOfOffenderMax(33);
		offenderSegment2.setAgeOfOffenderMin(33);
		
		offenderSegment2.setSexOfPersonType(sexOfPersonTypeRepository.findFirstBySexOfPersonCode("M"));
		offenderSegment2.setRaceOfPersonType(raceOfPersonTypeRepository.findFirstByRaceOfPersonCode("B"));
		offenderSegment2.setEthnicityOfPersonType(ethnicityOfPersonTypeRepository.findFirstByEthnicityOfPersonCode("U"));
		offenderSegment2.setOffenderSequenceNumber(2);
		
		administrativeSegment.setOffenderSegments(new HashSet<OffenderSegment>(){{
			add(offenderSegment1);
			add(offenderSegment2);
		}});
		
//		1 VictimSegment Segments:
//		VictimSegment [age=null, sex=null, race=null, ethnicity=null, victimSequenceNumber=01, 
//		ucrOffenseCodeConnection=[26B, 23D, null, null, null, null, null, null, null, null], 
//		typeOfVictim=B, residentStatus=null, aggravatedAssaultHomicideCircumstances=[null, null], 
//		additionalJustifiableHomicideCircumstances=null, typeOfInjury=[null, null, null, null, null], 
//		offenderNumberRelated=[null, null, null, null, null, null, null, null, null, null], 
//		victimOffenderRelationship=[null, null, null, null, null, null, null, null, null, null], 
//		typeOfOfficerActivityCircumstance=null, officerAssignmentType=null, officerOtherJurisdictionORI=null, 
//		populatedAggravatedAssaultHomicideCircumstancesCount=2, populatedTypeOfInjuryCount=5, 
//		populatedUcrOffenseCodeConnectionCount=10, populatedOffenderNumberRelatedCount=0]
		VictimSegment victimSegment = new VictimSegment();
		victimSegment.setAdministrativeSegment(administrativeSegment);
		victimSegment.setSegmentActionType(segmentActionTypeType);
		victimSegment.setVictimSequenceNumber(1);
		victimSegment.setTypeOfVictimType(typeOfVictimTypeRepository.findFirstByTypeOfVictimCode("B"));
		victimSegment.setOfficerActivityCircumstanceType(officerActivityCircumstanceTypeRepository.findFirstByOfficerActivityCircumstanceCode("99"));
		victimSegment.setOfficerAssignmentTypeType(officerAssignmentTypeTypeRepository.findFirstByOfficerAssignmentTypeCode("9"));
		
		victimSegment.setAgeNeonateIndicator(0);
		victimSegment.setAgeFirstWeekIndicator(0);
		victimSegment.setAgeFirstYearIndicator(0);
		
		victimSegment.setSexOfPersonType(sexOfPersonTypeRepository.findFirstBySexOfPersonCode("U"));
		victimSegment.setRaceOfPersonType(raceOfPersonTypeRepository.findFirstByRaceOfPersonCode("U"));
		victimSegment.setEthnicityOfPersonType(ethnicityOfPersonTypeRepository.findFirstByEthnicityOfPersonCode("U"));
		victimSegment.setResidentStatusOfPersonType(residentStatusOfPersonTypeRepository.findFirstByResidentStatusOfPersonCode("U"));
		
		victimSegment.setAdditionalJustifiableHomicideCircumstancesType(
				additionalJustifiableHomicideCircumstancesTypeRepository.findFirstByAdditionalJustifiableHomicideCircumstancesCode("9"));
		
		
		victimSegment.setTypeInjuryTypes(new HashSet<TypeInjuryType>(){{
			add(typeInjuryTypeRepository.findFirstByTypeInjuryCode("O"));
		}});

		victimSegment.setOffenseSegments(new HashSet<OffenseSegment>(){{
			add(offenseSegment);
			add(offenseSegment2);
		}});
		
		victimSegment.setAggravatedAssaultHomicideCircumstancesTypes(new HashSet<AggravatedAssaultHomicideCircumstancesType>(){{
			add(aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByAggravatedAssaultHomicideCircumstancesCode("01"));
			add(aggravatedAssaultHomicideCircumstancesTypeRepository.findFirstByAggravatedAssaultHomicideCircumstancesCode("02"));
		}});
		
		VictimOffenderAssociation victimOffenderAssociation1 = new VictimOffenderAssociation();
		victimOffenderAssociation1.setVictimSegment(victimSegment);
		victimOffenderAssociation1.setOffenderSegment(offenderSegment1);
		victimOffenderAssociation1.setVictimOffenderRelationshipType(
				victimOffenderRelationshipTypeRepository.findFirstByVictimOffenderRelationshipCode("AQ"));;
		
		victimSegment.setVictimOffenderAssociations(new HashSet<VictimOffenderAssociation>(){{
			add(victimOffenderAssociation1);
		}});

		administrativeSegment.setVictimSegments(new HashSet<VictimSegment>(){{
			add(victimSegment);
		}});
		
		return administrativeSegment; 
	}

	@Test
	public void saveGroupAIncidentReportTest(){
		GroupAIncidentReport groupAIncidentReport = BaselineIncidentFactory.getBaselineIncident();
		AdministrativeSegment administrativeSegment = groupAIncidentService.saveGroupAIncidentReport(groupAIncidentReport);
		
		AdministrativeSegment persisted = 
				groupAIncidentService.findAdministrativeSegment(administrativeSegment.getAdministrativeSegmentId());

		assertNotNull(persisted);
		assertThat(persisted.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
		assertThat(persisted.getMonthOfTape(), equalTo("05"));
		assertThat(persisted.getYearOfTape(), equalTo("2016"));
		assertThat(persisted.getCityIndicator(), equalTo("Y"));
		assertThat(persisted.getOri(), equalTo("WA1234567"));
		assertThat(persisted.getAgency().getAgencyId(), equalTo(99998));
		assertThat(persisted.getIncidentNumber(), equalTo("54236732"));
		assertTrue(DateUtils.isSameDay(persisted.getIncidentDate(), Date.from(LocalDateTime.of(2016, 5, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
		assertThat(persisted.getIncidentDateType().getDateTypeId(), equalTo(2324));
		assertNull(persisted.getReportDateIndicator());
		assertThat(persisted.getIncidentHour(), equalTo(""));
		assertThat(persisted.getClearedExceptionallyType().getClearedExceptionallyCode(), equalTo("A"));
		
//		1 OffenseSegment Segments:
//		OffenseSegment [ucrOffenseCode=13A, offenseAttemptedCompleted=C, 
	//offendersSuspectedOfUsing=[N, null, null], locationType=15, numberOfPremisesEntered=null, 
	//methodOfEntry=null, 
	//typeOfCriminalActivity=[J, null, null], 
	//typeOfWeaponForceInvolved=[14, null, null], 
	//automaticWeaponIndicator=[ , null, null], 
	//biasMotivation=[15, null, null, null, null], 
	//populatedBiasMotivationCount=1, 
	//populatedTypeOfWeaponForceInvolvedCount=1, 
	//populatedTypeOfCriminalActivityCount=1, 
	//populatedOffendersSuspectedOfUsingCount=1]

		Set<OffenseSegment> offenseSegments = persisted.getOffenseSegments();
		assertThat(offenseSegments.size(), equalTo(1));
		
		OffenseSegment offenseSegment = offenseSegments.stream().findFirst().get();
		assertThat(offenseSegment.getBiasMotivationType().getBiasMotivationCode(), equalTo("15"));
		assertThat(offenseSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
		assertThat(offenseSegment.getUcrOffenseCodeType().getUcrOffenseCode(), equalTo("13A"));
		assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("C"));
		assertThat(offenseSegment.getLocationType().getLocationTypeCode(), equalTo("15"));
		assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(null));
		assertThat(offenseSegment.getMethodOfEntryType().getMethodOfEntryTypeId(), equalTo(99998));
		Set<OffenderSuspectedOfUsingType> offenderSuspectedOfUsingTypes = 
				offenseSegment.getOffenderSuspectedOfUsingTypes();  
		assertThat(offenderSuspectedOfUsingTypes.size(), equalTo(1));
		assertThat(offenderSuspectedOfUsingTypes.contains(offenderSuspectedOfUsingTypeRepository.findFirstByOffenderSuspectedOfUsingCode("N")), equalTo(true));
		
		Set<TypeOfCriminalActivityType> typeOfCriminalActivityTypes = 
				offenseSegment.getTypeOfCriminalActivityTypes(); 
		assertThat(typeOfCriminalActivityTypes.size(), equalTo(1));
		assertTrue(typeOfCriminalActivityTypes.contains(typeOfCriminalActivityTypeRepository.findFirstByTypeOfCriminalActivityCode("J")));
		
		Set<TypeOfWeaponForceInvolved> typeOfWeaponForceInvolveds = 
				offenseSegment.getTypeOfWeaponForceInvolveds();
		assertThat(typeOfWeaponForceInvolveds.size(), equalTo(1));
		TypeOfWeaponForceInvolved typeOfWeaponForceInvolved = typeOfWeaponForceInvolveds.stream().findFirst().get();
		assertThat(typeOfWeaponForceInvolved.getOffenseSegment().getOffenseSegmentId(), equalTo(offenseSegment.getOffenseSegmentId()));
		assertThat(typeOfWeaponForceInvolved.getAutomaticWeaponIndicator(), equalTo(""));
		assertThat(typeOfWeaponForceInvolved.getTypeOfWeaponForceInvolvedType().getTypeOfWeaponForceInvolvedCode(), equalTo("14"));
//	    "offenders": [
//      {
//          "segmentType": "5",
//          "age": {
//              "ageMin": 22,
//              "ageMax": 22,
//              "nonNumericAge": null,
//              "error": null,
//              "unknown": false,
//              "nonNumeric": false,
//              "average": 22,
//              "ageRange": false
//          },
//          "sex": "M",
//          "race": "W",
//          "ethnicity": "H",
//          "offenderSequenceNumber": {
//              "value": 1,
//              "missing": false,
//              "invalid": false,
//              "validationError": null
//          },
//      }
//  ],
		Set<OffenderSegment> offenderSegments = persisted.getOffenderSegments();
		assertThat(offenderSegments.size(), equalTo(1));
		OffenderSegment offenderSegment = offenderSegments.stream().findFirst().get();
		
		assertThat(offenderSegment.getOffenderSequenceNumber(), equalTo(1)); 
		assertThat(offenderSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
		assertThat(offenderSegment.getAgeOfOffenderMax(), equalTo(22));
		assertThat(offenderSegment.getAgeOfOffenderMin(), equalTo(22));
		assertThat(offenderSegment.getSexOfPersonType().getSexOfPersonCode(), equalTo("M"));
		assertThat(offenderSegment.getRaceOfPersonType().getRaceOfPersonCode(), equalTo("W"));
		assertThat(offenderSegment.getEthnicityOfPersonType().getEthnicityOfPersonCode(), equalTo("H"));

	}
}
