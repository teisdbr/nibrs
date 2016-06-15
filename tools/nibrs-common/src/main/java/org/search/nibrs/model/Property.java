package org.search.nibrs.model;

import java.io.Serializable;
import java.util.*;

/**
 * Representation of an article of property reported within an Incident in a NIBRS report.
 *
 */
public class Property implements Serializable
{
    
	private static final long serialVersionUID = -6949610444314905319L;
	
	private String typeOfPropertyLoss;
    private String[] propertyDescription;
    private Integer[] valueOfProperty;
    private Date[] dateRecovered;
    private Integer numberOfStolenMotorVehicles;
    private Integer numberOfRecoveredMotorVehicles;
    private String[] suspectedDrugType;
    private Double[] estimatedDrugQuantity;
    private String[] typeDrugMeasurement;

    public Property()
    {
        propertyDescription = new String[10];
        valueOfProperty = new Integer[10];
        dateRecovered = new Date[10];
        suspectedDrugType = new String[3];
        estimatedDrugQuantity = new Double[3];
        typeDrugMeasurement = new String[3];
    }
    
    public Double getEstimatedDrugQuantity(int position)
    {
        return estimatedDrugQuantity[position];
    }
    
    public void setEstimatedDrugQuantity(int position, Double value)
    {
        estimatedDrugQuantity[position] = value;
    }

    public String getTypeDrugMeasurement(int position)
    {
        return typeDrugMeasurement[position];
    }
    
    public void setTypeDrugMeasurement(int position, String value)
    {
        typeDrugMeasurement[position] = value;
    }

    public String getSuspectedDrugType(int position)
    {
        return suspectedDrugType[position];
    }
    
    public void setSuspectedDrugType(int position, String value)
    {
        suspectedDrugType[position] = value;
    }

    public Date getDateRecovered(int position)
    {
        return dateRecovered[position];
    }
    
    public void setDateRecovered(int position, Date value)
    {
        dateRecovered[position] = value;
    }

    public Integer getValueOfProperty(int position)
    {
        return valueOfProperty[position];
    }
    
    public void setValueOfProperty(int position, Integer value)
    {
        valueOfProperty[position] = value;
    }

    public String getPropertyDescription(int position)
    {
        return propertyDescription[position];
    }
    
    public void setPropertyDescription(int position, String value)
    {
        propertyDescription[position] = value;
    }

    public Integer getNumberOfRecoveredMotorVehicles()
    {
        return numberOfRecoveredMotorVehicles;
    }
    public void setNumberOfRecoveredMotorVehicles(Integer numberOfRecoveredMotorVehicles)
    {
        this.numberOfRecoveredMotorVehicles = numberOfRecoveredMotorVehicles;
    }
    public Integer getNumberOfStolenMotorVehicles()
    {
        return numberOfStolenMotorVehicles;
    }
    public void setNumberOfStolenMotorVehicles(Integer numberOfStolenMotorVehicles)
    {
        this.numberOfStolenMotorVehicles = numberOfStolenMotorVehicles;
    }
    public String getTypeOfPropertyLoss()
    {
        return typeOfPropertyLoss;
    }
    public void setTypeOfPropertyLoss(String typeOfPropertyLoss)
    {
        this.typeOfPropertyLoss = typeOfPropertyLoss;
    }
    
    public boolean isLossTypeRecovered()
    {
        return NIBRSRules.propertyLossTypeIsRecovered(getTypeOfPropertyLoss());
    }
    
    public boolean isPropertyTypeDrug(int position)
    {
        return NIBRSRules.propertyDescriptionCodeIsDrug(getPropertyDescription(position));
    }

    public boolean drugMeasurementIsNotReported(int position)
    {
        return NIBRSRules.drugMeasurementIsNotReported(getTypeDrugMeasurement(position));
    }
    
}
