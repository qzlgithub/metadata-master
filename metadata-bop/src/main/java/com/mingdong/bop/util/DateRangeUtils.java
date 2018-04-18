package com.mingdong.bop.util;

import com.mingdong.bop.constant.TimeScope;
import com.mingdong.core.model.DateRange;

import java.util.Calendar;
import java.util.Date;

public class DateRangeUtils
{

    public static DateRange getTimeRangeByScope(TimeScope scope)
    {
        Date startDate, endDate;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(scope == TimeScope.TODAY)
        {
            startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            endDate = calendar.getTime();
        }
        else if(scope == TimeScope.YESTERDAY)
        {
            endDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            startDate = calendar.getTime();
        }
        else if(scope == TimeScope.LATEST_7_DAYS)
        {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            endDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            startDate = calendar.getTime();
        }
        else if(scope == TimeScope.LATEST_30_DAYS)
        {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            endDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            startDate = calendar.getTime();
        }
        else if(scope == TimeScope.LATEST_90_DAYS)
        {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            endDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, -90);
            startDate = calendar.getTime();
        }
        else if(scope == TimeScope.LATEST_1_YEAR)
        {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            endDate = calendar.getTime();
            calendar.add(Calendar.YEAR, -1);
            startDate = calendar.getTime();
        }
        else
        {
            return null;
        }
        return new DateRange(startDate, endDate);
    }
}
