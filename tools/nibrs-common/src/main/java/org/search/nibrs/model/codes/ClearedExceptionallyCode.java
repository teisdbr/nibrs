package org.search.nibrs.model.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for exceptional clearance (data element 4)
 */
public enum ClearedExceptionallyCode {
	
	A("A", "Death of Offender"),
	B("B", "Prosecution Declined (by the prosecutor for other than lack of probable cause)"),
	C("C", "In Custody of Other Jurisdiction"),
	D("D", "Victim Refused to Cooperate (in the prosecution)"),
	E("E", "Juvenile/No Custody (the handling of a juvenile without taking him/her into custody, but rather by oral or written notice given to the parents or legal guardian in a case involving a minor offense, such as petty larceny)."),
	N("N", "Not Applicable (not cleared exceptionally).");
	
	public String code;
	public String description;
	
	private ClearedExceptionallyCode(String code, String description){
		this.code = code;
		this.description = description;
	}
	
	public static final Set<ClearedExceptionallyCode> asSet() {
		return EnumSet.allOf(ClearedExceptionallyCode.class);
	}
	
	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (ClearedExceptionallyCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}

}
