package com.mingdong.core.util;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
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

    public static long getDayDiff(Date from, Date to)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTime(from);
        Date before = calendar.getTime();
        calendar.setTime(to);
        Date after = calendar.getTime();
        return (after.getTime() - before.getTime()) / 24 / 3600 / 1000;
    }

    public static Date getDayStartTime(Date date)
    {
        if(date != null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }
        return null;
    }

    /**
     * 获取指定时间第二天的零点时间
     */
    public static Date getLastDayStartTime(Date date)
    {
        if(date != null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }
        return null;
    }

    public static boolean checkAccountValid(Date startDate, Date endDate)
    {
        Date now = new Date();
        try
        {
            String start = DateUtils.format(startDate, DateFormat.YYYY_MM_DD);
            startDate = DateUtils.parseToDate(start, DateFormat.YYYY_MM_DD);
            if(now.before(startDate))
            {
                return false;
            }
            String end = DateUtils.format(endDate, DateFormat.YYYY_MM_DD);
            endDate = DateUtils.parseToDate(end + " 23:59:59", DateFormat.YYYY_MM_DD_HH_MM_SS);
            return !now.after(endDate);
        }
        catch(ParseException e)
        {
            return false;
        }
    }

    public static boolean checkAccountValid(BigDecimal unitAmt, BigDecimal balance)
    {
        return unitAmt != null && balance != null && balance.doubleValue() >= unitAmt.doubleValue();
    }

    /**
     * 创建一个全局唯一的用户接入凭证
     */
    public static String createAccessToken()
    {
        String s1 = StringUtils.getRandomString(32);
        String s2 = StringUtils.getUuid();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 32; i++)
        {
            sb.append(s1.charAt(i)).append(s2.charAt(i));
        }
        return sb.toString();
    }

    public static Date getTokenValidTime(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }
}
