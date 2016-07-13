package org.search.nibrs.model;

import java.io.Serializable;

public abstract class Person implements Serializable {

	private static final long serialVersionUID = -6892593914779656925L;
	
	private NIBRSAge age;
	private String sex;
	private String race;
	private String ethnicity;

	public NIBRSAge getAge() {
	    return age;
	}

	public void setAgeString(String ageString) {
	    age = new NIBRSAge();
	    age.setAgeString(ageString);
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

}