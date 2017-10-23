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
package org.search.nibrs.stagingdata.arrestreport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArrestReportServiceTest {

	@Autowired
	public ArrestReportService arrestReportService; 
	
	@Test
	public void test() {
		ArrestReportSegment arrestReportSegment = getBasicArrestReportSegment();
		arrestReportService.saveArrestReportSegment(arrestReportSegment); 
		
		ArrestReportSegment persisted = 
				arrestReportService.findArrestReportSegment(arrestReportSegment.getArrestReportSegmentId());
		assertThat(persisted.getAgeOfArresteeMax(), equalTo(25));
		assertThat(persisted.getAgeOfArresteeMin(), equalTo(22));
		assertTrue(DateUtils.isSameInstant(persisted.getArrestDate(), Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant())));
		
		//TODO get the arrestDateObject
		assertThat(persisted.getArrestDateId(), equalTo(1));
		
		assertThat(persisted.getArresteeSequenceNumber(), equalTo("arrestSequenceNumber"));
		assertThat(persisted.getAgencyId(), equalTo(1));
		assertThat(persisted.getArrestTransactionNumber(), equalTo("arerstTransactionNumber"));
		assertThat(persisted.getCityIndicator(), equalTo("Y"));
		assertThat(persisted.getDispositionOfArresteeUnder18TypeId(), equalTo(1));
		assertThat(persisted.getEthnicityOfPersonTypeId(), equalTo(1));
		assertThat(persisted.getMonthOfTape(), equalTo("12"));
		assertThat(persisted.getOri(), equalTo("ori"));;
		assertThat(persisted.getRaceOfPersonTypeId(), equalTo(1));
		assertThat(persisted.getResidentStatusOfPersonTypeId(), equalTo(1));
		assertThat(persisted.getSegmentActionTypeTypeID(), equalTo(1));
		assertThat(persisted.getSexOfPersonTypeId(), equalTo(1));
		assertThat(persisted.getTypeOfArrestTypeId(), equalTo(1));
		assertThat(persisted.getUcrOffenseCodeTypeId(), equalTo(1));
		assertThat(persisted.getYearOfTape(), equalTo("2016"));
	}
	
	public ArrestReportSegment getBasicArrestReportSegment(){
		ArrestReportSegment arrestReportSegment = new ArrestReportSegment();
		arrestReportSegment.setAgeOfArresteeMax(25);
		arrestReportSegment.setAgeOfArresteeMin(22);
		arrestReportSegment.setArrestDate(Date.from(LocalDateTime.of(2016, 6, 12, 10, 7, 46).atZone(ZoneId.systemDefault()).toInstant()));
		
		//TODO get the arrestDateObject
		arrestReportSegment.setArrestDateId(1);
		
		arrestReportSegment.setArresteeSequenceNumber("arrestSequenceNumber");
		arrestReportSegment.setAgencyId(1);
		arrestReportSegment.setArrestTransactionNumber("arerstTransactionNumber");
		arrestReportSegment.setCityIndicator("Y");
		arrestReportSegment.setDispositionOfArresteeUnder18TypeId(1);
		arrestReportSegment.setEthnicityOfPersonTypeId(1);
		arrestReportSegment.setMonthOfTape("12");
		arrestReportSegment.setOri("ori");;
		arrestReportSegment.setRaceOfPersonTypeId(1);
		arrestReportSegment.setResidentStatusOfPersonTypeId(1);
		arrestReportSegment.setSegmentActionTypeTypeID(1);
		arrestReportSegment.setSexOfPersonTypeId(1);
		arrestReportSegment.setTypeOfArrestTypeId(1);
		arrestReportSegment.setUcrOffenseCodeTypeId(1);
		arrestReportSegment.setYearOfTape("2016");
		
		return arrestReportSegment; 
	}

}
