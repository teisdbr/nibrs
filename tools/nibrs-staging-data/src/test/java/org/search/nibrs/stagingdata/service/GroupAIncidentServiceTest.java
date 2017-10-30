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
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.BiasMotivationType;
import org.search.nibrs.stagingdata.model.ClearedExceptionallyType;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.model.LocationType;
import org.search.nibrs.stagingdata.model.MethodOfEntryType;
import org.search.nibrs.stagingdata.model.OffenderSuspectedOfUsingType;
import org.search.nibrs.stagingdata.model.PropertyType;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.SuspectedDrugType;
import org.search.nibrs.stagingdata.model.TypeOfCriminalActivityType;
import org.search.nibrs.stagingdata.model.TypeOfWeaponForceInvolved;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;
import org.search.nibrs.stagingdata.model.segment.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.segment.OffenseSegment;
import org.search.nibrs.stagingdata.model.segment.PropertySegment;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.BiasMotivationTypeRepository;
import org.search.nibrs.stagingdata.repository.ClearedExceptionallyTypeRepository;
import org.search.nibrs.stagingdata.repository.DateTypeRepository;
import org.search.nibrs.stagingdata.repository.LocationTypeRepository;
import org.search.nibrs.stagingdata.repository.MethodOfEntryTypeRepository;
import org.search.nibrs.stagingdata.repository.OffenderSuspectedOfUsingTypeRepository;
import org.search.nibrs.stagingdata.repository.PropertyDescriptionTypeRepository;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeRepository;
import org.search.nibrs.stagingdata.repository.SuspectedDrugTypeTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeDrugMeasurementTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfCriminalActivityTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfWeaponForceInvolvedTypeRepository;
import org.search.nibrs.stagingdata.repository.TypePropertyLossEtcTypeRepository;
import org.search.nibrs.stagingdata.repository.UcrOffenseCodeTypeRepository;
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
		assertThat(persisted.getIncidentHour(), equalTo(13));
		assertThat(persisted.getClearedExceptionallyType().getClearedExceptionallyCode(), equalTo("B"));
		
		Set<OffenseSegment> offenseSegments = persisted.getOffenseSegments();
		assertThat(offenseSegments.size(), equalTo(2));
		
		for (OffenseSegment offenseSegment: offenseSegments){
			if (offenseSegment.getBiasMotivationType().getBiasMotivationCode().equals("11")){
				assertThat(offenseSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
				assertThat(offenseSegment.getUcrOffenseCodeType().getUcrOffenseCode(), equalTo("520"));
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
				assertThat(offenseSegment.getUcrOffenseCodeType().getUcrOffenseCode(), equalTo("35A"));
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
				assertNull(propertyType.getRecoveredDateType());
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
		administrativeSegment.setIncidentHour(13);  // allowed value 0-23.  
		ClearedExceptionallyType clearedExceptionallyType
			= clearedExceptionallyTypeRepository.findFirstByClearedExceptionallyCode("B");
		administrativeSegment.setClearedExceptionallyType(clearedExceptionallyType);
		
		
//		Offense segment 1 
		Set<OffenseSegment> offenseSegments = new HashSet<>();
		OffenseSegment offenseSegment = new OffenseSegment();
		offenseSegment.setSegmentActionType(segmentActionTypeType);
		
		UcrOffenseCodeType ucrOffenseCode = ucrOffenseCodeTypeRepository.findFirstByUcrOffenseCode("520");
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
		
		UcrOffenseCodeType ucrOffenseCode2 = ucrOffenseCodeTypeRepository.findFirstByUcrOffenseCode("35A");
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
		return administrativeSegment; 
	}


}
