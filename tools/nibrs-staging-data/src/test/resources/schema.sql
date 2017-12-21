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
Drop schema if exists ojbc_nibrs_staging;

CREATE schema ojbc_nibrs_staging;

CREATE TABLE CargoTheftIndicatorType (CargoTheftIndicatorTypeID INT NOT NULL AUTO_INCREMENT, CargoTheftIndicatorCode CHAR(1) NOT NULL, CargoTheftIndicatorDescription VARCHAR(7) NOT NULL);

ALTER TABLE CargoTheftIndicatorType ADD CONSTRAINT cargotheftindicatortypeid PRIMARY KEY (CargoTheftIndicatorTypeID);

CREATE TABLE AgencyType (AgencyTypeID INT NOT NULL AUTO_INCREMENT, AgencyTypeCode VARCHAR(2) NOT NULL, AgencyTypeDescription VARCHAR(80) NOT NULL);

ALTER TABLE AgencyType ADD CONSTRAINT agencytypeid PRIMARY KEY (AgencyTypeID);

CREATE TABLE DateType (DateTypeID INT NOT NULL AUTO_INCREMENT, CalendarDate date NOT NULL, Year INT NOT NULL, YearLabel CHAR(4) NOT NULL, CalendarQuarter INT NOT NULL, Month INT NOT NULL, MonthName VARCHAR(12) NOT NULL, FullMonth CHAR(7) NOT NULL, Day INT NOT NULL, DayOfWeek VARCHAR(9) NOT NULL, DayOfWeekSort INT NOT NULL, DateMMDDYYYY CHAR(10) NOT NULL);

ALTER TABLE DateType ADD CONSTRAINT datetypeid PRIMARY KEY (DateTypeID);

CREATE TABLE Agency (AgencyID INT NOT NULL AUTO_INCREMENT, AgencyORI CHAR(9) NOT NULL, AgencyName VARCHAR(60) NOT NULL, AgencyTypeID INT NOT NULL, StateCode CHAR(2) NOT NULL, StateName VARCHAR(20) NOT NULL, Population INT);

ALTER TABLE Agency ADD CONSTRAINT agency_pk PRIMARY KEY (AgencyID);

CREATE TABLE OfficerAssignmentTypeType (OfficerAssignmentTypeTypeID INT NOT NULL AUTO_INCREMENT, OfficerAssignmentTypeCode VARCHAR(1) NOT NULL, OfficerAssignmentTypeDescription VARCHAR(100) NOT NULL);

ALTER TABLE OfficerAssignmentTypeType ADD CONSTRAINT officerassignmenttypetype_pk PRIMARY KEY (OfficerAssignmentTypeTypeID);

CREATE TABLE OfficerActivityCircumstanceType (OfficerActivityCircumstanceTypeID INT NOT NULL AUTO_INCREMENT, OfficerActivityCircumstanceCode VARCHAR(2) NOT NULL, OfficerActivityCircumstanceDescription VARCHAR(75) NOT NULL);

ALTER TABLE OfficerActivityCircumstanceType ADD CONSTRAINT officeractivitycircumstancetype_pk PRIMARY KEY (OfficerActivityCircumstanceTypeID);

CREATE TABLE TypeOfWeaponForceInvolvedType (TypeOfWeaponForceInvolvedTypeID INT NOT NULL AUTO_INCREMENT, TypeOfWeaponForceInvolvedCode VARCHAR(2) NOT NULL, TypeOfWeaponForceInvolvedDescription VARCHAR(30) NOT NULL);

ALTER TABLE TypeOfWeaponForceInvolvedType ADD CONSTRAINT typeofweaponforceinvolvedtype_pk PRIMARY KEY (TypeOfWeaponForceInvolvedTypeID);

CREATE TABLE SuspectedDrugTypeType (SuspectedDrugTypeTypeID INT NOT NULL AUTO_INCREMENT, SuspectedDrugTypeCode VARCHAR(1) NOT NULL, SuspectedDrugTypeDescription VARCHAR(32) NOT NULL);

ALTER TABLE SuspectedDrugTypeType ADD CONSTRAINT suspecteddrugtypetype_pk PRIMARY KEY (SuspectedDrugTypeTypeID);

CREATE TABLE BiasMotivationType (BiasMotivationTypeID INT NOT NULL AUTO_INCREMENT, BiasMotivationCode VARCHAR(2) NOT NULL, BiasMotivationDescription VARCHAR(60) NOT NULL, BiasMotivationCategory VARCHAR(30) NOT NULL);

ALTER TABLE BiasMotivationType ADD CONSTRAINT biasmotivationtype_pk PRIMARY KEY (BiasMotivationTypeID);

CREATE TABLE MethodOfEntryType (MethodOfEntryTypeID INT NOT NULL AUTO_INCREMENT, MethodOfEntryCode VARCHAR(1) NOT NULL, MethodOfEntryDescription VARCHAR(10) NOT NULL);

ALTER TABLE MethodOfEntryType ADD CONSTRAINT methodofentrytype_pk PRIMARY KEY (MethodOfEntryTypeID);

CREATE TABLE LocationTypeType (LocationTypeTypeID INT NOT NULL AUTO_INCREMENT, LocationTypeCode VARCHAR(2) NOT NULL, LocationTypeDescription VARCHAR(45) NOT NULL);

ALTER TABLE LocationTypeType ADD CONSTRAINT locationtypetype_pk PRIMARY KEY (LocationTypeTypeID);

CREATE TABLE ClearedExceptionallyType (ClearedExceptionallyTypeID INT NOT NULL AUTO_INCREMENT, ClearedExceptionallyCode VARCHAR(1) NOT NULL, ClearedExceptionallyDescription VARCHAR(220) NOT NULL);

ALTER TABLE ClearedExceptionallyType ADD CONSTRAINT clearedexceptionallytype_pk PRIMARY KEY (ClearedExceptionallyTypeID);

