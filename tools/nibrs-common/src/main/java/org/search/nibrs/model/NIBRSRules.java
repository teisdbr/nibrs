package org.search.nibrs.model;

/**
 * A collection of static methods that implement various NIBRS business rules.
 *
 */
public class NIBRSRules
{

    private NIBRSRules()
    {
    }

    public static final boolean weaponForceTypeCodeIsFirearm(String weaponForceTypeCode)
    {
        return "11".equals(weaponForceTypeCode) || "12".equals(weaponForceTypeCode) || "13".equals(weaponForceTypeCode) || "14".equals(weaponForceTypeCode) || "15".equals(weaponForceTypeCode);
    }

    public static final boolean weaponForceTypeCodeIsWeapon(String weaponForceInvolved)
    {
        return weaponForceInvolved != null && !("85".equals(weaponForceInvolved) || "90".equals(weaponForceInvolved) || "95".equals(weaponForceInvolved) || "99".equals(weaponForceInvolved));
    }
    
    public static final boolean propertyLossTypeIsRecovered(String propertyLossTypeCode)
    {
        return "5".equals(propertyLossTypeCode);
    }
    
    public static final boolean propertyDescriptionCodeIsDrug(String propertyDescriptionCode)
    {
        return "10".equals(propertyDescriptionCode);
    }

    public static boolean drugMeasurementIsNotReported(String typeDrugMeasurement)
    {
        return "XX".equals(typeDrugMeasurement);
    }
    
    public static boolean victimTypeCodeIsPerson(String victimTypeCode)
    {
        return "I".equals(victimTypeCode);
    }

}
