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
package org.search.nibrs.importer;

import java.util.List;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.*;

/**
 * Interface for objects that are interested in processing new Incidents as they are built from a NIBRS report stream.
 *
 */
public interface ReportListener {

    /**
     * Called on implementations when the builder creates a new AbstractReport from the report stream.
     * @param report the new AbstractReport object
     * @param errorList any errors encountered during the creation of the Report
     */
    public void newReport(AbstractReport report, List<NIBRSError> errorList);

}
