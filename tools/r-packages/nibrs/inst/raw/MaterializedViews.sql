drop table if exists FullVictimOffenseView;

create table FullVictimOffenseView as
select
	AdministrativeSegment.AdministrativeSegmentID,
	AdministrativeSegment.ORI,
	AdministrativeSegment.IncidentNumber,
	AdministrativeSegment.IncidentHour,
	AdministrativeSegment.ClearedExceptionallyTypeID,
	AdministrativeSegment.IncidentDate,
	AdministrativeSegment.IncidentDateID,
	AdministrativeSegment.AgencyID,
	OffenseSegment.OffenseAttemptedCompleted,
	OffenseSegment.LocationTypeTypeID,
	OffenseSegment.NumberOfPremisesEntered,
	convert(ifnull(OffenseSegment.NumberOfPremisesEntered, 'N/A'), char(4)) as NumberOfPremisesEnteredDim,
	OffenseSegment.MethodOfEntryTypeID,
	OffenseSegment.BiasMotivationTypeID,
	OffenseSegment.UCROffenseCodeTypeID,
	OffenseSegment.OffenseSegmentID,
	VictimSegment.TypeOfVictimTypeID,
	VictimSegment.OfficerActivityCircumstanceTypeID,
	VictimSegment.OfficerAssignmentTypeTypeID,
	VictimSegment.SexOfPersonTypeID,
	VictimSegment.RaceOfPersonTypeID,
	VictimSegment.EthnicityOfPersonTypeID,
	VictimSegment.ResidentStatusOfPersonTypeID,
	VictimSegment.AdditionalJustifiableHomicideCircumstancesTypeID,
	VictimSegment.AgeOfVictimMin,
	VictimSegment.AgeOfVictimMax,
	VictimSegment.AgeNeonateIndicator,
	VictimSegment.AgeFirstWeekIndicator,
	VictimSegment.AgeFirstYearIndicator,
	(
    CASE 
        WHEN VictimSegment.AgeNeonateIndicator = 1 THEN '< 24 hours'
        WHEN VictimSegment.AgeFirstWeekIndicator = 1 THEN '< 1 week'
        WHEN VictimSegment.AgeFirstYearIndicator = 1 THEN '< 1 year'
        WHEN VictimSegment.AgeOfVictimMin = 99 THEN '> 98 years'
        WHEN VictimSegment.AgeOfVictimMin IS NULL THEN 'N/A'
        ELSE convert(VictimSegment.AgeOfVictimMin, char(12))
    END) AS VictimAgeDim,
	VictimSegment.VictimSegmentID,
	ifnull(AggravatedAssaultHomicideCircumstances.AggravatedAssaultHomicideCircumstancesTypeID, 98) as AggravatedAssaultHomicideCircumstancesTypeID,
	ifnull(OffenderSuspectedOfUsing.OffenderSuspectedOfUsingTypeID, 9) as OffenderSuspectedOfUsingTypeID,
	ifnull(TypeCriminalActivity.TypeOfCriminalActivityTypeID, 7) as TypeOfCriminalActivityTypeID,
	ifnull(TypeOfWeaponForceInvolved.TypeOfWeaponForceInvolvedTypeID, 99) as TypeOfWeaponForceInvolvedTypeID,
	ifnull(TypeOfWeaponForceInvolved.AutomaticWeaponIndicator, 'N') as AutomaticWeaponIndicator,
	ifnull(TypeInjury.TypeInjuryTypeID, 1) as TypeInjuryTypeID
from
	AdministrativeSegment left join OffenseSegment on AdministrativeSegment.AdministrativeSegmentID=OffenseSegment.AdministrativeSegmentID
	left join VictimOffenseAssociation on VictimOffenseAssociation.OffenseSegmentID=OffenseSegment.OffenseSegmentID
	left join VictimSegment on VictimSegment.VictimSegmentID=VictimOffenseAssociation.VictimSegmentID
	left join OffenderSuspectedOfUsing on OffenseSegment.OffenseSegmentID=OffenderSuspectedOfUsing.OffenseSegmentID
	left join TypeCriminalActivity on OffenseSegment.OffenseSegmentID=TypeCriminalActivity.OffenseSegmentID
	left join TypeOfWeaponForceInvolved on OffenseSegment.OffenseSegmentID=TypeOfWeaponForceInvolved.OffenseSegmentID
	left join TypeInjury on VictimSegment.VictimSegmentID=TypeInjury.VictimSegmentID
	left join AggravatedAssaultHomicideCircumstances on VictimSegment.VictimSegmentID=AggravatedAssaultHomicideCircumstances.VictimSegmentID;


drop table if exists FullVictimOffenderView;

