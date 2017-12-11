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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
public class AppProperties {

    private String multicastEndpoints = "direct:createErrorReport,direct:persistReport";
    private String nibrsFileFolderPath = "/tmp/nibrs";
    
	public String getMulticastEndpoints() {
		return multicastEndpoints;
	}

	public void setMulticastEndpoints(String multicastEndpoints) {
		this.multicastEndpoints = multicastEndpoints;
	}

	public String getNibrsFileFolderPath() {
		return nibrsFileFolderPath;
	}

	public void setNibrsFileFolderPath(String nibrsFileFolderPath) {
		this.nibrsFileFolderPath = nibrsFileFolderPath;
	}

}