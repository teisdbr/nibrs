package org.search.nibrs.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Representation of a VictimSegment reported within an Incident in a NIBRS report. Note that we extend AbstractPersonSegment even though some types of victims are not people (e.g., Business)...since
 * NIBRS represents them all with the same data structure.
 *
 */
public class VictimSegment extends AbstractPersonSegment {

	public static final char VICTIM_SEGMENT_TYPE_IDENTIFIER = '4';
	public static final int UCR_OFFENSE_CODE_CONNECTION_COUNT = 10;
	public static final int AGGRAVATED_ASSAULT_HOMICIDE_CIRCUMSTANCES_COUNT = 2;
	public static final int TYPE_OF_INJURY_COUNT = 5;
	public static final int OFFENDER_NUMBER_RELATED_COUNT = 10;

	private Integer victimSequenceNumber;
	private String[] ucrOffenseCodeConnection;
	private String typeOfVictim;
	private String residentStatusOfVictim;
	private String[] aggravatedAssaultHomicideCircumstances;
	private String additionalJustifiableHomicideCircumstances;
	private String[] typeOfInjury;
	private Integer[] offenderNumberRelated;
	private String[] victimOffenderRelationship;
	private String typeOfOfficerActivityCircumstance;
	private String officerAssignmentType;
	private String officerOtherJurisdictionORI;
	private int populatedAggravatedAssaultHomicideCircumstancesCount;
	private int populatedTypeOfInjuryCount;
	private int populatedUcrOffenseCodeConnectionCount;
	private int populatedOffenderNumberRelatedCount;

	public VictimSegment() {
		super();
		initUcrOffenseCodeConnection();
		initAggravatedAssaultHomicideCircumstances();
		initTypeOfInjury();
		initOffenderNumberRelated();
		initVictimOffenderRelationship();
		segmentType = VICTIM_SEGMENT_TYPE_IDENTIFIER;
	}

	public VictimSegment(VictimSegment v) {
		super(v);
		victimSequenceNumber = v.victimSequenceNumber;
		typeOfVictim = v.typeOfVictim;
		residentStatusOfVictim = v.residentStatusOfVictim;
		additionalJustifiableHomicideCircumstances = v.additionalJustifiableHomicideCircumstances;
		typeOfOfficerActivityCircumstance = v.typeOfOfficerActivityCircumstance;
		officerAssignmentType = v.officerAssignmentType;
		officerOtherJurisdictionORI = v.officerOtherJurisdictionORI;
		populatedAggravatedAssaultHomicideCircumstancesCount = v.populatedAggravatedAssaultHomicideCircumstancesCount;
		populatedTypeOfInjuryCount = v.populatedTypeOfInjuryCount;
		populatedUcrOffenseCodeConnectionCount = v.populatedUcrOffenseCodeConnectionCount;
		populatedOffenderNumberRelatedCount = v.populatedOffenderNumberRelatedCount;
		ucrOffenseCodeConnection = CopyUtils.copyArray(v.ucrOffenseCodeConnection);
		aggravatedAssaultHomicideCircumstances = CopyUtils.copyArray(v.aggravatedAssaultHomicideCircumstances);
		typeOfInjury = CopyUtils.copyArray(v.typeOfInjury);
		offenderNumberRelated = CopyUtils.copyArray(v.offenderNumberRelated);
		victimOffenderRelationship = CopyUtils.copyArray(v.victimOffenderRelationship);
		segmentType = VICTIM_SEGMENT_TYPE_IDENTIFIER;
	}

	public String getTypeOfOfficerActivityCircumstance() {
		return typeOfOfficerActivityCircumstance;
	}

	public void setTypeOfOfficerActivityCircumstance(String typeOfOfficerActivityCircumstance) {
		this.typeOfOfficerActivityCircumstance = typeOfOfficerActivityCircumstance;
	}

	public String getOfficerAssignmentType() {
		return officerAssignmentType;
	}

	public void setOfficerAssignmentType(String officerAssignmentType) {
		this.officerAssignmentType = officerAssignmentType;
	}

	public String getOfficerOtherJurisdictionORI() {
		return officerOtherJurisdictionORI;
	}

	public void setOfficerOtherJurisdictionORI(String officerOtherJurisdictionORI) {
		this.officerOtherJurisdictionORI = officerOtherJurisdictionORI;
	}

	public String getVictimOffenderRelationship(int position) {
		return victimOffenderRelationship[position];
	}

	public List<String> getVictimOffenderRelationshipList() {
		return Collections.unmodifiableList(Arrays.asList(victimOffenderRelationship));
	}

	public void setVictimOffenderRelationship(int position, String value) {
		victimOffenderRelationship[position] = value;
	}

	public Integer getOffenderNumberRelated(int position) {
		return offenderNumberRelated[position];
	}

	public List<Integer> getOffenderNumberRelatedList() {
		return Collections.unmodifiableList(Arrays.asList(offenderNumberRelated));
	}

