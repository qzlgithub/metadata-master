package com.mingdong.bop.util;

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

}
