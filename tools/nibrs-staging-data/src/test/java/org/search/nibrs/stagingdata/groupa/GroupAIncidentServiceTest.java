package org.search.nibrs.stagingdata.groupa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.search.nibrs.stagingdata.repository.Agency;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.ClearedExceptionallyType;
import org.search.nibrs.stagingdata.repository.ClearedExceptionallyTypeRepository;
import org.search.nibrs.stagingdata.repository.DateType;
import org.search.nibrs.stagingdata.repository.DateTypeRepository;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeRepository;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeType;
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
		
		return administrativeSegment; 
	}


}