CREATE TABLE DispositionOfArresteeUnder18Type (DispositionOfArresteeUnder18TypeID INT NOT NULL AUTO_INCREMENT, DispositionOfArresteeUnder18Code VARCHAR(1) NOT NULL, DispositionOfArresteeUnder18Description VARCHAR(30) NOT NULL);

ALTER TABLE DispositionOfArresteeUnder18Type ADD CONSTRAINT dispositionofarresteeunder18type_pk PRIMARY KEY (DispositionOfArresteeUnder18TypeID);

CREATE TABLE ArresteeWasArmedWithType (ArresteeWasArmedWithTypeID INT NOT NULL AUTO_INCREMENT, ArresteeWasArmedWithCode VARCHAR(2) NOT NULL, ArresteeWasArmedWithDescription VARCHAR(30) NOT NULL);

ALTER TABLE ArresteeWasArmedWithType ADD CONSTRAINT arresteewasarmedwithtype_pk PRIMARY KEY (ArresteeWasArmedWithTypeID);

CREATE TABLE MultipleArresteeSegmentsIndicatorType (MultipleArresteeSegmentsIndicatorTypeID INT NOT NULL AUTO_INCREMENT, MultipleArresteeSegmentsIndicatorCode VARCHAR(1) NOT NULL, MultipleArresteeSegmentsIndicatorDescription VARCHAR(15) NOT NULL);

ALTER TABLE MultipleArresteeSegmentsIndicatorType ADD CONSTRAINT multiplearresteesegmentsindicatortype_pk PRIMARY KEY (MultipleArresteeSegmentsIndicatorTypeID);

CREATE TABLE TypeOfArrestType (TypeOfArrestTypeID INT NOT NULL AUTO_INCREMENT, TypeOfArrestCode VARCHAR(1) NOT NULL, TypeOfArrestDescription VARCHAR(20) NOT NULL);

ALTER TABLE TypeOfArrestType ADD CONSTRAINT typeofarresttype_pk PRIMARY KEY (TypeOfArrestTypeID);

CREATE TABLE AdditionalJustifiableHomicideCircumstancesType (AdditionalJustifiableHomicideCircumstancesTypeID INT NOT NULL AUTO_INCREMENT, AdditionalJustifiableHomicideCircumstancesCode VARCHAR(1) NOT NULL, AdditionalJustifiableHomicideCircumstancesDescription VARCHAR(80) NOT NULL);

ALTER TABLE AdditionalJustifiableHomicideCircumstancesType ADD CONSTRAINT additionaljustifiablehomicidecircumstancestype_pk PRIMARY KEY (AdditionalJustifiableHomicideCircumstancesTypeID);

CREATE TABLE TypeInjuryType (TypeInjuryTypeID INT NOT NULL AUTO_INCREMENT, TypeInjuryCode VARCHAR(1) NOT NULL, TypeInjuryDescription VARCHAR(25) NOT NULL);

ALTER TABLE TypeInjuryType ADD CONSTRAINT typeinjurytype_pk PRIMARY KEY (TypeInjuryTypeID);

CREATE TABLE ResidentStatusOfPersonType (ResidentStatusOfPersonTypeID INT NOT NULL AUTO_INCREMENT, ResidentStatusOfPersonCode VARCHAR(1) NOT NULL, ResidentStatusOfPersonDescription VARCHAR(15) NOT NULL);

ALTER TABLE ResidentStatusOfPersonType ADD CONSTRAINT residentstatusofpersontype_pk PRIMARY KEY (ResidentStatusOfPersonTypeID);

CREATE TABLE EthnicityOfPersonType (EthnicityOfPersonTypeID INT NOT NULL AUTO_INCREMENT, EthnicityOfPersonCode VARCHAR(1) NOT NULL, EthnicityOfPersonDescription VARCHAR(25) NOT NULL);

ALTER TABLE EthnicityOfPersonType ADD CONSTRAINT ethnicityofpersontype_pk PRIMARY KEY (EthnicityOfPersonTypeID);

CREATE TABLE TypeOfVictimType (TypeOfVictimTypeID INT NOT NULL AUTO_INCREMENT, TypeOfVictimCode VARCHAR(1) NOT NULL, TypeOfVictimDescription VARCHAR(75) NOT NULL);

ALTER TABLE TypeOfVictimType ADD CONSTRAINT typeofvictimtype_pk PRIMARY KEY (TypeOfVictimTypeID);

CREATE TABLE TypeDrugMeasurementType (TypeDrugMeasurementTypeID INT NOT NULL AUTO_INCREMENT, TypeDrugMeasurementCode VARCHAR(2) NOT NULL, TypeDrugMeasurementDescription VARCHAR(20) NOT NULL);

ALTER TABLE TypeDrugMeasurementType ADD CONSTRAINT typedrugmeasurementtype_pk PRIMARY KEY (TypeDrugMeasurementTypeID);

CREATE TABLE PropertyDescriptionType (PropertyDescriptionTypeID INT NOT NULL AUTO_INCREMENT, PropertyDescriptionCode VARCHAR(2) NOT NULL, PropertyDescriptionDescription VARCHAR(70) NOT NULL);

ALTER TABLE PropertyDescriptionType ADD CONSTRAINT propertydescriptiontype_pk PRIMARY KEY (PropertyDescriptionTypeID);

CREATE TABLE RaceOfPersonType (RaceOfPersonTypeID INT NOT NULL AUTO_INCREMENT, RaceOfPersonCode VARCHAR(1) NOT NULL, RaceOfPersonDescription VARCHAR(42) NOT NULL);

ALTER TABLE RaceOfPersonType ADD CONSTRAINT raceofpersontype_pk PRIMARY KEY (RaceOfPersonTypeID);

CREATE TABLE SexOfPersonType (SexOfPersonTypeID INT NOT NULL AUTO_INCREMENT, SexOfPersonCode VARCHAR(1) NOT NULL, SexOfPersonDescription VARCHAR(35) NOT NULL);

