package com.mingdong.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCalculateUtils
{
    /**
     * 获取处理后的日期 yyyy-MM-dd 00:00:00
     *
     * @param date
     * @return
     */
    public static Date getCurrentDate(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取几天前的日期
     *
     * @param date
     * @param beforeNumber 几天
     * @param isDealAfter  是否处理为00:00:00
     * @return
     */
    public static Date getBeforeDayDate(Date date, int beforeNumber, boolean isDealAfter)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(isDealAfter)
        {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0,
                    0);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -beforeNumber);
        return calendar.getTime();
    }

    /**
     * 获取当前月第一天
     *
     * @param date
     * @param isDealAfter 是否处理为00:00:00
     * @return
     */
    public static Date getCurrentMonthFirst(Date date, boolean isDealAfter)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        if(isDealAfter)
        {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0,
                    0);
        }
        return calendar.getTime();
    }

    /**
     * 获取当前季度第一天日期
     *
     * @param date
     * @param isDealAfter 是否处理为00:00:00
     * @return
     */
    public static Date getCurrentQuarterFirstDate(Date date, boolean isDealAfter)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try
        {
            if(currentMonth >= 1 && currentMonth <= 3)
            {
                c.set(Calendar.MONTH, 0);
            }
            else if(currentMonth >= 4 && currentMonth <= 6)
            {
                c.set(Calendar.MONTH, 2);
            }
            else if(currentMonth >= 7 && currentMonth <= 9)
            {
                c.set(Calendar.MONTH, 3);
            }
            else if(currentMonth >= 10 && currentMonth <= 12)
            {
                c.set(Calendar.MONTH, 8);
            }
            c.set(Calendar.DATE, 1);
            if(isDealAfter)
            {
                SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
            }
            else
            {
                now = c.getTime();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 两个日期之间相差的天数，不考虑时分秒
     */
    public static int getBetweenDayDif(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for(int i = year1; i < year2; i++)
            {
                if(i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        }
        else    //不同年
        {
            return Math.abs(day2 - day1);
        }
    }

}
