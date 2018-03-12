package org.search.nibrs.stagingdata.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author lillian.kim@mark43.com
 * @since 2/26/18
 *
 * A set of utility methods that deal with Dates.
 */
public class DateUtils
{
    public static Date asDate(LocalDate localDate) {
        return localDate == null ? null : Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}