	public void setOffenderNumberRelated(int position, Integer value) {
		offenderNumberRelated[position] = value;
		populatedOffenderNumberRelatedCount = Math.max(populatedOffenderNumberRelatedCount, position + 1);
	}

	public int getPopulatedUcrOffenseCodeConnectionCount() {
		return populatedUcrOffenseCodeConnectionCount;
	}

	public int getPopulatedOffenderNumberRelatedCount() {
		return populatedOffenderNumberRelatedCount;
	}

	public String getTypeOfInjury(int position) {
		return typeOfInjury[position];
	}

	public List<String> getTypeOfInjuryList() {
		return Collections.unmodifiableList(Arrays.asList(typeOfInjury));
	}

	public void setTypeOfInjury(int position, String value) {
		typeOfInjury[position] = value;
		populatedTypeOfInjuryCount = Math.max(populatedTypeOfInjuryCount, position + 1);
	}

	public String getAggravatedAssaultHomicideCircumstances(int position) {
		return aggravatedAssaultHomicideCircumstances[position];
	}

	public List<String> getAggravatedAssaultHomicideCircumstancesList() {

		List<String> aggravatedAssaultHomicideCircumstancesList = new ArrayList<String>();

		for (int i = 0; i < populatedAggravatedAssaultHomicideCircumstancesCount; i++) {

			String aggAssaultCirc = aggravatedAssaultHomicideCircumstances[i];

			aggravatedAssaultHomicideCircumstancesList.add(aggAssaultCirc);
		}
		return Collections.unmodifiableList(aggravatedAssaultHomicideCircumstancesList);
	}

	public void setAggravatedAssaultHomicideCircumstances(int position, String value) {
		aggravatedAssaultHomicideCircumstances[position] = value;
		populatedAggravatedAssaultHomicideCircumstancesCount = Math.max(populatedAggravatedAssaultHomicideCircumstancesCount, position + 1);
	}

	public int getPopulatedAggravatedAssaultHomicideCircumstancesCount() {
		return populatedAggravatedAssaultHomicideCircumstancesCount;
	}

	public int getPopulatedTypeOfInjuryCount() {
		return populatedTypeOfInjuryCount;
	}

	public String getUcrOffenseCodeConnection(int position) {
		return ucrOffenseCodeConnection[position];
	}

	public List<String> getUcrOffenseCodeList() {
		return Collections.unmodifiableList(Arrays.asList(ucrOffenseCodeConnection));
	}

	public void setUcrOffenseCodeConnection(int position, String value) {
		ucrOffenseCodeConnection[position] = value;
		populatedUcrOffenseCodeConnectionCount = Math.max(populatedUcrOffenseCodeConnectionCount, position + 1);
	}

	public String getAdditionalJustifiableHomicideCircumstances() {
		return additionalJustifiableHomicideCircumstances;
	}

	public void setAdditionalJustifiableHomicideCircumstances(String additionalJustifiableHomicideCircumstances) {
		this.additionalJustifiableHomicideCircumstances = additionalJustifiableHomicideCircumstances;
	}

	public String getResidentStatusOfVictim() {
		return residentStatusOfVictim;
	}

	public void setResidentStatusOfVictim(String residentStatusOfVictim) {
		this.residentStatusOfVictim = residentStatusOfVictim;
	}

	public String getTypeOfVictim() {
		return typeOfVictim;
	}

	public void setTypeOfVictim(String typeOfVictim) {
		this.typeOfVictim = typeOfVictim;
	}

	public Integer getVictimSequenceNumber() {
		return victimSequenceNumber;
	}

	public void setVictimSequenceNumber(Integer victimSequenceNumber) {
		this.victimSequenceNumber = victimSequenceNumber;
	}

	@Override
	public boolean isPerson() {
		return "I".equals(typeOfVictim) || "L".equals(typeOfVictim);
	}

	public boolean isLawEnforcementOfficer() {
		return "L".equals(typeOfVictim);
	}

	public String[] getUcrOffenseCodeConnection() {
		return ucrOffenseCodeConnection;
	}

	public void setUcrOffenseCodeConnection(String[] ucrOffenseCodeConnection) {
		if (ucrOffenseCodeConnection == null) {
			initUcrOffenseCodeConnection();
		} else {
			this.ucrOffenseCodeConnection = ucrOffenseCodeConnection;
		}
	}

	public String[] getAggravatedAssaultHomicideCircumstances() {
		return aggravatedAssaultHomicideCircumstances;
	}

	public void setAggravatedAssaultHomicideCircumstances(String[] aggravatedAssaultHomicideCircumstances) {
		if (aggravatedAssaultHomicideCircumstances == null) {
			initAggravatedAssaultHomicideCircumstances();
		} else {
			this.aggravatedAssaultHomicideCircumstances = aggravatedAssaultHomicideCircumstances;
		}
	}

