package org.search.nibrs.ff_conversion.importer;

import java.io.*;
import java.util.*;

import org.search.nibrs.model.*;
import org.search.nibrs.ff_conversion.util.*;

/**
 * Builder class that constructs incidents from a stream of NIBRS report data.
 * Incidents are broadcast to listeners as events; this keeps the class as
 * memory-unintensive as possible (NIBRS report streams can be rather large).
 * <br/>At some point, if other report elements than Incidents are desired, this
 * will need to be modified. Currently, it only broadcasts Incident "add"
 * records.
 *  
 */
public class IncidentBuilder
{

    private List<IncidentListener> listeners;

    public IncidentBuilder()
    {
        listeners = new ArrayList<IncidentListener>();
    }

    public void addIncidentListener(IncidentListener listener)
    {
        listeners.add(listener);
    }

    public void removeIncidentListener(IncidentListener listener)
    {
        listeners.remove(listener);
    }

    public void buildIncidents(Reader reader) throws IOException
    {
        BufferedReader br = null;
        if (!(reader instanceof BufferedReader))
        {
            br = new BufferedReader(reader);
        }
        else
        {
            br = (BufferedReader) reader;
        }
        String line = null;
        Incident currentIncident = null;
        String currentIncidentNumber = null;
        Throwable currentThrowable = null;
        while ((line = br.readLine()) != null)
        {
            try
            {
                Segment s = new Segment(line);
                if (isIncluded(s))
                {
                    if (currentIncidentNumber == null || !currentIncidentNumber.equals(s.getIncidentNumber()))
                    {
                        if (currentThrowable == null)
                        {
                            handleNewIncident(currentIncident);
                        }
                        else
                        {
                            handleThrowable(s.getIncidentNumber(), currentThrowable);
                            currentThrowable = null;
                        }
                        currentIncidentNumber = s.getIncidentNumber();
                        currentIncident = buildIncidentSegment(s);
                    }
                    else
                    {
                        if (currentThrowable == null)
                        {
                            addSegmentToIncident(currentIncident, s);
                        }
                    }
                }
            }
            catch (Throwable t)
            {
                currentThrowable = t;
            }
        }
        if (currentThrowable == null)
        {
            handleNewIncident(currentIncident);
        }
        else
        {
            handleThrowable(currentIncidentNumber, currentThrowable);
        }
    }

    private void handleThrowable(String currentIncidentNumber, Throwable t)
    {
        for (Iterator<IncidentListener> it = listeners.iterator(); it.hasNext();)
        {
            IncidentListener listener = it.next();
            listener.handleThrowable(t, currentIncidentNumber);
        }
    }

    private final void handleNewIncident(Incident newIncident)
    {
        if (newIncident != null)
        {
            for (Iterator<IncidentListener> it = listeners.iterator(); it.hasNext();)
            {
                IncidentListener listener = it.next();
                listener.newIncident(newIncident);
            }
        }
    }

    private final Incident buildIncidentSegment(Segment s)
    {
        Incident newIncident = new Incident();
        newIncident.setIncidentNumber(s.getIncidentNumber());
        newIncident.setOri(s.getOri());
        String segmentData = s.getData();
        newIncident.setMonthOfTape(Integer.parseInt(StringUtils.getStringBetween(7, 8, segmentData)));
        newIncident.setYearOfTape(Integer.parseInt(StringUtils.getStringBetween(9, 12, segmentData)));
        newIncident.setCityIndicator(StringUtils.getStringBetween(13, 16, segmentData));
        int incidentYear = Integer.parseInt(StringUtils.getStringBetween(38, 41, segmentData));
        int incidentMonth = DateUtils.convertMonthValue(Integer.parseInt(StringUtils.getStringBetween(42, 43, segmentData)));
        int incidentDay = Integer.parseInt(StringUtils.getStringBetween(44, 45, segmentData));
        newIncident.setIncidentDate(DateUtils.makeDate(incidentYear, incidentMonth, incidentDay));
        newIncident.setReportDateIndicator(StringUtils.getStringBetween(46, 46, segmentData));
        String hourString = StringUtils.getStringBetween(47, 48, segmentData);
        if (hourString != null)
        {
            newIncident.setIncidentHour(new Integer(hourString));
        }
        newIncident.setExceptionalClearanceCode(StringUtils.getStringBetween(49, 49, segmentData));
        String clearanceYearString = StringUtils.getStringBetween(50, 53, segmentData);
        if (clearanceYearString != null)
        {
            int clearanceYear = Integer.parseInt(clearanceYearString);
            int clearanceMonth = DateUtils.convertMonthValue(Integer.parseInt(StringUtils.getStringBetween(54, 55, segmentData)));
            int clearanceDay = Integer.parseInt(StringUtils.getStringBetween(56, 57, segmentData));
            newIncident.setExceptionalClearanceDate(DateUtils.makeDate(clearanceYear, clearanceMonth, clearanceDay));
        }
        return newIncident;
    }