create table FullVictimOffenderView as
select
	AdministrativeSegment.AdministrativeSegmentID,
	AdministrativeSegment.ORI,
	AdministrativeSegment.IncidentNumber,
	AdministrativeSegment.IncidentHour,
	AdministrativeSegment.ClearedExceptionallyTypeID,
	AdministrativeSegment.IncidentDate,
	AdministrativeSegment.IncidentDateID,
	AdministrativeSegment.AgencyID,
	OffenderSegment.OffenderSequenceNumber,
	OffenderSegment.AgeOfOffenderMin,
	OffenderSegment.AgeOfOffenderMax,
	(
    CASE 
        WHEN OffenderSegment.AgeOfOffenderMin = 99 THEN '> 98 years'
        WHEN OffenderSegment.AgeOfOffenderMin IS NULL THEN 'N/A'
        ELSE convert(OffenderSegment.AgeOfOffenderMin, char(12))
    END) AS OffenderAgeDim,
	OffenderSegment.SexOfPersonTypeID as OffenderSexOfPersonTypeID,
	OffenderSegment.RaceOfPersonTypeID as OffenderRaceOfPersonTypeID,
	OffenderSegment.EthnicityOfPersonTypeID as OffenderEthnicityOfPersonTypeID,
	OffenderSegment.OffenderSegmentID,
	VictimSegment.VictimSequenceNumber,
	VictimSegment.TypeOfVictimTypeID,
	VictimSegment.OfficerActivityCircumstanceTypeID,
	VictimSegment.OfficerAssignmentTypeTypeID,
	VictimSegment.SexOfPersonTypeID as VictimSexOfPersonTypeID,
	VictimSegment.RaceOfPersonTypeID as VictimRaceOfPersonTypeID,
	VictimSegment.EthnicityOfPersonTypeID as VictimEthnicityOfPersonTypeID,
	VictimSegment.ResidentStatusOfPersonTypeID as VictimResidentStatusOfPersonTypeID,
	VictimSegment.AdditionalJustifiableHomicideCircumstancesTypeID,
	VictimSegment.AgeOfVictimMin,
	VictimSegment.AgeOfVictimMax,
	VictimSegment.AgeNeonateIndicator,
	VictimSegment.AgeFirstWeekIndicator,
	VictimSegment.AgeFirstYearIndicator,
	(
    CASE 
        WHEN VictimSegment.AgeNeonateIndicator = 1 THEN '< 24 hours'
        WHEN VictimSegment.AgeFirstWeekIndicator = 1 THEN '< 1 week'
        WHEN VictimSegment.AgeFirstYearIndicator = 1 THEN '< 1 year'
        WHEN VictimSegment.AgeOfVictimMin = 99 THEN '> 98 years'
        WHEN VictimSegment.AgeOfVictimMin IS NULL THEN 'N/A'
        ELSE convert(VictimSegment.AgeOfVictimMin, char(12))
    END) AS VictimAgeDim,
    VictimSegment.VictimSegmentID,
	VictimOffenderAssociation.VictimOffenderRelationshipTypeID,
	ifnull(AggravatedAssaultHomicideCircumstances.AggravatedAssaultHomicideCircumstancesTypeID, 98) as AggravatedAssaultHomicideCircumstancesTypeID,
	ifnull(TypeInjury.TypeInjuryTypeID, 1) as TypeInjuryTypeID
from
	AdministrativeSegment left join OffenderSegment on AdministrativeSegment.AdministrativeSegmentID=OffenderSegment.AdministrativeSegmentID
	left join VictimOffenderAssociation on VictimOffenderAssociation.OffenderSegmentID=OffenderSegment.OffenderSegmentID
	left join VictimSegment on VictimSegment.VictimSegmentID=VictimOffenderAssociation.VictimSegmentID
	left join TypeInjury on VictimSegment.VictimSegmentID=TypeInjury.VictimSegmentID
	left join AggravatedAssaultHomicideCircumstances on VictimSegment.VictimSegmentID=AggravatedAssaultHomicideCircumstances.VictimSegmentID;

drop table if exists FullGroupAArrestView;

create table FullGroupAArrestView as
select
	AdministrativeSegment.AdministrativeSegmentID,
	AdministrativeSegment.ORI,
	AdministrativeSegment.IncidentNumber,
	AdministrativeSegment.IncidentHour,
	AdministrativeSegment.ClearedExceptionallyTypeID,
	AdministrativeSegment.IncidentDate,
	AdministrativeSegment.IncidentDateID,
	AdministrativeSegment.AgencyID,
	ArresteeSegment.ArresteeSequenceNumber,
	ArresteeSegment.ArrestTransactionNumber,
	ArresteeSegment.ArrestDate,
	ArresteeSegment.TypeOfArrestTypeID,
	ArresteeSegment.MultipleArresteeSegmentsIndicatorTypeID,
	ArresteeSegment.AgeOfArresteeMin,
	ArresteeSegment.AgeOfArresteeMax,
	(
    CASE 
        WHEN ArresteeSegment.AgeOfArresteeMin = 99 THEN '> 98 years'
        WHEN ArresteeSegment.AgeOfArresteeMin IS NULL THEN 'N/A'
        ELSE convert(ArresteeSegment.AgeOfArresteeMin, char(12))
    END) AS ArresteeAgeDim,
	ArresteeSegment.SexOfPersonTypeID,
	ArresteeSegment.RaceOfPersonTypeID,
	ArresteeSegment.EthnicityOfPersonTypeID,
	ArresteeSegment.ResidentStatusOfPersonTypeID,
	ArresteeSegment.DispositionOfArresteeUnder18TypeID,
	ArresteeSegment.UCROffenseCodeTypeID,
	ifnull(ArresteeSegment.ArrestDateID, 99998) as ArrestDateID,
	ArresteeSegment.ArresteeSegmentID,
	ifnull(ArresteeSegmentWasArmedWith.ArresteeWasArmedWithTypeID, 1) as ArresteeWasArmedWithTypeID,
	ifnull(ArresteeSegmentWasArmedWith.AutomaticWeaponIndicator, 'N') as AutomaticWeaponIndicator
