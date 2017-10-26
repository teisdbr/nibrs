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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.stagingdata.model.AdministrativeSegment;
import org.search.nibrs.stagingdata.model.Agency;
import org.search.nibrs.stagingdata.model.BiasMotivationType;
import org.search.nibrs.stagingdata.model.ClearedExceptionallyType;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.model.LocationType;
import org.search.nibrs.stagingdata.model.MethodOfEntryType;
import org.search.nibrs.stagingdata.model.OffenseSegment;
import org.search.nibrs.stagingdata.model.SegmentActionTypeType;
import org.search.nibrs.stagingdata.model.UcrOffenseCodeType;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.BiasMotivationTypeRepository;
import org.search.nibrs.stagingdata.repository.ClearedExceptionallyTypeRepository;
import org.search.nibrs.stagingdata.repository.DateTypeRepository;
import org.search.nibrs.stagingdata.repository.LocationTypeRepository;
import org.search.nibrs.stagingdata.repository.MethodOfEntryTypeRepository;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeRepository;
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
			if (offenseSegment.getOffenseSegmentId() == 1){
				assertThat(offenseSegment.getBiasMotivationType().getBiasMotivationCode(), equalTo("11"));
				assertThat(offenseSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
				assertThat(offenseSegment.getUcrOffenseCodeType().getUcrOffenseCode(), equalTo("520"));
				assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("C"));
				assertThat(offenseSegment.getLocationType().getLocationTypeCode(), equalTo("04"));
				assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(2));
				assertThat(offenseSegment.getMethodOfEntryType().getMethodOfEntryCode(), equalTo("F"));
			}
			else {
				assertThat(offenseSegment.getBiasMotivationType().getBiasMotivationCode(), equalTo("12"));
				assertThat(offenseSegment.getSegmentActionType().getSegmentActionTypeCode(), equalTo("I"));
				assertThat(offenseSegment.getUcrOffenseCodeType().getUcrOffenseCode(), equalTo("35A"));
				assertThat(offenseSegment.getOffenseAttemptedCompleted(), equalTo("A"));
				assertThat(offenseSegment.getLocationType().getLocationTypeCode(), equalTo("02"));
				assertThat(offenseSegment.getNumberOfPremisesEntered(), equalTo(1));
				assertThat(offenseSegment.getMethodOfEntryType().getMethodOfEntryCode(), equalTo("F"));
			}
		}
	}
	
	public AdministrativeSegment getBasicAdministrativeSegment(){
		
		AdministrativeSegment administrativeSegment = new AdministrativeSegment();
		SegmentActionTypeType segmentActionTypeType = segmentActionTypeRepository.findBySegmentActionTypeCode("I").get(0);
		administrativeSegment.setSegmentActionType(segmentActionTypeType);
		administrativeSegment.setMonthOfTape("12");
		administrativeSegment.setYearOfTape("2016");
		administrativeSegment.setCityIndicator("Y");
		administrativeSegment.setOri("ori");;
		
		Agency agency = agencyRepository.findByAgencyOri("agencyORI").get(0);
		administrativeSegment.setAgency(agency);
		administrativeSegment.setIncidentNumber("1234568910");
		administrativeSegment.setIncidentDate(Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		DateType incidentDateType = dateTypeRepository.findByDateMMDDYYYY("06122016").get(0);
		administrativeSegment.setIncidentDateType(incidentDateType);
		
		administrativeSegment.setReportDateIndicator(null);  //'R' for report. Must be empty when incident is known.    
		administrativeSegment.setIncidentHour(13);  // allowed value 0-23.  
		ClearedExceptionallyType clearedExceptionallyType
			= clearedExceptionallyTypeRepository.findByClearedExceptionallyCode("B").get(0);
		administrativeSegment.setClearedExceptionallyType(clearedExceptionallyType);
		
		
//		Offense segment 1 
		Set<OffenseSegment> offenseSegments = new HashSet<>();
		OffenseSegment offenseSegment = new OffenseSegment();
		offenseSegment.setSegmentActionType(segmentActionTypeType);
		
		UcrOffenseCodeType ucrOffenseCode = ucrOffenseCodeTypeRepository.findByUcrOffenseCode("520").get(0);
		offenseSegment.setUcrOffenseCodeType(ucrOffenseCode);
		
		offenseSegment.setOffenseAttemptedCompleted("C");  //Allowed values C or A 
		
		LocationType locationType = locationTypeRepository.findByLocationTypeCode("04").get(0);
		offenseSegment.setLocationType(locationType);
		
		offenseSegment.setNumberOfPremisesEntered(2);
		
		MethodOfEntryType methodOfEntryType = methodOfEntryTypeRepository.findByMethodOfEntryCode("F").get(0);
		offenseSegment.setMethodOfEntryType(methodOfEntryType);
		BiasMotivationType biasMotivationType = biasMotivationTypeRepository.findByBiasMotivationCode("11").get(0);
		offenseSegment.setBiasMotivationType(biasMotivationType);;
		offenseSegment.setAdministrativeSegment(administrativeSegment);
		
		offenseSegments.add(offenseSegment);
//		Offense segment 2 
		OffenseSegment offenseSegment2 = new OffenseSegment();
		offenseSegment2.setSegmentActionType(segmentActionTypeType);
		
		UcrOffenseCodeType ucrOffenseCode2 = ucrOffenseCodeTypeRepository.findByUcrOffenseCode("35A").get(0);
		offenseSegment2.setUcrOffenseCodeType(ucrOffenseCode2);
		
		offenseSegment2.setOffenseAttemptedCompleted("A");  //Allowed values C or A 
		
		LocationType locationType2 = locationTypeRepository.findByLocationTypeCode("02").get(0);
		offenseSegment2.setLocationType(locationType2);
		
		offenseSegment2.setNumberOfPremisesEntered(1);
		
		MethodOfEntryType methodOfEntryType2 = methodOfEntryTypeRepository.findByMethodOfEntryCode("F").get(0);
		offenseSegment2.setMethodOfEntryType(methodOfEntryType2);
		BiasMotivationType biasMotivationType2 = biasMotivationTypeRepository.findByBiasMotivationCode("12").get(0);
		offenseSegment2.setBiasMotivationType(biasMotivationType2);;
		offenseSegment2.setAdministrativeSegment(administrativeSegment);
		
		offenseSegments.add(offenseSegment2);
		administrativeSegment.setOffenseSegments(offenseSegments);
		return administrativeSegment; 
	}


}