    private final void addSegmentToIncident(Incident currentIncident, Segment s)
    {
        char segmentType = s.getSegmentType();
        switch (segmentType)
        {
        case '2':
            currentIncident.addOffense(buildOffenseSegment(s));
            break;
        case '3':
            currentIncident.addProperty(buildPropertySegment(s));
            break;
        case '4':
            currentIncident.addVictim(buildVictimSegment(s));
            break;
        case '5':
            currentIncident.addOffender(buildOffenderSegment(s));
            break;
        case '6':
            currentIncident.addArrestee(buildArresteeSegment(s));
            break;
        default:
            throw new IllegalStateException("Unknown segment type " + segmentType);
        }
    }

    private Arrestee buildArresteeSegment(Segment s)
    {
        Arrestee newArrestee = new Arrestee();
        String segmentData = s.getData();
        newArrestee.setArresteeSequenceNumber(StringUtils.getIntegerBetween(38, 39, segmentData));
        newArrestee.setArrestTransactionNumber(StringUtils.getStringBetween(40, 51, segmentData));
        newArrestee.setArrestDate(StringUtils.getDateBetween(52, 59, segmentData));
        newArrestee.setTypeOfArrest(StringUtils.getStringBetween(60, 60, segmentData));
        newArrestee.setMultipleArresteeSegmentsIndicator(StringUtils.getStringBetween(61, 61, segmentData));
        newArrestee.setUcrArrestOffenseCode(StringUtils.getStringBetween(62, 64, segmentData));
        for (int i=0;i < 2;i++)
        {
            newArrestee.setArresteeArmedWith(i, StringUtils.getStringBetween(65 + 3*i, 66 + 3*i, segmentData));
            newArrestee.setAutomaticWeaponIndicator(i, StringUtils.getStringBetween(67 + 3*i, 67 + 3*i, segmentData));
        }
        newArrestee.setAgeOfArresteeString(StringUtils.getStringBetween(71, 74, segmentData));
        newArrestee.setSexOfArrestee(StringUtils.getStringBetween(75, 75, segmentData));
        newArrestee.setRaceOfArrestee(StringUtils.getStringBetween(76, 76, segmentData));
        newArrestee.setEthnicityOfArrestee(StringUtils.getStringBetween(77, 77, segmentData));
        newArrestee.setResidentStatusOfArrestee(StringUtils.getStringBetween(78, 78, segmentData));
        newArrestee.setDispositionOfArresteeUnder18(StringUtils.getStringBetween(79, 79, segmentData));
        return newArrestee;
    }

    private Offender buildOffenderSegment(Segment s)
    {
        Offender newOffender = new Offender();
        String segmentData = s.getData();
        newOffender.setOffenderSequenceNumber(StringUtils.getIntegerBetween(38, 39, segmentData));
        newOffender.setAgeOfOffenderString(StringUtils.getStringBetween(40, 43, segmentData));
        newOffender.setSexOfOffender(StringUtils.getStringBetween(44, 44, segmentData));
        newOffender.setRaceOfOffender(StringUtils.getStringBetween(45, 45, segmentData));
        newOffender.setEthnicityOfOffender(StringUtils.getStringBetween(46, 46, segmentData));
        return newOffender;
    }

    private Victim buildVictimSegment(Segment s)
    {
        
        Victim newVictim = new Victim();
        
        String segmentData = s.getData();
        
        newVictim.setVictimSequenceNumber(StringUtils.getIntegerBetween(38, 40, segmentData));
        
        for (int i=0;i < 10;i++)
        {
            newVictim.setUcrOffenseCodeConnection(i, StringUtils.getStringBetween(41 + 3*i, 43 + 3*i, segmentData));
            newVictim.setOffenderNumberRelated(i, StringUtils.getIntegerBetween(90 + 4*i, 91 + 4*i, segmentData));
            newVictim.setVictimOffenderRelationship(i, StringUtils.getStringBetween(92 + 4*i, 93 + 4*i, segmentData));
        }
        
        newVictim.setTypeOfVictim(StringUtils.getStringBetween(71, 71, segmentData));
        newVictim.setAgeOfVictimString(StringUtils.getStringBetween(72, 75, segmentData));
        newVictim.setSexOfVictim(StringUtils.getStringBetween(76, 76, segmentData));
        newVictim.setRaceOfVictim(StringUtils.getStringBetween(77, 77, segmentData));
        newVictim.setEthnicityOfVictim(StringUtils.getStringBetween(78, 78, segmentData));
        newVictim.setResidentStatusOfVictim(StringUtils.getStringBetween(79, 79, segmentData));
        newVictim.setAggravatedAssaultHomicideCircumstances(0, StringUtils.getStringBetween(80, 81, segmentData));
        newVictim.setAggravatedAssaultHomicideCircumstances(1, StringUtils.getStringBetween(82, 83, segmentData));
        newVictim.setAdditionalJustifiableHomicideCircumstances(StringUtils.getStringBetween(84, 84, segmentData));
        
        for (int i=0;i < 5;i++)
        {
            newVictim.setTypeOfInjury(i, StringUtils.getStringBetween(85 + i, 85 + i, segmentData));
        }
        
        newVictim.setTypeOfOfficerActivityCircumstance(StringUtils.getStringBetween(130, 131, segmentData));
        newVictim.setOfficerAssignmentType(StringUtils.getStringBetween(132, 132, segmentData));
        newVictim.setOfficerOtherJurisdictionORI(StringUtils.getStringBetween(133, 141, segmentData));
        
        return newVictim;
        
    }