from
	AdministrativeSegment inner join ArresteeSegment on AdministrativeSegment.AdministrativeSegmentID=ArresteeSegment.AdministrativeSegmentID
	left join ArresteeSegmentWasArmedWith on ArresteeSegment.ArresteeSegmentID=ArresteeSegmentWasArmedWith.ArresteeSegmentID;

drop table if exists FullPropertyView;

create table FullPropertyView as
select
	AdministrativeSegment.AdministrativeSegmentID,
	AdministrativeSegment.ORI,
	AdministrativeSegment.IncidentNumber,
	AdministrativeSegment.IncidentHour,
	AdministrativeSegment.ClearedExceptionallyTypeID,
	AdministrativeSegment.IncidentDate,
	AdministrativeSegment.IncidentDateID,
	AdministrativeSegment.AgencyID,
	ifnull(PropertySegment.TypePropertyLossEtcTypeID, 1) as TypePropertyLossEtcTypeID,
	ifnull(PropertySegment.NumberOfStolenMotorVehicles, 0) as NumberOfStolenMotorVehicles,
	ifnull(PropertySegment.NumberOfRecoveredMotorVehicles, 0) as NumberOfRecoveredMotorVehicles,
	PropertySegment.PropertySegmentID,
	ifnull(PropertyType.PropertyDescriptionTypeID, 98) as PropertyDescriptionTypeID,
	ifnull(PropertyType.ValueOfProperty, 0) as ValueOfProperty,
	PropertyType.RecoveredDate,
	ifnull(PropertyType.RecoveredDateID, 99998) as RecoveredDateID,
	SuspectedDrugType.SuspectedDrugTypeID,
	ifnull(SuspectedDrugType.SuspectedDrugTypeTypeID, 98) as SuspectedDrugTypeTypeID,
	ifnull(SuspectedDrugType.TypeDrugMeasurementTypeID,  98) as TypeDrugMeasurementTypeID,
	ifnull(SuspectedDrugType.EstimatedDrugQuantity, 0) as EstimatedDrugQuantity
from
	AdministrativeSegment inner join PropertySegment on AdministrativeSegment.AdministrativeSegmentID=PropertySegment.AdministrativeSegmentID
	left join PropertyType on PropertySegment.PropertySegmentID=PropertyType.PropertySegmentID
	left join SuspectedDrugType on PropertySegment.PropertySegmentID=SuspectedDrugType.PropertySegmentID;

drop table if exists FullGroupBArrestView;

create table FullGroupBArrestView as
select
	ArrestReportSegment.ArresteeSequenceNumber,
	ArrestReportSegment.ArrestTransactionNumber,
	ArrestReportSegment.ArrestDate,
	ArrestReportSegment.TypeOfArrestTypeID,
	ArrestReportSegment.UCROffenseCodeTypeID,
	ArrestReportSegment.AgeOfArresteeMin,
	ArrestReportSegment.AgeOfArresteeMax,
	(
    CASE 
        WHEN ArrestReportSegment.AgeOfArresteeMin = 99 THEN '> 98 years'
        WHEN ArrestReportSegment.AgeOfArresteeMin IS NULL THEN 'N/A'
        ELSE convert(ArrestReportSegment.AgeOfArresteeMin, char(12))
    END) AS ArresteeAgeDim,
	ArrestReportSegment.SexOfPersonTypeID,
	ArrestReportSegment.RaceOfPersonTypeID,
	ArrestReportSegment.EthnicityOfPersonTypeID,
	ArrestReportSegment.ResidentStatusOfPersonTypeID,
	ArrestReportSegment.DispositionOfArresteeUnder18TypeID,
	ArrestReportSegment.ORI,
	ArrestReportSegment.ArrestReportSegmentID,
	ifnull(ArrestReportSegment.ArrestDateID, 99998) as ArrestDateID,
	ArrestReportSegment.AgencyID,
	ifnull(ArrestReportSegmentWasArmedWith.ArresteeWasArmedWithTypeID, 1) as ArresteeWasArmedWithTypeID,
	ifnull(ArrestReportSegmentWasArmedWith.AutomaticWeaponIndicator, 'N') as AutomaticWeaponIndicator
from
	ArrestReportSegment
	left join ArrestReportSegmentWasArmedWith on ArrestReportSegment.ArrestReportSegmentID=ArrestReportSegmentWasArmedWith.ArrestReportSegmentID;