ALTER TABLE SexOfPersonType ADD CONSTRAINT sexofpersontype_pk PRIMARY KEY (SexOfPersonTypeID);

CREATE TABLE TypeOfCriminalActivityType (TypeOfCriminalActivityTypeID INT NOT NULL AUTO_INCREMENT, TypeOfCriminalActivityCode VARCHAR(1) NOT NULL, TypeOfCriminalActivityDescription VARCHAR(80) NOT NULL);

ALTER TABLE TypeOfCriminalActivityType ADD CONSTRAINT typeofcriminalactivitytype_pk PRIMARY KEY (TypeOfCriminalActivityTypeID);

CREATE TABLE OffenderSuspectedOfUsingType (OffenderSuspectedOfUsingTypeID INT NOT NULL AUTO_INCREMENT, OffenderSuspectedOfUsingCode VARCHAR(1) NOT NULL, OffenderSuspectedOfUsingDescription VARCHAR(20) NOT NULL);

ALTER TABLE OffenderSuspectedOfUsingType ADD CONSTRAINT offendersuspectedofusingtype_pk PRIMARY KEY (OffenderSuspectedOfUsingTypeID);

CREATE TABLE SegmentActionTypeType (SegmentActionTypeTypeID INT NOT NULL AUTO_INCREMENT, SegmentActionTypeCode VARCHAR(1) NOT NULL, SegmentActionTypeDescription VARCHAR(25) NOT NULL);

ALTER TABLE SegmentActionTypeType ADD CONSTRAINT segmentactiontypetype_pk PRIMARY KEY (SegmentActionTypeTypeID);

CREATE TABLE VictimOffenderRelationshipType (VictimOffenderRelationshipTypeID INT NOT NULL AUTO_INCREMENT, VictimOffenderRelationshipCode VARCHAR(2) NOT NULL, VictimOffenderRelationshipDescription VARCHAR(50) NOT NULL);

ALTER TABLE VictimOffenderRelationshipType ADD CONSTRAINT victimoffenderrelationshiptype_pk PRIMARY KEY (VictimOffenderRelationshipTypeID);

CREATE TABLE AggravatedAssaultHomicideCircumstancesType (AggravatedAssaultHomicideCircumstancesTypeID INT NOT NULL AUTO_INCREMENT, AggravatedAssaultHomicideCircumstancesCode VARCHAR(3) NOT NULL, AggravatedAssaultHomicideCircumstancesDescription VARCHAR(55) NOT NULL);

ALTER TABLE AggravatedAssaultHomicideCircumstancesType ADD CONSTRAINT aggravatedassaulthomicidecircumstancestype_pk PRIMARY KEY (AggravatedAssaultHomicideCircumstancesTypeID);

CREATE TABLE TypePropertyLossEtcType (TypePropertyLossEtcTypeID INT NOT NULL AUTO_INCREMENT, TypePropertyLossEtcCode VARCHAR(1) NOT NULL, TypePropertyLossEtcDescription VARCHAR(85) NOT NULL);

ALTER TABLE TypePropertyLossEtcType ADD CONSTRAINT typepropertylossetctype_pk PRIMARY KEY (TypePropertyLossEtcTypeID);

CREATE TABLE UCROffenseCodeType (UCROffenseCodeTypeID INT NOT NULL AUTO_INCREMENT, UCROffenseCode VARCHAR(3) NOT NULL, UCROffenseCodeDescription VARCHAR(70) NOT NULL, OffenseCategory1 VARCHAR(70) NOT NULL, OffenseCategory2 VARCHAR(70) NOT NULL, OffenseCategory3 VARCHAR(70) NOT NULL, OffenseCategory4 VARCHAR(70) NOT NULL);

ALTER TABLE UCROffenseCodeType ADD CONSTRAINT ucroffensecode_pk PRIMARY KEY (UCROffenseCodeTypeID);

CREATE TABLE ArrestReportSegment (ArrestReportSegmentID INT NOT NULL AUTO_INCREMENT, SegmentActionTypeTypeID INT NOT NULL, MonthOfTape VARCHAR(2), YearOfTape VARCHAR(4), CityIndicator VARCHAR(4), AgencyID INT NOT NULL, ORI VARCHAR(9), ArrestTransactionNumber VARCHAR(12), ArresteeSequenceNumber INT NOT NULL, ArrestDate date, ArrestDateID INT NOT NULL, TypeOfArrestTypeID INT NOT NULL, AgeOfArresteeMin INT, AgeOfArresteeMax INT, SexOfPersonTypeID INT NOT NULL, RaceOfPersonTypeID INT NOT NULL, EthnicityOfPersonTypeID INT NOT NULL, ResidentStatusOfPersonTypeID INT NOT NULL, DispositionOfArresteeUnder18TypeID INT NOT NULL, UCROffenseCodeTypeID INT NOT NULL);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT arrestereport_pk PRIMARY KEY (ArrestReportSegmentID);

CREATE TABLE ArrestReportSegmentWasArmedWith (ArrestReportSegmentWasArmedWithID INT NOT NULL AUTO_INCREMENT, ArrestReportSegmentID INT NOT NULL, ArresteeWasArmedWithTypeID INT NOT NULL, AutomaticWeaponIndicator VARCHAR(1));

ALTER TABLE ArrestReportSegmentWasArmedWith ADD CONSTRAINT arrestreportsegmentwasarmedwithid PRIMARY KEY (ArrestReportSegmentWasArmedWithID);

CREATE TABLE AdministrativeSegment (AdministrativeSegmentID INT NOT NULL AUTO_INCREMENT, SegmentActionTypeTypeID INT NOT NULL, MonthOfTape VARCHAR(2), YearOfTape VARCHAR(4), CityIndicator VARCHAR(4), ORI VARCHAR(9), AgencyID INT NOT NULL, IncidentNumber VARCHAR(12), IncidentDate date, IncidentDateID INT NOT NULL, ReportDateIndicator VARCHAR(1), IncidentHour VARCHAR(2) NOT NULL, ClearedExceptionallyTypeID INT NOT NULL, ExceptionalClearanceDate DATE,ExceptionalClearanceDateID INT NOT NULL, CargoTheftIndicatorTypeID INT NOT NULL);

