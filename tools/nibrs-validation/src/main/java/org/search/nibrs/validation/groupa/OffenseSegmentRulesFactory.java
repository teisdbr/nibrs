package org.search.nibrs.validation.groupa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.search.nibrs.common.NIBRSError;
import org.search.nibrs.model.OffenseSegment;
import org.search.nibrs.model.codes.AutomaticWeaponIndicatorCode;
import org.search.nibrs.model.codes.BiasMotivationCode;
import org.search.nibrs.model.codes.LocationTypeCode;
import org.search.nibrs.model.codes.MethodOfEntryCode;
import org.search.nibrs.model.codes.NIBRSErrorCode;
import org.search.nibrs.model.codes.OffenderSuspectedOfUsingCode;
import org.search.nibrs.model.codes.OffenseAttemptedCompletedCode;
import org.search.nibrs.model.codes.OffenseCode;
import org.search.nibrs.model.codes.TypeOfCriminalActivityCode;
import org.search.nibrs.model.codes.TypeOfWeaponForceCode;
import org.search.nibrs.validation.rules.DuplicateCodedValueRule;
import org.search.nibrs.validation.rules.ExclusiveCodedValueRule;
import org.search.nibrs.validation.rules.NotAllBlankRule;
import org.search.nibrs.validation.rules.NotBlankRule;
import org.search.nibrs.validation.rules.NumericValueRule;
import org.search.nibrs.validation.rules.Rule;
import org.search.nibrs.validation.rules.ValidValueListRule;

public class OffenseSegmentRulesFactory {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(OffenseSegmentRulesFactory.class);
	
	private static final class Rule219 implements Rule<OffenseSegment> {
		
		private Set<String> activitySet1 = new HashSet<>();
		private Set<String> activitySet2 = new HashSet<>();
		private Set<String> activitySet3 = new HashSet<>();
		private Set<String> offenseSet1 = new HashSet<>();
		private Set<String> offenseSet2 = new HashSet<>();
		private Set<String> offenseSet3 = new HashSet<>();
		
		public Rule219() {
			
			activitySet1.addAll(Arrays.asList(new String[] {
				TypeOfCriminalActivityCode.B.code,
				TypeOfCriminalActivityCode.C.code,
				TypeOfCriminalActivityCode.D.code,
				TypeOfCriminalActivityCode.E.code,
				TypeOfCriminalActivityCode.O.code,
				TypeOfCriminalActivityCode.P.code,
				TypeOfCriminalActivityCode.T.code,
				TypeOfCriminalActivityCode.U.code
			}));
			
			offenseSet1.addAll(Arrays.asList(new String[] {
				OffenseCode._250.code,
				OffenseCode._280.code,
				OffenseCode._35A.code,
				OffenseCode._35B.code,
				OffenseCode._39C.code,
				OffenseCode._370.code,
				OffenseCode._520.code
			}));
			
			activitySet2.addAll(Arrays.asList(new String[] {
				TypeOfCriminalActivityCode.J.code,
				TypeOfCriminalActivityCode.G.code,
				TypeOfCriminalActivityCode.N.code
			}));
			
			offenseSet2.addAll(Arrays.asList(new String[] {
				OffenseCode._09B.code,
				OffenseCode._100.code,
				OffenseCode._11A.code,
				OffenseCode._11B.code,
				OffenseCode._11C.code,
				OffenseCode._11D.code,
				OffenseCode._120.code,
				OffenseCode._13A.code,
				OffenseCode._13B.code,
				OffenseCode._13C.code
			}));
			
			activitySet3.addAll(Arrays.asList(new String[] {
				TypeOfCriminalActivityCode.A.code,
				TypeOfCriminalActivityCode.F.code,
				TypeOfCriminalActivityCode.I.code,
				TypeOfCriminalActivityCode.S.code
			}));
			
			offenseSet3.addAll(Arrays.asList(new String[] {
					OffenseCode._720.code
			}));
			
		}

		@Override
		public NIBRSError apply(OffenseSegment subject) {
			NIBRSError ret = null;
			String offenseCode = subject.getUcrOffenseCode();
			for (String typeOfCriminalActivity : subject.getTypeOfCriminalActivity()) {
				if (typeOfCriminalActivity != null) {
					if (activitySet1.contains(typeOfCriminalActivity) && !offenseSet1.contains(offenseCode) ||
							activitySet2.contains(typeOfCriminalActivity) && !offenseSet2.contains(offenseCode) ||
							activitySet3.contains(typeOfCriminalActivity) && !offenseSet3.contains(offenseCode)) {
						ret = subject.getErrorTemplate();
						ret.setValue(typeOfCriminalActivity);
						ret.setDataElementIdentifier("12");
						ret.setNIBRSErrorCode(NIBRSErrorCode._219);
					}
				}
			}
			return ret;
		}

	}
	
