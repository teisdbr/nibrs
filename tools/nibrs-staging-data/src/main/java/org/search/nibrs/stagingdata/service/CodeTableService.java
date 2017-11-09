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
package org.search.nibrs.stagingdata.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.search.nibrs.stagingdata.model.DateType;
import org.search.nibrs.stagingdata.repository.AdditionalJustifiableHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.AgencyRepository;
import org.search.nibrs.stagingdata.repository.AggravatedAssaultHomicideCircumstancesTypeRepository;
import org.search.nibrs.stagingdata.repository.ArresteeWasArmedWithTypeRepository;
import org.search.nibrs.stagingdata.repository.BiasMotivationTypeRepository;
import org.search.nibrs.stagingdata.repository.ClearedExceptionallyTypeRepository;
import org.search.nibrs.stagingdata.repository.DateTypeRepository;
import org.search.nibrs.stagingdata.repository.DispositionOfArresteeUnder18TypeRepository;
import org.search.nibrs.stagingdata.repository.EthnicityOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.LocationTypeRepository;
import org.search.nibrs.stagingdata.repository.MethodOfEntryTypeRepository;
import org.search.nibrs.stagingdata.repository.MultipleArresteeSegmentsIndicatorTypeRepository;
import org.search.nibrs.stagingdata.repository.OffenderSuspectedOfUsingTypeRepository;
import org.search.nibrs.stagingdata.repository.OfficerActivityCircumstanceTypeRepository;
import org.search.nibrs.stagingdata.repository.OfficerAssignmentTypeTypeRepository;
import org.search.nibrs.stagingdata.repository.PropertyDescriptionTypeRepository;
import org.search.nibrs.stagingdata.repository.RaceOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.ResidentStatusOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.SegmentActionTypeRepository;
import org.search.nibrs.stagingdata.repository.SexOfPersonTypeRepository;
import org.search.nibrs.stagingdata.repository.SuspectedDrugTypeTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeDrugMeasurementTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeInjuryTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfArrestTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfCriminalActivityTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfVictimTypeRepository;
import org.search.nibrs.stagingdata.repository.TypeOfWeaponForceInvolvedTypeRepository;
import org.search.nibrs.stagingdata.repository.TypePropertyLossEtcTypeRepository;
import org.search.nibrs.stagingdata.repository.UcrOffenseCodeTypeRepository;
import org.search.nibrs.stagingdata.repository.VictimOffenderRelationshipTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to process Group B Arrest Report.  
 *
 */
@Service
public class CodeTableService {
	SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");

	@Autowired
	public DateTypeRepository dateTypeRepository; 
	@Autowired
	public AgencyRepository agencyRepository; 
	@Autowired
	public SegmentActionTypeRepository segmentActionTypeRepository; 
	@Autowired
	public ClearedExceptionallyTypeRepository clearedExceptionallyTypeRepository; 
	@Autowired
	public UcrOffenseCodeTypeRepository ucrOffenseCodeTypeRepository; 
	@Autowired
	public LocationTypeRepository locationTypeRepository; 
	@Autowired
	public MethodOfEntryTypeRepository methodOfEntryTypeRepository; 
	@Autowired
	public BiasMotivationTypeRepository biasMotivationTypeRepository; 
	@Autowired
	public TypeOfWeaponForceInvolvedTypeRepository typeOfWeaponForceInvolvedTypeRepository; 
	@Autowired
	public OffenderSuspectedOfUsingTypeRepository offenderSuspectedOfUsingTypeRepository; 
	@Autowired
	public TypeOfCriminalActivityTypeRepository typeOfCriminalActivityTypeRepository; 
	@Autowired
	public TypePropertyLossEtcTypeRepository typePropertyLossEtcTypeRepository; 
	@Autowired
	public TypeDrugMeasurementTypeRepository typeDrugMeasurementTypeRepository; 
	@Autowired
	public PropertyDescriptionTypeRepository propertyDescriptionTypeRepository; 
	@Autowired
	public SuspectedDrugTypeTypeRepository suspectedDrugTypeTypeRepository; 
	@Autowired
	public DispositionOfArresteeUnder18TypeRepository dispositionOfArresteeUnder18TypeRepository; 
	@Autowired
	public EthnicityOfPersonTypeRepository ethnicityOfPersonTypeRepository; 
	@Autowired
	public RaceOfPersonTypeRepository raceOfPersonTypeRepository; 
	@Autowired
	public SexOfPersonTypeRepository sexOfPersonTypeRepository; 
	@Autowired
	public TypeOfArrestTypeRepository typeOfArrestTypeRepository; 
	@Autowired
	public ResidentStatusOfPersonTypeRepository residentStatusOfPersonTypeRepository; 
	@Autowired
	public MultipleArresteeSegmentsIndicatorTypeRepository multipleArresteeSegmentsIndicatorTypeRepository; 
	@Autowired
	public ArresteeWasArmedWithTypeRepository arresteeWasArmedWithTypeRepository; 
	@Autowired
	public TypeOfVictimTypeRepository typeOfVictimTypeRepository; 
	@Autowired
	public OfficerActivityCircumstanceTypeRepository officerActivityCircumstanceTypeRepository; 
	@Autowired
	public OfficerAssignmentTypeTypeRepository officerAssignmentTypeTypeRepository; 
	@Autowired
	public AdditionalJustifiableHomicideCircumstancesTypeRepository additionalJustifiableHomicideCircumstancesTypeRepository; 
	@Autowired
	public TypeInjuryTypeRepository typeInjuryTypeRepository; 
	@Autowired
	public AggravatedAssaultHomicideCircumstancesTypeRepository aggravatedAssaultHomicideCircumstancesTypeRepository; 
	@Autowired
	public VictimOffenderRelationshipTypeRepository victimOffenderRelationshipTypeRepository;
	
	public DateType getDateType(Date date) {
		
		Optional<Date> optionalDate = Optional.ofNullable(date); 
		
		DateType dateType = getCodeTableType( 
				optionalDate.map(d -> formatter.format(d)).orElse(""), 
				dateTypeRepository::findFirstByDateMMDDYYYY, 
				DateType::new);  
		return dateType;
	}
	
	public <R> R getCodeTableType(String code,  Function<String, R> findByCodeFunction, Function<Integer, R> constructorFunction ) {
		R r = null;
		if (StringUtils.isNotBlank(code)){
			r = findByCodeFunction.apply(code);
		}
		
		if (r == null && constructorFunction != null){
			r = constructorFunction.apply(99998);
		}
		return r;
	}

}