ALTER TABLE AdministrativeSegment ADD CONSTRAINT administrativesegment_pk PRIMARY KEY (AdministrativeSegmentID);

CREATE TABLE ArresteeSegment (ArresteeSegmentID INT NOT NULL AUTO_INCREMENT, SegmentActionTypeTypeID INT NOT NULL, AdministrativeSegmentID INT NOT NULL, ArresteeSequenceNumber INT NOT NULL, ArrestTransactionNumber VARCHAR(12), ArrestDate date, ArrestDateID INT NOT NULL, TypeOfArrestTypeID INT NOT NULL, MultipleArresteeSegmentsIndicatorTypeID INT NOT NULL, AgeOfArresteeMin INT, AgeOfArresteeMax INT, SexOfPersonTypeID INT NOT NULL, RaceOfPersonTypeID INT NOT NULL, EthnicityOfPersonTypeID INT NOT NULL, ResidentStatusOfPersonTypeID INT NOT NULL, DispositionOfArresteeUnder18TypeID INT NOT NULL, UCROffenseCodeTypeID INT NOT NULL);

ALTER TABLE ArresteeSegment ADD CONSTRAINT arrestee_pk PRIMARY KEY (ArresteeSegmentID);

CREATE TABLE ArresteeSegmentWasArmedWith (ArresteeSegmentWasArmedWithID INT NOT NULL AUTO_INCREMENT, ArresteeSegmentID INT NOT NULL, ArresteeWasArmedWithTypeID INT NOT NULL, AutomaticWeaponIndicator VARCHAR(1));

ALTER TABLE ArresteeSegmentWasArmedWith ADD CONSTRAINT arresteesegmentwasarmedwithid PRIMARY KEY (ArresteeSegmentWasArmedWithID);

CREATE TABLE OffenderSegment (OffenderSegmentID INT NOT NULL AUTO_INCREMENT, SegmentActionTypeTypeID INT NOT NULL, AdministrativeSegmentID INT NOT NULL, OffenderSequenceNumber INT, AgeOfOffenderMin INT, AgeOfOffenderMax INT, SexOfPersonTypeID INT NOT NULL, RaceOfPersonTypeID INT NOT NULL, EthnicityOfPersonTypeID INT NOT NULL);

ALTER TABLE OffenderSegment ADD CONSTRAINT offendersegment_pk PRIMARY KEY (OffenderSegmentID);

CREATE TABLE VictimSegment (VictimSegmentID INT NOT NULL AUTO_INCREMENT, SegmentActionTypeTypeID INT NOT NULL, AdministrativeSegmentID INT NOT NULL, VictimSequenceNumber INT, TypeOfVictimTypeID INT NOT NULL, OfficerActivityCircumstanceTypeID INT NOT NULL, OfficerAssignmentTypeTypeID INT NOT NULL, AgeOfVictimMin INT, AgeOfVictimMax INT, AgeNeonateIndicator INT NOT NULL, AgeFirstWeekIndicator INT NOT NULL, AgeFirstYearIndicator INT NOT NULL, SexOfPersonTypeID INT NOT NULL, RaceOfPersonTypeID INT NOT NULL, EthnicityOfPersonTypeID INT NOT NULL, ResidentStatusOfPersonTypeID INT NOT NULL, AdditionalJustifiableHomicideCircumstancesTypeID INT NOT NULL);

ALTER TABLE VictimSegment ADD CONSTRAINT victimsegment_pk PRIMARY KEY (VictimSegmentID);

CREATE TABLE TypeInjury (TypeInjuryID INT NOT NULL AUTO_INCREMENT, VictimSegmentID INT NOT NULL, TypeInjuryTypeID INT NOT NULL);

ALTER TABLE TypeInjury ADD CONSTRAINT typeinjury_pk PRIMARY KEY (TypeInjuryID);

CREATE TABLE AggravatedAssaultHomicideCircumstances (AggravatedAssaultHomicideCircumstancesID INT NOT NULL AUTO_INCREMENT, VictimSegmentID INT NOT NULL, AggravatedAssaultHomicideCircumstancesTypeID INT NOT NULL);

ALTER TABLE AggravatedAssaultHomicideCircumstances ADD CONSTRAINT aggravatedassaulthomicidecircumstances_pk PRIMARY KEY (AggravatedAssaultHomicideCircumstancesID);

CREATE TABLE VictimOffenderAssociation (VictimOffenderAssociationID INT NOT NULL AUTO_INCREMENT, VictimSegmentID INT NOT NULL, OffenderSegmentID INT NOT NULL, VictimOffenderRelationshipTypeID INT NOT NULL);

ALTER TABLE VictimOffenderAssociation ADD CONSTRAINT victimoffenderassociation_pk PRIMARY KEY (VictimOffenderAssociationID);

CREATE TABLE PropertySegment (PropertySegmentID INT NOT NULL AUTO_INCREMENT, SegmentActionTypeTypeID INT NOT NULL, AdministrativeSegmentID INT NOT NULL, TypePropertyLossEtcTypeID INT NOT NULL, NumberOfStolenMotorVehicles INT, NumberOfRecoveredMotorVehicles INT);

ALTER TABLE PropertySegment ADD CONSTRAINT propertysegmentpk PRIMARY KEY (PropertySegmentID);

CREATE TABLE PropertyType (PropertyTypeID INT NOT NULL AUTO_INCREMENT, PropertySegmentID INT NOT NULL, PropertyDescriptionTypeID INT NOT NULL, ValueOfProperty NUMBER(0, 0), RecoveredDate date, RecoveredDateID INT NOT NULL);

ALTER TABLE PropertyType ADD CONSTRAINT propertytypeid PRIMARY KEY (PropertyTypeID);