	private List<Rule<OffenseSegment>> rulesList = new ArrayList<>();
	
	public OffenseSegmentRulesFactory() {
		
		rulesList.add(getRule201ForUCROffenseCode());
		rulesList.add(getRule201ForOffendersSuspectedOfUsing());
		rulesList.add(getRule201ForSingleStringProperty("offenseAttemptedCompleted", "7"));
		rulesList.add(getRule201ForSingleStringProperty("locationType", "9"));
		rulesList.add(getRule201ForStringArrayProperty("biasMotivation", "8A"));
		
		rulesList.add(getRule204ForValueList("biasMotivation", "8A", BiasMotivationCode.codeSet()));
		rulesList.add(getRule204ForValueList("locationType", "9", LocationTypeCode.codeSet()));
		rulesList.add(getRule204ForValueList("methodOfEntry", "11", MethodOfEntryCode.codeSet()));
		rulesList.add(getRule204ForValueList("typeOfCriminalActivity", "12", TypeOfCriminalActivityCode.codeSet()));
		rulesList.add(getRule204ForValueList("typeOfWeaponForceInvolved", "13", TypeOfWeaponForceCode.codeSet()));
		rulesList.add(getRule204ForValueList("automaticWeaponIndicator", "13", AutomaticWeaponIndicatorCode.codeSet()));
		rulesList.add(getRule204ForPremisesEntered());
		
		rulesList.add(getRule206("typeOfCriminalActivity", "12"));
		rulesList.add(getRule206("typeOfWeaponForceInvolved", "13"));
		rulesList.add(getRule206("offendersSuspectedOfUsing", "8"));
		rulesList.add(getRule206("biasMotivation", "8A"));
		
		rulesList.add(getRule207("typeOfCriminalActivity", "12", TypeOfCriminalActivityCode.noneOrUnknownValueCodeSet()));
		rulesList.add(getRule207("typeOfWeaponForceInvolved", "13", TypeOfWeaponForceCode.noneOrUnknownValueCodeSet()));
		rulesList.add(getRule207("offendersSuspectedOfUsing", "8", OffenderSuspectedOfUsingCode.noneOrUnknownValueCodeSet()));
		rulesList.add(getRule207("biasMotivation", "8A", BiasMotivationCode.noneOrUnknownValueCodeSet()));
		
		rulesList.add(getRule219());
		rulesList.add(getRule220());
		rulesList.add(getRule221());
		rulesList.add(getRule251());
		rulesList.add(getRule252());
		rulesList.add(getRule253());
		rulesList.add(getRule254());
		rulesList.add(getRule255());
		
	}
	
	Rule<OffenseSegment> getRule255() {
		return new ValidValueListRule<>("automaticWeaponIndicator", "13", OffenseSegment.class, NIBRSErrorCode._255, AutomaticWeaponIndicatorCode.codeSet());
	}
	
	Rule<OffenseSegment> getRule254() {
		return new Rule<OffenseSegment>() {
			@Override
			public NIBRSError apply(OffenseSegment subject) {
				NIBRSError ret = null;
				String offenseCode = subject.getUcrOffenseCode();
				if (subject.getMethodOfEntry() != null && offenseCode != null && !OffenseCode._220.code.equals(offenseCode)) {
					ret = subject.getErrorTemplate();
					ret.setValue(subject.getMethodOfEntry());
					ret.setDataElementIdentifier("11");
					ret.setNIBRSErrorCode(NIBRSErrorCode._254);
				}
				return ret;
			}
		};
	}
	
	Rule<OffenseSegment> getRule253() {
		return new NotBlankRule<OffenseSegment>("methodOfEntry", "11", OffenseSegment.class, NIBRSErrorCode._253) {
			@Override
			public boolean ignore(OffenseSegment o) {
				return o != null && !OffenseCode._220.code.equals(o.getUcrOffenseCode());
			}
		};
	}
	
	Rule<OffenseSegment> getRule252() {
		return new Rule<OffenseSegment>() {
			@Override
			public NIBRSError apply(OffenseSegment subject) {
				NIBRSError ret = null;
				if (subject.getNumberOfPremisesEntered() != null) {
					String offenseCode = subject.getUcrOffenseCode();
					String locationType = subject.getLocationType();
					if (!(OffenseCode._220.code.equals(offenseCode) && (LocationTypeCode._14.code.equals(locationType) || LocationTypeCode._19.code.equals(locationType)))) {
						ret = subject.getErrorTemplate();
						ret.setValue(subject.getNumberOfPremisesEntered());
						ret.setDataElementIdentifier("10");
						ret.setNIBRSErrorCode(NIBRSErrorCode._252);
					}
				}
				return ret;
			}
		};
	}
	
