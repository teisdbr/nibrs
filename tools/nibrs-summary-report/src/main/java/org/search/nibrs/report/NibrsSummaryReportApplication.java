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
package org.search.nibrs.report;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.nibrs.model.reports.ReturnAForm;
import org.search.nibrs.report.service.ExcelExporter;
import org.search.nibrs.report.service.StagingDataRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NibrsSummaryReportApplication implements CommandLineRunner{
	public static final Log log = LogFactory.getLog(NibrsSummaryReportApplication.class);
	@Autowired 
	public StagingDataRestClient restClient; 
	@Autowired 
	public ExcelExporter excelExporter;
	public static ConfigurableApplicationContext context;
	
	public static void main(String[] args) {
		SpringApplication.run(NibrsSummaryReportApplication.class, args).close();
	}
	
    @Override
    public void run(String... args) throws Exception {

    	if (args.length < 3){
    		log.error("Missing one or more required arguments ORI, Year or Month");
    		System.exit(0);; 
    	}
    	
        for (String arg: args){
        	System.out.println("arg: " + arg);
        }
        
        ReturnAForm returnAForm = restClient.getReturnAForm(args[0], args[1], args[2]);
        System.out.println("returnAForm: \n" + returnAForm);
        excelExporter.exportReturnAForm(returnAForm);

    }
}
