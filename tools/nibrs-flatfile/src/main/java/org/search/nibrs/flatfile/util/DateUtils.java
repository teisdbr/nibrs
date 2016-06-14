package org.search.nibrs.flatfile.util;

import java.util.*;

/**
 * A set of utility methods that deal with Dates.
 *
 */
public class DateUtils
{
    
    private static final int[] MONTHS = new int[]
    {
            Calendar.JANUARY,
            Calendar.FEBRUARY,
            Calendar.MARCH,
            Calendar.APRIL,
            Calendar.MAY,
            Calendar.JUNE,
            Calendar.JULY,
            Calendar.AUGUST,
            Calendar.SEPTEMBER,
            Calendar.OCTOBER,
            Calendar.NOVEMBER,
            Calendar.DECEMBER
    };
    
    /**
     * Convenience utility method that creates a Date object properly; the object represents the start of the date
     * represented by the specified year, month, and day.
     * @return the corresponding Date object
     */
    public static final Date makeDate(int year, int month, int day)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Converts month values of the form Jan=1, Feb=2, ..., Dec=12 to the Java Calendar values.
     * @param monthValue the ordinary month value
     * @return the value used by the Java Calendar class
     */
    public static int convertMonthValue(int monthValue)
    {
        return MONTHS[monthValue-1];
    }
    
}