	public Integer[] getOffenderNumberRelated() {
		return offenderNumberRelated;
	}

	public void setOffenderNumberRelated(Integer[] offenderNumberRelated) {
		if (offenderNumberRelated == null) {
			initOffenderNumberRelated();
		} else {
			this.offenderNumberRelated = offenderNumberRelated;
		}
	}

	public String[] getTypeOfInjury() {
		return typeOfInjury;
	}

	public void setTypeOfInjury(String[] typeOfInjury) {
		if (typeOfInjury == null) {
			initTypeOfInjury();
		} else {
			this.typeOfInjury = typeOfInjury;
		}
	}

	public String[] getVictimOffenderRelationship() {
		return victimOffenderRelationship;
	}

	public void setVictimOffenderRelationship(String[] victimOffenderRelationship) {
		if (victimOffenderRelationship == null) {
			initVictimOffenderRelationship();
		} else {
			this.victimOffenderRelationship = victimOffenderRelationship;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((additionalJustifiableHomicideCircumstances == null) ? 0 : additionalJustifiableHomicideCircumstances.hashCode());
		result = prime * result + Arrays.hashCode(aggravatedAssaultHomicideCircumstances);
		result = prime * result + Arrays.hashCode(offenderNumberRelated);
		result = prime * result + ((officerAssignmentType == null) ? 0 : officerAssignmentType.hashCode());
		result = prime * result + ((officerOtherJurisdictionORI == null) ? 0 : officerOtherJurisdictionORI.hashCode());
		result = prime * result + populatedAggravatedAssaultHomicideCircumstancesCount;
		result = prime * result + populatedOffenderNumberRelatedCount;
		result = prime * result + populatedTypeOfInjuryCount;
		result = prime * result + populatedUcrOffenseCodeConnectionCount;
		result = prime * result + ((residentStatusOfVictim == null) ? 0 : residentStatusOfVictim.hashCode());
		result = prime * result + Arrays.hashCode(typeOfInjury);
		result = prime * result + ((typeOfOfficerActivityCircumstance == null) ? 0 : typeOfOfficerActivityCircumstance.hashCode());
		result = prime * result + ((typeOfVictim == null) ? 0 : typeOfVictim.hashCode());
		result = prime * result + Arrays.hashCode(ucrOffenseCodeConnection);
		result = prime * result + Arrays.hashCode(victimOffenderRelationship);
		result = prime * result + ((victimSequenceNumber == null) ? 0 : victimSequenceNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}

	@Override
	public String toString() {
		return "VictimSegment [victimSequenceNumber=" + victimSequenceNumber + ", ucrOffenseCodeConnection=" + Arrays.toString(ucrOffenseCodeConnection) + ", typeOfVictim=" + typeOfVictim
				+ ", residentStatusOfVictim=" + residentStatusOfVictim + ", aggravatedAssaultHomicideCircumstances=" + Arrays.toString(aggravatedAssaultHomicideCircumstances)
				+ ", additionalJustifiableHomicideCircumstances=" + additionalJustifiableHomicideCircumstances + ", typeOfInjury=" + Arrays.toString(typeOfInjury) + ", offenderNumberRelated="
				+ Arrays.toString(offenderNumberRelated) + ", victimOffenderRelationship=" + Arrays.toString(victimOffenderRelationship) + ", typeOfOfficerActivityCircumstance="
				+ typeOfOfficerActivityCircumstance + ", officerAssignmentType=" + officerAssignmentType + ", officerOtherJurisdictionORI=" + officerOtherJurisdictionORI
				+ ", populatedAggravatedAssaultHomicideCircumstancesCount=" + populatedAggravatedAssaultHomicideCircumstancesCount + ", populatedTypeOfInjuryCount=" + populatedTypeOfInjuryCount
				+ ", populatedUcrOffenseCodeConnectionCount=" + populatedUcrOffenseCodeConnectionCount + ", populatedOffenderNumberRelatedCount=" + populatedOffenderNumberRelatedCount + "]";
	}

	@Override
	protected Object getWithinSegmentIdentifier() {
		return victimSequenceNumber;
	}

	private void initVictimOffenderRelationship() {
		victimOffenderRelationship = new String[OFFENDER_NUMBER_RELATED_COUNT];
	}

	private void initOffenderNumberRelated() {
		offenderNumberRelated = new Integer[OFFENDER_NUMBER_RELATED_COUNT];
	}

	private void initTypeOfInjury() {
		typeOfInjury = new String[TYPE_OF_INJURY_COUNT];
	}

	private void initAggravatedAssaultHomicideCircumstances() {
		aggravatedAssaultHomicideCircumstances = new String[AGGRAVATED_ASSAULT_HOMICIDE_CIRCUMSTANCES_COUNT];
	}

	private void initUcrOffenseCodeConnection() {
		ucrOffenseCodeConnection = new String[UCR_OFFENSE_CODE_CONNECTION_COUNT];
	}

}
