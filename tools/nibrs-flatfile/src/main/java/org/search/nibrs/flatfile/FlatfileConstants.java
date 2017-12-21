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
package org.search.nibrs.flatfile;

public interface FlatfileConstants {
	
	public static final int ADMIN_WITH_CARGO_THEFT_SEGMENT_LENGTH = 88;
	public static final int ADMIN_WITHOUT_CARGO_THEFT_SEGMENT_LENGTH = 87;
	public static final int ZERO_REPORT_SEGMENT_LENGTH = 43;
    public static final int GROUP_A_ARRESTEE_SEGMENT_LENGTH = 110;
    public static final int GROUP_B_ARRESTEE_SEGMENT_LENGTH = 66;
    public static final int OFFENSE_SEGMENT_LENGTH = 71;
	public static final int OFFENDER_WITH_ETHNICITY_SEGMENT_LENGTH = 46;
    public static final int PROPERTY_SEGMENT_LENGTH = 307;
	public static final int VICTIM_WITH_LEOKA_SEGMENT_LENGTH = 141;
	public static final int VICTIM_WITHOUT_LEOKA_SEGMENT_LENGTH = 129;

}
