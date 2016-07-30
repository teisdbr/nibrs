package org.search.nibrs.model;

import java.util.Arrays;
import java.util.Date;

/**
 * Representation of an article of property reported within an Incident in a NIBRS report.
 *
 */
public class Property
{
    
	private String typeOfPropertyLoss;
    private String[] propertyDescription;
    private Integer[] valueOfProperty;
    private Date[] dateRecovered;
    private Integer numberOfStolenMotorVehicles;
    private Integer numberOfRecoveredMotorVehicles;
    private String[] suspectedDrugType;
    private Double[] estimatedDrugQuantity;
    private String[] typeDrugMeasurement;
    private int populatedPropertyDescriptionCount;
    private int populatedSuspectedDrugTypeCount;

    public Property()
    {
        propertyDescription = new String[10];
        valueOfProperty = new Integer[10];
        dateRecovered = new Date[10];
        suspectedDrugType = new String[3];
        estimatedDrugQuantity = new Double[3];
        typeDrugMeasurement = new String[3];
    }
    
    public Property(Property p) {
    	this();
    	typeOfPropertyLoss = p.typeOfPropertyLoss;
    	numberOfRecoveredMotorVehicles = p.numberOfRecoveredMotorVehicles;
    	numberOfStolenMotorVehicles = p.numberOfStolenMotorVehicles;
    	populatedPropertyDescriptionCount = p.populatedPropertyDescriptionCount;
    	populatedSuspectedDrugTypeCount = p.populatedSuspectedDrugTypeCount;
    	propertyDescription = CopyUtils.copyArray(p.propertyDescription);
    	valueOfProperty = CopyUtils.copyArray(p.valueOfProperty);
    	dateRecovered = CopyUtils.copyArray(p.dateRecovered);
    	suspectedDrugType = CopyUtils.copyArray(p.suspectedDrugType);
    	estimatedDrugQuantity = CopyUtils.copyArray(p.estimatedDrugQuantity);
    	typeDrugMeasurement = CopyUtils.copyArray(p.typeDrugMeasurement);
    }
    
    public int getPopulatedSuspectedDrugTypeCount() {
    	return populatedSuspectedDrugTypeCount;
    }
    
    public int getPopulatedPropertyDescriptionCount() {
    	return populatedPropertyDescriptionCount;
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
        populatedSuspectedDrugTypeCount = Math.max(populatedSuspectedDrugTypeCount, position+1);
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
        populatedPropertyDescriptionCount = Math.max(populatedPropertyDescriptionCount, position+1);
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

	@Override
	public String toString() {
		return "Property [typeOfPropertyLoss=" + typeOfPropertyLoss + ", propertyDescription=" + Arrays.toString(propertyDescription) + ", valueOfProperty=" + Arrays.toString(valueOfProperty)
				+ ", dateRecovered=" + Arrays.toString(dateRecovered) + ", numberOfStolenMotorVehicles=" + numberOfStolenMotorVehicles + ", numberOfRecoveredMotorVehicles="
				+ numberOfRecoveredMotorVehicles + ", suspectedDrugType=" + Arrays.toString(suspectedDrugType) + ", estimatedDrugQuantity=" + Arrays.toString(estimatedDrugQuantity)
				+ ", typeDrugMeasurement=" + Arrays.toString(typeDrugMeasurement) + ", populatedPropertyDescriptionCount=" + populatedPropertyDescriptionCount + ", populatedSuspectedDrugTypeCount="
				+ populatedSuspectedDrugTypeCount + "]";
	}
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(dateRecovered);
		result = prime * result + Arrays.hashCode(estimatedDrugQuantity);
		result = prime * result + ((numberOfRecoveredMotorVehicles == null) ? 0 : numberOfRecoveredMotorVehicles.hashCode());
		result = prime * result + ((numberOfStolenMotorVehicles == null) ? 0 : numberOfStolenMotorVehicles.hashCode());
		result = prime * result + populatedPropertyDescriptionCount;
		result = prime * result + populatedSuspectedDrugTypeCount;
		result = prime * result + Arrays.hashCode(propertyDescription);
		result = prime * result + Arrays.hashCode(suspectedDrugType);
		result = prime * result + Arrays.hashCode(typeDrugMeasurement);
		result = prime * result + ((typeOfPropertyLoss == null) ? 0 : typeOfPropertyLoss.hashCode());
		result = prime * result + Arrays.hashCode(valueOfProperty);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.hashCode() == hashCode();
	}

}