    private Property buildPropertySegment(Segment s)
    {

        Property newProperty = new Property();

        String segmentData = s.getData();

        newProperty.setTypeOfPropertyLoss(StringUtils.getStringBetween(38, 38, segmentData));
        
        for (int i=0;i < 10;i++)
        {
            newProperty.setPropertyDescription(i, StringUtils.getStringBetween(39 + 19*i, 40 + 19*i, segmentData));
            newProperty.setValueOfProperty(i, StringUtils.getIntegerBetween(41 + 19*i, 49 + 19*i, segmentData));
            newProperty.setDateRecovered(i, StringUtils.getDateBetween(50 + 19*i, 57 + 19*i, segmentData));
        }
        
        newProperty.setNumberOfStolenMotorVehicles(StringUtils.getIntegerBetween(229, 230, segmentData));
        newProperty.setNumberOfRecoveredMotorVehicles(StringUtils.getIntegerBetween(231, 232, segmentData));
        
        for (int i=0;i < 3;i++)
        {
            newProperty.setSuspectedDrugType(i, StringUtils.getStringBetween(233 + 15*i, 233 + 15*i, segmentData));
            String drugQuantityWholePartString = StringUtils.getStringBetween(234 + 15*i, 242 + 15*i, segmentData);
            String drugQuantityFractionalPartString = StringUtils.getStringBetween(243 + 15*i, 245 + 15*i, segmentData);
            if (drugQuantityWholePartString != null)
            {
                String fractionalValueString = "000";
                if (drugQuantityFractionalPartString != null)
                {
                    fractionalValueString = drugQuantityFractionalPartString;
                }
                String drugQuantityFullValueString = drugQuantityWholePartString + "." + fractionalValueString;
                newProperty.setEstimatedDrugQuantity(i, new Double(drugQuantityFullValueString));
            }
            newProperty.setTypeDrugMeasurement(i, StringUtils.getStringBetween(246 + 15*i, 247 + 15*i, segmentData));
        }

        return newProperty;

    }

    private Offense buildOffenseSegment(Segment s)
    {

        Offense newOffense = new Offense();

        String segmentData = s.getData();

        newOffense.setUcrOffenseCode(StringUtils.getStringBetween(38, 40, segmentData));
        newOffense.setOffenseAttemptedCompleted(StringUtils.getStringBetween(41, 41, segmentData));
        newOffense.setLocationType(StringUtils.getStringBetween(45, 46, segmentData));
        newOffense.setNumberOfPremisesEntered(StringUtils.getIntegerBetween(47, 48, segmentData));
        newOffense.setMethodOfEntry(StringUtils.getStringBetween(49, 49, segmentData));
        
        for (int i=0; i < 5; i++) {
            newOffense.setBiasMotivation(i, StringUtils.getStringBetween(62+i, 63+i, segmentData));
        }
        
        for (int i = 0; i < 3; i++)
        {
            newOffense.setOffendersSuspectedOfUsing(i, StringUtils.getStringBetween(42 + i, 42 + i, segmentData));
            newOffense.setTypeOfCriminalActivity(i, StringUtils.getStringBetween(50 + i, 50 + i, segmentData));
            newOffense.setTypeOfWeaponForceInvolved(i, StringUtils.getStringBetween(53 + 3 * i, 54 + 3 * i, segmentData));
            newOffense.setAutomaticWeaponIndicator(i, StringUtils.getStringBetween(55 + 3 * i, 55 + 3 * i, segmentData));
        }

        return newOffense;

    }

    private final boolean isIncluded(Segment s)
    {
        // currently, we exclude all the segment types except for incident adds.
        return s.getActionType() == 'I';
    }

}
