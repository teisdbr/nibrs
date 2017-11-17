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
package org.search.nibrs.stagingdata.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.GroupBArrestReport;
import org.search.nibrs.stagingdata.model.segment.ArrestReportSegment;
import org.search.nibrs.stagingdata.service.ArrestReportService;
import org.search.nibrs.stagingdata.util.BaselineIncidentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArrestReportController {
	@Autowired
	private ArrestReportService arrestReportService;
	
	@RequestMapping("/arrestReports")
	public List<ArrestReportSegment> getAllArrestReport(){
		return arrestReportService.findAllArrestReportSegment();
	}
	
	@RequestMapping("/groupBArrestReport")
	public GroupBArrestReport getGroupBReport(){
		return BaselineIncidentFactory.getBaselineGroupBArrestReport();
	}
	
	@RequestMapping(value="/arrestReports", method=RequestMethod.POST)
	public void saveArrestReport(@RequestBody GroupBArrestReport groupBArrestReport){
		arrestReportService.saveGroupBArrestReport(groupBArrestReport);
	}
	
	@RequestMapping(value="/arrestReports", method=RequestMethod.DELETE)
	public void deleteArrestReport(@RequestBody GroupBArrestReport groupBArrestReport){
		arrestReportService.deleteGroupBArrestReport(groupBArrestReport);
	}
	
}
