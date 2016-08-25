package org.search.nibrs.model.codes;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Code enum for type of criminal activity / gang info (data element 12)
 */
public enum TypeOfCriminalActivityCode {
	
	A("A", "Simple/Gross Neglect (unintentionally, intentionally, or knowingly failing to provide food, water, shelter, veterinary care, hoarding, etc.)"),
	B("B", "Buying/Receiving"),
	C("C", "Cultivating/Manufacturing/Publishing (i.e., production of any type)"),
	D("D", "Distributing/Selling"),
	F("F", "Organized Abuse (Dog Fighting and Cock Fighting)"),
	E("E", "Exploiting Children"),
	I("I", "Intentional Abuse and Torture (tormenting, mutilating, maiming, poisoning, or abandonment)"),
	O("O", "Operating/Promoting/Assisting"),
	P("P", "Possessing/Concealing"),
	S("S", "Animal Sexual Abuse (Bestiality)"),
	T("T", "Transporting/Transmitting/Importing"),
	U("U", "Using/Consuming"),
	J("J", "Juvenile Gang"),
	G("G", "Other Gang"),
	N("N", "None/Unknown");
		
	private TypeOfCriminalActivityCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String code;
	public String description;
	
	public static final Set<TypeOfCriminalActivityCode> asSet() {
		return EnumSet.allOf(TypeOfCriminalActivityCode.class);
	}

	public static final Set<String> codeSet() {
		Set<String> ret = new HashSet<>();
		for (TypeOfCriminalActivityCode v : values()) {
			ret.add(v.code);
		}
		return ret;
	}
	
	public static final Set<String> noneOrUnknownValueCodeSet() {
		return Collections.singleton(N.code);
	}

}