	Rule<OffenseSegment> getRule221() {
		return new NotAllBlankRule<OffenseSegment>("typeOfWeaponForceInvolved", "13", OffenseSegment.class, NIBRSErrorCode._221) {
			@Override
			public boolean ignore(OffenseSegment o) {
				Set<String> applicableOffenses = new HashSet<>();
				applicableOffenses.addAll(Arrays.asList(new String[] {
					OffenseCode._09A.code,
					OffenseCode._09B.code,
					OffenseCode._09C.code,
					OffenseCode._100.code,
					OffenseCode._11A.code,
					OffenseCode._11B.code,
					OffenseCode._11C.code,
					OffenseCode._11D.code,
					OffenseCode._120.code,
					OffenseCode._13A.code,
					OffenseCode._13B.code,
					OffenseCode._210.code,
					OffenseCode._520.code,
					OffenseCode._64A.code,
					OffenseCode._64B.code,
				}));
				String offenseCode = o.getUcrOffenseCode();
				return offenseCode == null || !applicableOffenses.contains(offenseCode);
			}
		};
	}
	
	Rule<OffenseSegment> getRule220() {
		return new NotAllBlankRule<OffenseSegment>("typeOfCriminalActivity", "12", OffenseSegment.class, NIBRSErrorCode._220) {
			@Override
			public boolean ignore(OffenseSegment o) {
				Set<String> applicableOffenses = new HashSet<>();
				applicableOffenses.addAll(Arrays.asList(new String[] {
					OffenseCode._250.code,
					OffenseCode._280.code,
					OffenseCode._35A.code,
					OffenseCode._35B.code,
					OffenseCode._39C.code,
					OffenseCode._370.code,
					OffenseCode._520.code,
					OffenseCode._720.code
				}));
				String offenseCode = o.getUcrOffenseCode();
				return offenseCode == null || !applicableOffenses.contains(offenseCode);
			}
		};
	}
	
	Rule<OffenseSegment> getRule219() {
		return new Rule219();
	}
	
	Rule<OffenseSegment> getRule207(String propertyName, String dataElementIdentifier, Set<String> exclusiveValueSet) {
		return new ExclusiveCodedValueRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._207, exclusiveValueSet);
	}
	
	Rule<OffenseSegment> getRule206(String propertyName, String dataElementIdentifier) {
		return new DuplicateCodedValueRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._206);
	}
	
	Rule<OffenseSegment> getRule201ForUCROffenseCode() {
		// weird that the 2.1 spec calls this a 201 error, not 204 like the rest of the "must be a valid value" ones, but there you go...
		return new ValidValueListRule<>("ucrOffenseCode", "6", OffenseSegment.class, NIBRSErrorCode._201, OffenseCode.codeSet(), false);
	}
	
	Rule<OffenseSegment> getRule201ForOffendersSuspectedOfUsing() {
		// weird that the 2.1 spec calls this a 201 error, not 204 like the rest of the "must be a valid value" ones, but there you go...
		return new ValidValueListRule<>("offendersSuspectedOfUsing", "8", OffenseSegment.class, NIBRSErrorCode._201, OffenderSuspectedOfUsingCode.codeSet(), false);
	}
	
	Rule<OffenseSegment> getRule201ForSingleStringProperty(String propertyName, String dataElementIdentifier) {
		return new NotBlankRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._201);
	}
	
	Rule<OffenseSegment> getRule201ForStringArrayProperty(String propertyName, String dataElementIdentifier) {
		return new NotAllBlankRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._201);
	}
	
	Rule<OffenseSegment> getRule204ForValueList(String propertyName, String dataElementIdentifier, Set<String> allowedValueSet) {
		return new ValidValueListRule<>(propertyName, dataElementIdentifier, OffenseSegment.class, NIBRSErrorCode._204, allowedValueSet);
	}
	
	Rule<OffenseSegment> getRule251() {
		return new ValidValueListRule<>("offenseAttemptedCompleted", "7", OffenseSegment.class, NIBRSErrorCode._251, OffenseAttemptedCompletedCode.codeSet(), true);
	}
	
	Rule<OffenseSegment> getRule204ForPremisesEntered() {
		Rule<OffenseSegment> ret = new NumericValueRule<>(
				subject -> {
					return subject.getNumberOfPremisesEntered();
				},
				(value, target) -> {
					NIBRSError e = null;
					if (value != null && (0 > value.intValue() || 99 < value.intValue())) {
						e = target.getErrorTemplate();
						e.setNIBRSErrorCode(NIBRSErrorCode._204);
						e.setDataElementIdentifier("10");
						e.setValue(value);
					}
					return e;
				});
		return ret;
	}
	
	/**
	 * Get the list of rules for the administrative segment.
	 * @return the list of rules
	 */
	public List<Rule<OffenseSegment>> getRulesList() {
		return Collections.unmodifiableList(rulesList);
	}

}