CREATE TABLE SuspectedDrugType (SuspectedDrugTypeID INT NOT NULL AUTO_INCREMENT, PropertySegmentID INT NOT NULL, SuspectedDrugTypeTypeID INT NOT NULL, TypeDrugMeasurementTypeID INT NOT NULL, EstimatedDrugQuantity DECIMAL(10, 3));

ALTER TABLE SuspectedDrugType ADD CONSTRAINT suspecteddrugtype_pk PRIMARY KEY (SuspectedDrugTypeID);

CREATE TABLE OffenseSegment (OffenseSegmentID INT NOT NULL AUTO_INCREMENT, SegmentActionTypeTypeID INT NOT NULL, AdministrativeSegmentID INT NOT NULL, UCROffenseCodeTypeID INT NOT NULL, OffenseAttemptedCompleted VARCHAR(1), LocationTypeTypeID INT NOT NULL, NumberOfPremisesEntered INT, MethodOfEntryTypeID INT NOT NULL);

ALTER TABLE OffenseSegment ADD CONSTRAINT offense_pk PRIMARY KEY (OffenseSegmentID);

CREATE TABLE BiasMotivation (BiasMotivationID INT AUTO_INCREMENT NOT NULL, OffenseSegmentID INT NOT NULL AUTO_INCREMENT, BiasMotivationTypeID INT NOT NULL);

ALTER TABLE BiasMotivation ADD CONSTRAINT BiasMotivationID PRIMARY KEY (BiasMotivationID);

CREATE SEQUENCE BiasMotivation_BiasMotivationID_seq;

CREATE TABLE VictimOffenseAssociation (VictimOffenseAssociationID INT NOT NULL AUTO_INCREMENT, VictimSegmentID INT NOT NULL, OffenseSegmentID INT NOT NULL);

ALTER TABLE VictimOffenseAssociation ADD CONSTRAINT victimoffenseassociation_pk PRIMARY KEY (VictimOffenseAssociationID);

CREATE TABLE TypeOfWeaponForceInvolved (TypeOfWeaponForceInvolvedID INT NOT NULL AUTO_INCREMENT, AutomaticWeaponIndicator VARCHAR(1) NOT NULL, OffenseSegmentID INT NOT NULL, TypeOfWeaponForceInvolvedTypeID INT NOT NULL);

ALTER TABLE TypeOfWeaponForceInvolved ADD CONSTRAINT typeofweaponforceinvolved_pk PRIMARY KEY (TypeOfWeaponForceInvolvedID);

CREATE TABLE TypeCriminalActivity (TypeCriminalActivityID INT NOT NULL AUTO_INCREMENT, OffenseSegmentID INT NOT NULL, TypeOfCriminalActivityTypeID INT NOT NULL);

ALTER TABLE TypeCriminalActivity ADD CONSTRAINT typecriminalactivity_pk PRIMARY KEY (TypeCriminalActivityID);

CREATE TABLE OffenderSuspectedOfUsing (OffenderSuspectedOfUsingID INT NOT NULL AUTO_INCREMENT, OffenseSegmentID INT NOT NULL, OffenderSuspectedOfUsingTypeID INT NOT NULL);

ALTER TABLE OffenderSuspectedOfUsing ADD CONSTRAINT offendersuspectedofusing_pk PRIMARY KEY (OffenderSuspectedOfUsingID);

CREATE TABLE ZeroReportingSegment (ZeroReportingSegmentID INT NOT NULL AUTO_INCREMENT, AdministrativeSegmentID INT NOT NULL, RecordDescriptionWord VARCHAR(4), SegmentLevel VARCHAR(1), MonthOfTape VARCHAR(2), YearOfTape VARCHAR(4), CityIndicator VARCHAR(4), IncidentDate date, IncidentDateID INT NOT NULL, ReportDateIndicator VARCHAR(1), IncidentHour VARCHAR(4), CleardExceptionally VARCHAR(1), ExceptionalClearanceDate date);

ALTER TABLE ZeroReportingSegment ADD CONSTRAINT zeroreportingsegment_pk PRIMARY KEY (ZeroReportingSegmentID);

CREATE TABLE LEOKASegment (LEOKASegmentID INT NOT NULL AUTO_INCREMENT, AdministrativeSegmentID INT NOT NULL, RecordDescriptionWord VARCHAR(4), SegmentLevel VARCHAR(1), MonthOfTape VARCHAR(2), YearOfTape VARCHAR(4), CityIndicator VARCHAR(4), Filler VARCHAR(12), LEOKAData VARCHAR(600));

ALTER TABLE LEOKASegment ADD CONSTRAINT idleokasegment PRIMARY KEY (LEOKASegmentID);

ALTER TABLE AdministrativeSegment ADD CONSTRAINT CargoTheftIndicatorType_AdministrativeSegment_fk FOREIGN KEY (CargoTheftIndicatorTypeID) REFERENCES CargoTheftIndicatorType (CargoTheftIndicatorTypeID);

ALTER TABLE Agency ADD CONSTRAINT AgencyType_Agency_fk FOREIGN KEY (AgencyTypeID) REFERENCES AgencyType (AgencyTypeID);

ALTER TABLE AdministrativeSegment ADD CONSTRAINT Date_AdministrativeSegment_fk FOREIGN KEY (IncidentDateID) REFERENCES DateType (DateTypeID);

