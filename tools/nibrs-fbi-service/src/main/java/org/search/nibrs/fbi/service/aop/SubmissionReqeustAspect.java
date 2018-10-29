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
package org.search.nibrs.fbi.service.aop;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.search.nibrs.fbi.service.AppProperties;
import org.search.nibrs.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

@Aspect
@Component
public class SubmissionReqeustAspect {
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private AppProperties appProperties;

	@Around("execution(* org.search.nibrs.fbi.service.service.SubmissionRequestProcessor.*(..))")
    public void processSubmissionRequest(ProceedingJoinPoint joinPoint) throws Exception {
        //Advice
		
		Document requestDocument = (Document)joinPoint.getArgs()[0];
		log.info("Aspect request: " + XmlUtils.nodeToString(requestDocument)); 
    	log.info(" Check for SubmissionRequestProcessor ");

    	String incidentIdentifier = XmlUtils.xPathStringSearch(requestDocument, "(nibrs:Submission/nibrs:Report/nc:Incident/nc:ActivityIdentification/nc:IdentificationID)"
    			+ "|(nibrs:Submission/nibrs:Report[not(nc:Incident)]/j:Arrest/nc:ActivityIdentification/nc:IdentificationID)");
    	log.info("Incident Identifier: " + incidentIdentifier);

    	
    	Exchange exchange = (Exchange)joinPoint.getArgs()[1];
    	log.info("Aspect exchange messageID: " + exchange.getIn().getMessageId()); 
    	log.info("Aspect exchange Camel File Name: " + exchange.getIn().getHeader("CamelFileName")); 
    	
    	try {
    		log.info(" Allowed execution for " + joinPoint);
			Document returnedDocument = (Document) joinPoint.proceed();
			log.info("Aspect result: " + XmlUtils.nodeToString(returnedDocument)); 
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
}