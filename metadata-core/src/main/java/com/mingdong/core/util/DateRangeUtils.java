package com.mingdong.core.util;

import com.mingdong.core.model.TimeRange;

import java.util.Calendar;
import java.util.Date;

public class DateRangeUtils
{
    private DateRangeUtils()
    {
    }

    public static TimeRange getThisDayRange()
    {
        return getLatestNDaysRange(1);
    }

    public static TimeRange getWeekRangeFromMonday(Date date)
    {
        return getWeekRange(Calendar.MONDAY, date);
    }

    public static TimeRange getWeekRange(int firstDayOfWeek, Date date)
    {
        if(firstDayOfWeek > Calendar.SATURDAY || firstDayOfWeek < Calendar.SUNDAY)
        {
            firstDayOfWeek = Calendar.SUNDAY;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int diff = weekday - firstDayOfWeek;
        calendar.add(Calendar.DAY_OF_MONTH, -(diff >= 0 ? diff : 7 + diff));
        Date startTime = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date endTime = calendar.getTime();
        return new TimeRange(startTime, endTime);
    }

    public static TimeRange getThisMonthRange()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startTime = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date endTime = calendar.getTime();
        return new TimeRange(startTime, endTime);
    }

    public static TimeRange getLatestNDaysRange(int n)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endTime = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -n);
        Date startTime = calendar.getTime();
        return new TimeRange(startTime, endTime);
    }
}
