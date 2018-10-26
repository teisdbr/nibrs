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
package org.search.nibrs.fbi.service;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Camel routes picks up the 
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class CamelRouter extends RouteBuilder {
	
	@Autowired
	private AppProperties appProperties;
	
    @Override
    public void configure() throws Exception {
        fromF("file:%s/input?idempotent=true&moveFailed=%s/error&move=processed/", 
        		appProperties.getNibrsNiemDocumentRequestFolder(), appProperties.getNibrsNiemDocumentRequestFolder()).routeId("niemDocumentFileInput")
        		.to("direct:submitNiemDocument")
                .end();
        
        from("direct:submitNiemDocument").routeId("callFBINibrsNiemService")
        	.log(LoggingLevel.DEBUG, "Before calling the FBI service")
        	.to("https4://" + appProperties.getNibrsNiemServiceEndpointUrl())
        	.log(LoggingLevel.DEBUG, "After calling the FBI service")
        	.end();
        
    }

}