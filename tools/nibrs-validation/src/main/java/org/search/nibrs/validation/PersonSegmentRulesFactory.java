package org.search.nibrs.validation;

import java.util.Set;

import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.AbstractPersonSegment;
import org.search.nibrs.model.NIBRSAge;
import org.search.nibrs.model.VictimSegment;
import org.search.nibrs.model.codes.EthnicityCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.RaceCode;
import org.search.nibrs.model.codes.ResidentStatusCode;
import org.search.nibrs.model.codes.SexCode;
import org.search.nibrs.validation.rules.AbstractBeanPropertyRule;
import org.search.nibrs.validation.rules.NullObjectRule;
import org.search.nibrs.validation.rules.Rule;

public class PersonSegmentRulesFactory<T extends AbstractPersonSegment> {
	
	private Class<T> clazz;
	
	public <S extends AbstractPersonSegment> PersonSegmentRulesFactory(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	private final class PersonVictimValidValueRule<S extends T> extends AbstractBeanPropertyRule<T> {
		
		private Set<String> allowedValueSet;
		private boolean allowNull;

		public PersonVictimValidValueRule(String propertyName, String dataElementIdentifier, NIBRSErrorCode errorCode, Set<String> allowedValueSet, boolean allowNull) {
			super(propertyName, dataElementIdentifier, clazz, errorCode);
			this.allowedValueSet = allowedValueSet;
			this.allowNull = allowNull;
		}

		@Override
		protected boolean propertyViolatesRule(Object value, AbstractPersonSegment subject) {
			return (subject.isPerson() && ((!allowNull && value == null) || (value != null && !allowedValueSet.contains(value))));
		}

	}

	public Rule<T> getAgeValidNonBlankRule() {
		// note:  because we parse the age string when constructing the nibrsAge property of VictimSegment, it is not possible
		// to have a separate violation of rule 404 for victim age.
		return new NullObjectRule<T>();
	}

	public Rule<T> getSexValidNonBlankRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		return new PersonVictimValidValueRule<T>("sex", dataElementIdentifier, nibrsErrorCode, SexCode.codeSet(), false);
	}

	public Rule<T> getRaceValidNonBlankRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		return new PersonVictimValidValueRule<T>("race", dataElementIdentifier, nibrsErrorCode, RaceCode.codeSet(), false);
	}

	public Rule<T> getResidentStatusValidNonBlankRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode, boolean allowNull) {
		return new PersonVictimValidValueRule<T>("residentStatusOfVictim", dataElementIdentifier, nibrsErrorCode, ResidentStatusCode.codeSet(), allowNull);
	}

	public Rule<T> getEthnicityValidNonBlankRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode, boolean allowNull) {
		return new PersonVictimValidValueRule<T>("ethnicity", dataElementIdentifier, nibrsErrorCode, EthnicityCode.codeSet(), allowNull);
	}
	
	public Rule<T> getProperAgeRangeRule(String dataElementIdentifier, NIBRSErrorCode nibrsErrorCode) {
		return new Rule<T>() {
			@Override
			public NIBRSError apply(T segment) {
				NIBRSError e = null;
				NIBRSAge nibrsAge = segment.getAge();
				if (nibrsAge != null && nibrsAge.isAgeRange()) {

					Integer ageMin = nibrsAge.getAgeMin();
					Integer ageMax = nibrsAge.getAgeMax();

					if (ageMin > ageMax) {
						e = segment.getErrorTemplate();
						e.setDataElementIdentifier(dataElementIdentifier);
						e.setNIBRSErrorCode(nibrsErrorCode);
						e.setValue(nibrsAge);
					}
				}
				return e;
			}
		};
	}
	
}
