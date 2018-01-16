package com.mingdong.core.util;

import java.util.Calendar;
import java.util.Date;

public class BusinessUtils
{
    private BusinessUtils()
    {
    }

    public static long getDayDiffFromNow(Date from, Date to)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();
        if(today.before(from))
        {
            return (to.getTime() - from.getTime()) / 24 / 3600 / 1000;
        }
        else if(today.after(to))
        {
            return 0;
        }
        return (to.getTime() - today.getTime()) / 24 / 3600 / 1000;
    }
}
