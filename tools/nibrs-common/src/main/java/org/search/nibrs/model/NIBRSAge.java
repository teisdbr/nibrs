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
package org.search.nibrs.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;

/**
 * The class of objects representing an expression of a person's age in NIBRS.  An age expression can be a non-numeric code (e.g., for newborns), a single
 * integer value, or a range of integer values.  If the age is a single value, the min and max will be equal.
 *
 */
public class NIBRSAge {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(NIBRSAge.class);
	
	private Integer ageMin;
	private Integer ageMax;
	private String nonNumericAge;
	private NIBRSError error;
	
	public NIBRSAge() {
	}
	
	public NIBRSAge(NIBRSAge a) {
		this.ageMin = a.ageMin;
		this.ageMax = a.ageMax;
		this.nonNumericAge = a.nonNumericAge;
		this.error = a.error == null ? null : new NIBRSError(a.error);
	}
	
	private NIBRSAge(String nonNumericAge) {
		this.nonNumericAge = nonNumericAge;
	}
	
	private NIBRSAge(Integer ageMin, Integer ageMax) {
		this.ageMin = ageMin;
		this.ageMax = ageMax;
	}
	
	public void setError(NIBRSError error) {
		this.error = error;
	}
	
	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}

	public void setAgeMax(Integer ageMax) {
		this.ageMax = ageMax;
	}
	
	public void setNonNumericAge(String nonNumericAge) {
		this.nonNumericAge = nonNumericAge;
	}

	public Integer getAgeMin() {
		return ageMin;
	}

	public Integer getAgeMax() {
		return ageMax;
	}
	
	public Integer getAverage() {
		Integer ret = null;
		if (!isNonNumeric()) {
			double min = getAgeMin().doubleValue();
			double max = getAgeMax().doubleValue();
			double average = (min + max) / 2.0;
			ret = new Integer((int) average);
		}
		return ret;
	}

	public String getNonNumericAge() {
		return nonNumericAge;
	}

	public NIBRSError getError() {
		return error;
	}
	
	public boolean isNonNumeric() {
		return error == null && nonNumericAge != null;
	}
	
	public boolean isUnknown() {
		return "00".equals(nonNumericAge);
	}
	
	/**
	 * 1–6 Days Old
	 * @return true if "NN" is the value
	 */
	public boolean isNeonate() {
		return "NN".equals(nonNumericAge);
	}
	
	/**
	 * 1-6 days old 
	 * @return true if "NB" is the value 
	 */
	public boolean isNewborn() {
		return "NB".equals(nonNumericAge);
	}
	
	/**
	 * 7–364 Days Old
	 * @return true if "BB" is the value 
	 */
	public boolean isBaby() {
		return "BB".equals(nonNumericAge);
	}
	
	public boolean isAgeRange() {
		boolean isAgeRange = false;
		if (error == null && !isNonNumeric()) {
			if (ageMin != null && ageMax != null) {
				isAgeRange = !ageMin.equals(ageMax);
			}
		}
		return isAgeRange;
	}
	
	public boolean isInvalid() {
		return error != null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ageMin == null) ? 0 : ageMin.hashCode());
		result = prime * result + ((ageMax == null) ? 0 : ageMax.hashCode());
		result = prime * result + ((nonNumericAge == null) ? 0 : nonNumericAge.hashCode());
		result = prime * result + ((error == null) ? 0 : error.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}
	
	@Override
	public String toString() {
		NIBRSError e = getError();
		if (e != null) {
			return "Invalid age: " + e.getValue();
		}
		return isNonNumeric() ? getNonNumericAge() : (isAgeRange() ? ageMin + "-" + ageMax : ageMin.toString());
	}

	public boolean isYoungerThan(NIBRSAge age, boolean lenientRange) {
		if (age == null) {
			throw new IllegalArgumentException("Cannot compare to null age");
		}
		int[] thisDays = convertAgeToDays();
		int[] thatDays = age.convertAgeToDays();
		int thatComp = lenientRange ? thatDays[1] : thatDays[0];
		int thisComp = lenientRange ? thisDays[0] : thisDays[1];
		return thisComp < thatComp;
	}

	public boolean isOlderThan(NIBRSAge age, boolean lenientRange) {
		if (age == null) {
			throw new IllegalArgumentException("Cannot compare to null age");
		}
		int[] thisDays = convertAgeToDays();
		int[] thatDays = age.convertAgeToDays();
		int thatComp = lenientRange ? thatDays[0] : thatDays[1];
		int thisComp = lenientRange ? thisDays[1] : thisDays[0];
		return thisComp > thatComp;
	}
	
	int[] convertAgeToDays() {
		int[] ret = new int[2];
		if (isNonNumeric()) {
			if ("NN".equals(nonNumericAge)) {
				ret[0] = 0;
				ret[1] = 0;
			} else if ("NB".equals(nonNumericAge)) {
				ret[0] = 1;
				ret[1] = 1;
			} else if ("BB".equals(nonNumericAge)) {
				ret[0] = 7;
				ret[1] = 7;
			}
		} else {
			ret[0] = ageMin*365;
			ret[1] = ageMax*365;
		}
		return ret;
	}
	
	// static factory methods
	
	public static final NIBRSAge getNeonateAge() {
		NIBRSAge ret = new NIBRSAge("NN");
		ret.ageMax = 0;
		ret.ageMin = 0;
		return ret;
	}

	public static final NIBRSAge getNewbornAge() {
		NIBRSAge ret = new NIBRSAge("NB");
		ret.ageMax = 0;
		ret.ageMin = 0;
		return ret;
	}

	public static final NIBRSAge getBabyAge() {
		NIBRSAge ret = new NIBRSAge("BB");
		ret.ageMax = 0;
		ret.ageMin = 0;
		return ret;
	}

	public static final NIBRSAge getUnknownAge() {
		return new NIBRSAge("00");
	}

	public static final NIBRSAge getAge(Integer ageMin, Integer ageMax) {
		if (ageMax == null) {
			ageMax = ageMin;
		}
		if (ageMin == 0) {
			return new NIBRSAge("00");
		}
		return new NIBRSAge(ageMin, ageMax);
	}

}