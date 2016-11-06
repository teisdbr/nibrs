package org.search.nibrs.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract class of objects that represent People in NIBRS (offenders, victims, and arrestees).
 *
 */
public abstract class AbstractPersonSegment extends AbstractSegment {

	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AbstractPersonSegment.class);
	
	private NIBRSAge age;
	private String sex;
	private String race;
	private String ethnicity;

	public AbstractPersonSegment() {
		super();
	}
	
	public AbstractPersonSegment(AbstractPersonSegment p) {
		super(p);
		age = p.age ==  null ? null : new NIBRSAge(p.age);
		sex = p.sex;
		race = p.race;
		ethnicity = p.ethnicity;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((ethnicity == null) ? 0 : ethnicity.hashCode());
		result = prime * result + ((race == null) ? 0 : race.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}

	public NIBRSAge getAge() {
	    return age;
	}

	public void setAgeString(String ageString) {
		if (ageString != null && ageString.trim().length() != 0) {
			age = new NIBRSAge();
			age.setAgeString(ageString, segmentType);
		}
	}

	public String getEthnicity() {
	    return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
	    this.ethnicity = ethnicity;
	}

	public String getRace() {
	    return race;
	}

	public void setRace(String race) {
	    this.race = race;
	}

	public String getSex() {
	    return sex;
	}

	public void setSex(String sex) {
	    this.sex = sex;
	}

	@Override
	public String toString() {
		return "age=" + age + ", sex=" + sex + ", race=" + race + ", ethnicity=" + ethnicity;
	}

}