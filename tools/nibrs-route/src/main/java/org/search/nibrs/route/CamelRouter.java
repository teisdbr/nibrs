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
package org.search.nibrs.route;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Camel routes picks up the flatfile from the input folder, drop the error report file into the 
 * result folder and persist the good incident reports by calling the rest service
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class CamelRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        fromF("file:%s/input?idempotent=true&moveFailed=%s/error&move=processed/", "/tmp/nibrs", "/tmp/nibrs").routeId("validate")
        		.transform().method("flatFileValidator", "validate")
        		.multicast().to("direct:createErrorReport", "direct:persistReport")
                .end();
//                .to("stream:out")
//                .toF("file:%s/result", "/tmp/nibrs");
        
        from("direct:createErrorReport").routeId("createErrorReport")
        	.transform().method("flatFileValidator", "createErrorReport")
        	.end();
        
        from("direct:persistReport").routeId("persistReport")
        	.transform().method("stagingDataRestClient", "persistIncidentReports")
        	.end(); 
    }

}