package org.search.nibrs.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.codes.NIBRSErrorCode;

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
	private String ageString;
	
	public NIBRSAge() {
	}
	
	public NIBRSAge(NIBRSAge a) {
		this.ageMin = a.ageMin;
		this.ageMax = a.ageMax;
		this.nonNumericAge = a.nonNumericAge;
		this.error = a.error == null ? null : new NIBRSError(a.error);
		this.ageString = a.ageString;
	}
	
	String getAgeString() {
		return ageString;
	}
	
	void setError(NIBRSError error) {
		this.error = error;
	}
	
	void setAgeString(String ageString, char segmentContext) {
		if (ageString != null) {
			String ageStringTrim = ageString.trim();
			if (ageStringTrim.length() == 4) {
				try {
					ageMin = Integer.parseInt(ageStringTrim.substring(0, 2));
				} catch (NumberFormatException nfe) {
					error = new NIBRSError();
					error.setValue(ageString);
					error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentContext + "04"));
				}
				try {
					ageMax = Integer.parseInt(ageStringTrim.substring(2, 4));
				} catch (NumberFormatException nfe) {
					error = new NIBRSError();
					error.setValue(ageString);
					error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentContext + "09"));
				}
			} else {
				if ("NN".equals(ageStringTrim) || "NB".equals(ageStringTrim) || "BB".equals(ageStringTrim) || "00".equals(ageStringTrim)) {
					nonNumericAge = ageStringTrim;
				} else {
					try {
						ageMin = Integer.parseInt(ageStringTrim.substring(0, 2));
						ageMax = ageMin;
					} catch (NumberFormatException nfe) {
						error = new NIBRSError();
						error.setValue(ageString);
						error.setNIBRSErrorCode(NIBRSErrorCode.valueOf("_" + segmentContext + "04"));
					}
				}
			}
		}
	}

	public Integer getAgeMin() {
		return ageMin;
	}

	public Integer getAgeMax() {
		return ageMax;
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
	
	public boolean isAgeRange() {
		return error == null && !isNonNumeric() && !ageMin.equals(ageMax);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ageString == null) ? 0 : ageString.hashCode());
		//LOG.info("hashCode=" + result);
		result = prime * result + ((error == null) ? 0 : error.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}
	
	@Override
	public String toString() {
		return isNonNumeric() ? getNonNumericAge() : (isAgeRange() ? ageMin + "-" + ageMax : ageMin.toString());
	}

}