ALTER TABLE PropertyType ADD CONSTRAINT Date_PropertyType_fk FOREIGN KEY (RecoveredDateID) REFERENCES DateType (DateTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT Date_ArresteeSegment_fk FOREIGN KEY (ArrestDateID) REFERENCES DateType (DateTypeID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT Date_ArrestReportSegment_fk FOREIGN KEY (ArrestDateID) REFERENCES DateType (DateTypeID);

ALTER TABLE ZeroReportingSegment ADD CONSTRAINT Date_ZeroReportingSegment_fk FOREIGN KEY (IncidentDateID) REFERENCES DateType (DateTypeID);

ALTER TABLE AdministrativeSegment ADD CONSTRAINT datetype_administrativesegment_fk FOREIGN KEY (ExceptionalClearanceDateID) REFERENCES DateType (DateTypeID);

ALTER TABLE AdministrativeSegment ADD CONSTRAINT Agency_AdministrativeSegment_fk FOREIGN KEY (AgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT Agency_ArrestReportSegment_fk FOREIGN KEY (AgencyID) REFERENCES Agency (AgencyID);

ALTER TABLE VictimSegment ADD CONSTRAINT OfficerAssignmentTypeType_VictimSegment_fk FOREIGN KEY (OfficerAssignmentTypeTypeID) REFERENCES OfficerAssignmentTypeType (OfficerAssignmentTypeTypeID);

ALTER TABLE VictimSegment ADD CONSTRAINT TypeOfOfficerActivityCircumstanceType_VictimSegment_fk FOREIGN KEY (OfficerActivityCircumstanceTypeID) REFERENCES OfficerActivityCircumstanceType (OfficerActivityCircumstanceTypeID);

ALTER TABLE TypeOfWeaponForceInvolved ADD CONSTRAINT TypeOfWeaponForceInvolvedType_TypeOfWeaponForceInvolved_fk FOREIGN KEY (TypeOfWeaponForceInvolvedTypeID) REFERENCES TypeOfWeaponForceInvolvedType (TypeOfWeaponForceInvolvedTypeID);

ALTER TABLE SuspectedDrugType ADD CONSTRAINT SuspectedDrugTypeType_SuspectedDrugType_fk FOREIGN KEY (SuspectedDrugTypeTypeID) REFERENCES SuspectedDrugTypeType (SuspectedDrugTypeTypeID);

ALTER TABLE BiasMotivation ADD CONSTRAINT BiasMotivationType_BiasMotivation_fk FOREIGN KEY (BiasMotivationTypeID) REFERENCES BiasMotivationType (BiasMotivationTypeID);

ALTER TABLE OffenseSegment ADD CONSTRAINT MethodOfEntryType_OffenseSegment_fk FOREIGN KEY (MethodOfEntryTypeID) REFERENCES MethodOfEntryType (MethodOfEntryTypeID);

ALTER TABLE OffenseSegment ADD CONSTRAINT LocationTypeType_OffenseSegment_fk FOREIGN KEY (LocationTypeTypeID) REFERENCES LocationTypeType (LocationTypeTypeID);

ALTER TABLE AdministrativeSegment ADD CONSTRAINT ClearedExceptionallyType_AdminSeg_fk FOREIGN KEY (ClearedExceptionallyTypeID) REFERENCES ClearedExceptionallyType (ClearedExceptionallyTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT DispositionOfArresteeUnder18Lookup_ArresteeSegment_fk FOREIGN KEY (DispositionOfArresteeUnder18TypeID) REFERENCES DispositionOfArresteeUnder18Type (DispositionOfArresteeUnder18TypeID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT DispositionOfArresteeUnder18Lookup_ArrestReportSegment_fk FOREIGN KEY (DispositionOfArresteeUnder18TypeID) REFERENCES DispositionOfArresteeUnder18Type (DispositionOfArresteeUnder18TypeID);

ALTER TABLE ArresteeSegmentWasArmedWith ADD CONSTRAINT ArresteeArmedWithTypeLookup_ArresteeArmedWith_fk FOREIGN KEY (ArresteeWasArmedWithTypeID) REFERENCES ArresteeWasArmedWithType (ArresteeWasArmedWithTypeID);

ALTER TABLE ArrestReportSegmentWasArmedWith ADD CONSTRAINT ArresteeWasArmedWithType_ArrestReportSegmentWasArmedWith_1_fk FOREIGN KEY (ArresteeWasArmedWithTypeID) REFERENCES ArresteeWasArmedWithType (ArresteeWasArmedWithTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT MultipleArresteeSegmentsIndicatorLookup_ArresteeSegment_fk FOREIGN KEY (MultipleArresteeSegmentsIndicatorTypeID) REFERENCES MultipleArresteeSegmentsIndicatorType (MultipleArresteeSegmentsIndicatorTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT TypeOfArrestLookup_ArresteeSegment_fk FOREIGN KEY (TypeOfArrestTypeID) REFERENCES TypeOfArrestType (TypeOfArrestTypeID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT TypeOfArrestLookup_ArrestReportSegment_fk FOREIGN KEY (TypeOfArrestTypeID) REFERENCES TypeOfArrestType (TypeOfArrestTypeID);

ALTER TABLE VictimSegment ADD CONSTRAINT AdditionalJustifiableHomicideCircumstancesType_VictimSegment_fk FOREIGN KEY (AdditionalJustifiableHomicideCircumstancesTypeID) REFERENCES AdditionalJustifiableHomicideCircumstancesType (AdditionalJustifiableHomicideCircumstancesTypeID);

ALTER TABLE TypeInjury ADD CONSTRAINT TypeOfInjuryLookup_InjuryTable_fk FOREIGN KEY (TypeInjuryTypeID) REFERENCES TypeInjuryType (TypeInjuryTypeID);

ALTER TABLE VictimSegment ADD CONSTRAINT ResidentStatusOfPersonType_VictimSegment_fk FOREIGN KEY (ResidentStatusOfPersonTypeID) REFERENCES ResidentStatusOfPersonType (ResidentStatusOfPersonTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT ResidentStatusOfPersonType_ArresteeSegment_fk FOREIGN KEY (ResidentStatusOfPersonTypeID) REFERENCES ResidentStatusOfPersonType (ResidentStatusOfPersonTypeID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT ResidentStatusOfPersonType_ArrestReportSegment_fk FOREIGN KEY (ResidentStatusOfPersonTypeID) REFERENCES ResidentStatusOfPersonType (ResidentStatusOfPersonTypeID);

ALTER TABLE VictimSegment ADD CONSTRAINT EthnicityOfPersonType_VictimSegment_fk FOREIGN KEY (EthnicityOfPersonTypeID) REFERENCES EthnicityOfPersonType (EthnicityOfPersonTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT EthnicityOfPersonType_ArresteeSegment_fk FOREIGN KEY (EthnicityOfPersonTypeID) REFERENCES EthnicityOfPersonType (EthnicityOfPersonTypeID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT EthnicityOfPersonType_ArrestReportSegment_fk FOREIGN KEY (EthnicityOfPersonTypeID) REFERENCES EthnicityOfPersonType (EthnicityOfPersonTypeID);

ALTER TABLE OffenderSegment ADD CONSTRAINT EthnicityOfPersonType_OffenderSegment_fk FOREIGN KEY (EthnicityOfPersonTypeID) REFERENCES EthnicityOfPersonType (EthnicityOfPersonTypeID);

ALTER TABLE VictimSegment ADD CONSTRAINT TypeOfVictimType_VictimSegment_fk FOREIGN KEY (TypeOfVictimTypeID) REFERENCES TypeOfVictimType (TypeOfVictimTypeID);

ALTER TABLE SuspectedDrugType ADD CONSTRAINT TypeDrugMeasurementType_SuspectedDrugType_fk FOREIGN KEY (TypeDrugMeasurementTypeID) REFERENCES TypeDrugMeasurementType (TypeDrugMeasurementTypeID);

ALTER TABLE PropertyType ADD CONSTRAINT PropertyDescriptionType_PropertyType_fk FOREIGN KEY (PropertyDescriptionTypeID) REFERENCES PropertyDescriptionType (PropertyDescriptionTypeID);

ALTER TABLE VictimSegment ADD CONSTRAINT RaceOfPersonType_VictimSegment_fk FOREIGN KEY (RaceOfPersonTypeID) REFERENCES RaceOfPersonType (RaceOfPersonTypeID);

ALTER TABLE OffenderSegment ADD CONSTRAINT RaceOfPersonType_Offender_fk FOREIGN KEY (RaceOfPersonTypeID) REFERENCES RaceOfPersonType (RaceOfPersonTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT RaceOfPersonType_ArresteeSegment_fk FOREIGN KEY (RaceOfPersonTypeID) REFERENCES RaceOfPersonType (RaceOfPersonTypeID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT RaceOfPersonType_ArrestReportSegment_fk FOREIGN KEY (RaceOfPersonTypeID) REFERENCES RaceOfPersonType (RaceOfPersonTypeID);

ALTER TABLE OffenderSegment ADD CONSTRAINT SexCodeLookup_Offender_fk FOREIGN KEY (SexOfPersonTypeID) REFERENCES SexOfPersonType (SexOfPersonTypeID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT SexCodeLookup_ArrestReportSegment_fk FOREIGN KEY (SexOfPersonTypeID) REFERENCES SexOfPersonType (SexOfPersonTypeID);

ALTER TABLE VictimSegment ADD CONSTRAINT SexOfPersonType_VictimSegment_fk FOREIGN KEY (SexOfPersonTypeID) REFERENCES SexOfPersonType (SexOfPersonTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT SexOfPersonType_ArresteeSegment_fk FOREIGN KEY (SexOfPersonTypeID) REFERENCES SexOfPersonType (SexOfPersonTypeID);

ALTER TABLE TypeCriminalActivity ADD CONSTRAINT TypeOfCriminalActivityType_TypeCriminalActivity_fk FOREIGN KEY (TypeOfCriminalActivityTypeID) REFERENCES TypeOfCriminalActivityType (TypeOfCriminalActivityTypeID);

ALTER TABLE OffenderSuspectedOfUsing ADD CONSTRAINT OffenderSuspectedOfUsingType_OffenderSuspectedOfUsing_fk FOREIGN KEY (OffenderSuspectedOfUsingTypeID) REFERENCES OffenderSuspectedOfUsingType (OffenderSuspectedOfUsingTypeID);

ALTER TABLE AdministrativeSegment ADD CONSTRAINT SegmentActionType_AdminSeg_fk FOREIGN KEY (SegmentActionTypeTypeID) REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID);

ALTER TABLE OffenseSegment ADD CONSTRAINT SegmentActionType_OffenseSegment_fk FOREIGN KEY (SegmentActionTypeTypeID) REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID);

ALTER TABLE PropertySegment ADD CONSTRAINT SegmentActionType_PropertySegment_fk FOREIGN KEY (SegmentActionTypeTypeID) REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID);

ALTER TABLE VictimSegment ADD CONSTRAINT SegmentActionType_VictimSegment_fk FOREIGN KEY (SegmentActionTypeTypeID) REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID);

ALTER TABLE OffenderSegment ADD CONSTRAINT SegmentActionType_Offender_fk FOREIGN KEY (SegmentActionTypeTypeID) REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT SegmentActionType_ArresteeSegment_fk FOREIGN KEY (SegmentActionTypeTypeID) REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT SegmentActionType_ArrestReportSegment_fk FOREIGN KEY (SegmentActionTypeTypeID) REFERENCES SegmentActionTypeType (SegmentActionTypeTypeID);

ALTER TABLE VictimOffenderAssociation ADD CONSTRAINT VictimOffenderRelationshipType_VictimAssociation_fk FOREIGN KEY (VictimOffenderRelationshipTypeID) REFERENCES VictimOffenderRelationshipType (VictimOffenderRelationshipTypeID);

ALTER TABLE AggravatedAssaultHomicideCircumstances ADD CONSTRAINT AAHomicideCircumstancesType_AAHomicideCircumstances_fk FOREIGN KEY (AggravatedAssaultHomicideCircumstancesTypeID) REFERENCES AggravatedAssaultHomicideCircumstancesType (AggravatedAssaultHomicideCircumstancesTypeID);

ALTER TABLE PropertySegment ADD CONSTRAINT TypePropertyLossEtcType_PropertySegment_fk FOREIGN KEY (TypePropertyLossEtcTypeID) REFERENCES TypePropertyLossEtcType (TypePropertyLossEtcTypeID);

ALTER TABLE OffenseSegment ADD CONSTRAINT UCROffenseCodeType_OffenseSegment_fk FOREIGN KEY (UCROffenseCodeTypeID) REFERENCES UCROffenseCodeType (UCROffenseCodeTypeID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT UCROffenseCodeType_ArresteeSegment_fk FOREIGN KEY (UCROffenseCodeTypeID) REFERENCES UCROffenseCodeType (UCROffenseCodeTypeID);

ALTER TABLE ArrestReportSegment ADD CONSTRAINT UCROffenseCodeType_ArrestReportSegment_fk FOREIGN KEY (UCROffenseCodeTypeID) REFERENCES UCROffenseCodeType (UCROffenseCodeTypeID);

ALTER TABLE ArrestReportSegmentWasArmedWith ADD CONSTRAINT ArrestReportSegment_ArrestReportSegmentWasArmedWith_1_fk FOREIGN KEY (ArrestReportSegmentID) REFERENCES ArrestReportSegment (ArrestReportSegmentID);

ALTER TABLE LEOKASegment ADD CONSTRAINT AdminSeg_LEOKASegment_fk FOREIGN KEY (AdministrativeSegmentID) REFERENCES AdministrativeSegment (AdministrativeSegmentID);

ALTER TABLE ZeroReportingSegment ADD CONSTRAINT AdminSeg_ZeroReportingSegment_fk FOREIGN KEY (AdministrativeSegmentID) REFERENCES AdministrativeSegment (AdministrativeSegmentID);

ALTER TABLE OffenseSegment ADD CONSTRAINT AdministrativeSegment_OffenseSegment_fk FOREIGN KEY (AdministrativeSegmentID) REFERENCES AdministrativeSegment (AdministrativeSegmentID);

ALTER TABLE PropertySegment ADD CONSTRAINT AdministrativeSegment_PropertySegment_fk FOREIGN KEY (AdministrativeSegmentID) REFERENCES AdministrativeSegment (AdministrativeSegmentID);

ALTER TABLE VictimSegment ADD CONSTRAINT AdministrativeSegment_VictimSegment_fk FOREIGN KEY (AdministrativeSegmentID) REFERENCES AdministrativeSegment (AdministrativeSegmentID);

ALTER TABLE OffenderSegment ADD CONSTRAINT AdministrativeSegment_Offender_fk FOREIGN KEY (AdministrativeSegmentID) REFERENCES AdministrativeSegment (AdministrativeSegmentID);

ALTER TABLE ArresteeSegment ADD CONSTRAINT AdministrativeSegment_ArresteeSegment_fk FOREIGN KEY (AdministrativeSegmentID) REFERENCES AdministrativeSegment (AdministrativeSegmentID);

ALTER TABLE ArresteeSegmentWasArmedWith ADD CONSTRAINT ArresteeSegment_ArresteeWasArmedWith_fk FOREIGN KEY (ArresteeSegmentID) REFERENCES ArresteeSegment (ArresteeSegmentID);

ALTER TABLE VictimOffenderAssociation ADD CONSTRAINT OffenderSegment_SegmentAssociation_fk FOREIGN KEY (OffenderSegmentID) REFERENCES OffenderSegment (OffenderSegmentID);

ALTER TABLE VictimOffenderAssociation ADD CONSTRAINT VictimSegment_UCROffenseCodeAssociation_fk FOREIGN KEY (VictimSegmentID) REFERENCES VictimSegment (VictimSegmentID);

ALTER TABLE AggravatedAssaultHomicideCircumstances ADD CONSTRAINT VictimSegment_AggravatedAssaultHomicideCircumstances_fk FOREIGN KEY (VictimSegmentID) REFERENCES VictimSegment (VictimSegmentID);

ALTER TABLE TypeInjury ADD CONSTRAINT VictimSegment_InjuryTable_fk FOREIGN KEY (VictimSegmentID) REFERENCES VictimSegment (VictimSegmentID);

ALTER TABLE VictimOffenseAssociation ADD CONSTRAINT VictimSegment_VictimOffenseAssociation_fk FOREIGN KEY (VictimSegmentID) REFERENCES VictimSegment (VictimSegmentID);

ALTER TABLE SuspectedDrugType ADD CONSTRAINT PropertySegment_SuspectedDrugType_fk FOREIGN KEY (PropertySegmentID) REFERENCES PropertySegment (PropertySegmentID);

ALTER TABLE PropertyType ADD CONSTRAINT PropertySegment_PropertyType_fk FOREIGN KEY (PropertySegmentID) REFERENCES PropertySegment (PropertySegmentID);

ALTER TABLE OffenderSuspectedOfUsing ADD CONSTRAINT OffenseSegment_OffenderSuspectedOfUsing_fk FOREIGN KEY (OffenseSegmentID) REFERENCES OffenseSegment (OffenseSegmentID);

ALTER TABLE TypeCriminalActivity ADD CONSTRAINT OffenseSegment_TypeCriminalActivity_fk FOREIGN KEY (OffenseSegmentID) REFERENCES OffenseSegment (OffenseSegmentID);

ALTER TABLE TypeOfWeaponForceInvolved ADD CONSTRAINT OffenseSegment_TypeOfWeaponForceInvolved_fk FOREIGN KEY (OffenseSegmentID) REFERENCES OffenseSegment (OffenseSegmentID);

ALTER TABLE VictimOffenseAssociation ADD CONSTRAINT OffenseSegment_VictimOffenseAssociation_fk FOREIGN KEY (OffenseSegmentID) REFERENCES OffenseSegment (OffenseSegmentID);

ALTER TABLE BiasMotivation ADD CONSTRAINT OffenseSegment_BiasMotivation_fk FOREIGN KEY (OffenseSegmentID) REFERENCES OffenseSegment (OffenseSegmentID);
