package org.search.nibrs.flatfile.util;

import java.util.Date;

/**
 * A class of String utilities
 */
public final class StringUtils
{

    /**
     * Get the substring that appears in the specified string, between the 1-based beginning and ending positions.  (This is 1-based
     * to make it easy to write code to correspond to the NIBRS spex, which are also 1-based.)
     * @param begin the 1-based beginning position
     * @param end the 1-based ending position
     * @param s the string to subset
     * @return the substring
     */
    public static final String getStringBetween(int begin, int end, String s)
    {
        if (s == null || s.length() == 0)
        {
            return null;
        }
        String substring = s.substring(begin-1, end).trim();
        return substring.length() == 0 ? null : substring;
    }

    /**
     * Get the integer value represented by the substring between the beginning and ending positions.  (Same rules as for
     * getStringBetween(int, int, String) apply.)
     */
    public static final Integer getIntegerBetween(int begin, int end, String s)
    {
        String substring = getStringBetween(begin, end, s);
        return substring == null ? null : new Integer(substring);
    }

    /**
     * Get the date value represented by the substring between the beginning and ending positions.  (Same rules as for
     * getStringBetween(int, int, String) apply.)
     */
    public static final Date getDateBetween(int begin, int end, String s)
    {
        String substring = getStringBetween(begin, end, s);
        if (substring == null) return null;
        int year = Integer.parseInt(getStringBetween(1, 4, substring));
        int month = Integer.parseInt(getStringBetween(5, 6, substring));
        int day = Integer.parseInt(getStringBetween(7, 8, substring));
        return DateUtils.makeDate(year, DateUtils.convertMonthValue(month), day);
    }

}
