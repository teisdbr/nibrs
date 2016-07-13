package org.search.nibrs.model;

import org.search.nibrs.common.NIBRSError;

/**
 * The class of objects representing an expression of a person's age in NIBRS.  An age expression can be a non-numeric code (e.g., for newborns), a single
 * integer value, or a range of integer values.  If the age is a single value, the min and max will be equal.
 *
 */
public class NIBRSAge {

	private Integer ageMin;
	private Integer ageMax;
	private String nonNumericAge;
	private NIBRSError error;

	public void setAgeString(String ageString) {
		if (ageString != null) {
			String ageStringTrim = ageString.trim();
			if (ageStringTrim.length() == 4) {
				try {
					ageMin = Integer.parseInt(ageStringTrim.substring(0, 2));
				} catch (NumberFormatException nfe) {
					error = new NIBRSError();
					error.setValue(ageString);
					error.setRuleDescription("Invalid age string");
				}
				try {
					ageMax = Integer.parseInt(ageStringTrim.substring(2, 4));
				} catch (NumberFormatException nfe) {
					error = new NIBRSError();
					error.setValue(ageString);
					error.setRuleDescription("Invalid age string");
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
						error.setRuleDescription("Invalid age string");
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

}