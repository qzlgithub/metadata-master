package com.mingdong.core.util;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.model.DateAxis;
import com.mingdong.core.model.DateRange;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateRangeUtils
{
    private DateRangeUtils()
    {
    }

    public static DateRange getThisDayRange()
    {
        return getLatestNDaysRange(1);
    }

    public static DateRange getWeekRangeFromMonday(Date date)
    {
        return getWeekRange(Calendar.MONDAY, date);
    }

    public static DateRange getWeekRange(int firstDayOfWeek, Date date)
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
        return new DateRange(startTime, endTime);
    }

    public static DateRange getThisMonthRange()
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
        return new DateRange(startTime, endTime);
    }

    public static DateRange getLatestNDaysRange(int n)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date endTime = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -n + 1);
        Date startTime = calendar.getTime();
        return new DateRange(startTime, endTime);
    }

    /**
     * 将指定时间段按时分割
     */
    public static List<DateAxis> getDateRangeSpiltByHour(DateRange dateRange)
    {
        List<DateAxis> list = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(dateRange.getStart());
        Calendar end = Calendar.getInstance();
        end.setTime(dateRange.getEnd());
        end.add(Calendar.DAY_OF_MONTH, 1);
        String name;
        DateRange range;
        while(start.getTime().before(end.getTime()))
        {
            Date _start = start.getTime();
            start.add(Calendar.HOUR_OF_DAY, 1);
            range = new DateRange(_start, start.getTime());
            name = DateUtils.format(start.getTime(), "yyyyMMdd-HH");
            list.add(new DateAxis(name, range));
        }
        return list;
    }

    /**
     * 将指定时间段按时分割
     */
    @SuppressWarnings("unused")
    public static List<String> getRangeSpiltByHour()
    {
        List<String> list = new ArrayList<>();
        int i = 1;
        while(i <= 24)
        {
            list.add(i + "");
            i++;
        }
        return list;
    }

    /**
     * 将指定时间段按日分割
     */
    public static List<DateAxis> getDateRangeSpiltByDay(DateRange dateRange)
    {
        List<DateAxis> list = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(dateRange.getStart());
        String name;
        DateRange range;
        while(!start.getTime().after(dateRange.getEnd()))
        {
            Date _start = start.getTime();
            name = DateUtils.format(start.getTime(), "yyyyMMdd");
            start.add(Calendar.DAY_OF_MONTH, 1);
            range = new DateRange(_start, start.getTime());
            list.add(new DateAxis(name, range));
        }
        return list;
    }

    /**
     * 将指定时间段按日分割
     */
    public static List<String> getRangeSpiltByDay(DateRange dateRange)
    {
        List<String> list = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(dateRange.getStart());
        while(!start.getTime().after(dateRange.getEnd()))
        {
            list.add(DateUtils.format(start.getTime(), DateFormat.YYYY_MM_DD_2));
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    /**
     * 将指定时间段按周分割
     */
    public static List<DateAxis> getDateRangeSpiltByWeek(DateRange dateRange)
    {
        List<DateAxis> list = new ArrayList<>();
        if(dateRange == null || dateRange.getStart() == null || dateRange.getEnd() == null)
        {
            return list;
        }
        Date start = dateRange.getStart();
        Date end = dateRange.getEnd();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        if(!start.before(end))
        {
            String name = calendar.get(Calendar.YEAR) + String.format("%02d", calendar.get(Calendar.WEEK_OF_YEAR));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            list.add(new DateAxis(name, new DateRange(start, calendar.getTime())));
            return list;
        }
        Date _end;
        while(!calendar.getTime().after(end))
        {
            if(Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK) || !calendar.getTime().before(end))
            {
                String name = calendar.get(Calendar.YEAR) + String.format("%02d", calendar.get(Calendar.WEEK_OF_YEAR));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                _end = calendar.getTime();
                list.add(new DateAxis(name, new DateRange(start, _end)));
                start = calendar.getTime();
            }
            else
            {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return list;
    }

    /**
     * 将指定时间段按周分割
     */
    public static List<String> getRangeSpiltByWeek(DateRange dateRange)
    {
        List<String> list = new ArrayList<>();
        if(dateRange == null || dateRange.getStart() == null || dateRange.getEnd() == null)
        {
            return list;
        }
        Date start = dateRange.getStart();
        Date end = dateRange.getEnd();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        if(!start.before(end))
        {
            String name = calendar.get(Calendar.YEAR) + String.format("%02d", calendar.get(Calendar.WEEK_OF_YEAR));
            list.add(name);
            return list;
        }
        while(!calendar.getTime().after(end))
        {
            if(Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK) || !calendar.getTime().before(end))
            {
                String name = calendar.get(Calendar.YEAR) + String.format("%02d", calendar.get(Calendar.WEEK_OF_YEAR));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                list.add(name);
            }
            else
            {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return list;
    }

    /**
     * 将指定时间段按月分割
     */
    public static List<DateAxis> getDateRangeSpiltByMonth(DateRange dateRange)
    {
        List<DateAxis> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRange.getStart());
        while(!calendar.getTime().after(dateRange.getEnd()))
        {
            Date _start = calendar.getTime();
            Date _end;
            String name = DateUtils.format(_start, "yyyyMM");
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            if(!_start.before(dateRange.getEnd()))
            {
                Calendar end = Calendar.getInstance();
                end.setTime(dateRange.getEnd());
                end.add(Calendar.DAY_OF_MONTH, 1);
                _end = end.getTime();
            }
            else
            {
                _end = calendar.getTime();
            }
            list.add(new DateAxis(name, new DateRange(_start, _end)));
        }
        return list;
    }

    /**
     * 将指定时间段按月分割
     */
    public static List<String> getRangeSpiltByMonth(DateRange dateRange)
    {
        List<String> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRange.getStart());
        while(!calendar.getTime().after(dateRange.getEnd()))
        {
            String name = DateUtils.format(calendar.getTime(), "yyyy/MM");
            list.add(name);
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public static List<String> getRangeSpilt(DateRange range, RangeUnit rangeUnit)
    {
        switch(rangeUnit)
        {
            case DAY:
                return getRangeSpiltByDay(range);
            case WEEK:
                return getRangeSpiltByWeek(range);
            case MONTH:
                return getRangeSpiltByMonth(range);
            default:
                return getRangeSpiltByHour();
        }
    }

    public static void main(String[] args) throws ParseException
    {
        /*Date start = DateUtils.parseToDate("2018-03-11", DateFormat.YYYY_MM_DD);
        Date end = DateUtils.parseToDate("2018-05-01", DateFormat.YYYY_MM_DD);

        // List<DateAxis> list = getDateRangeSpiltByHour(new DateRange(start, end));
        // List<DateAxis> list = getDateRangeSpiltByDay(new DateRange(start, end));
        // List<DateAxis> list = getDateRangeSpiltByWeek(new DateRange(start, end));
        List<DateAxis> list = getDateRangeSpiltByMonth(new DateRange(start, end));
        for(DateAxis o : list)
        {
            System.out.println(o.getName() + " : " + o.getRange());
        }*/
        /*String template =
                "insert into stats (create_time, update_time, client_increment, client_request, client_recharge," +
                        " stats_date, stats_year, stats_month, stats_week, stats_day, stats_hour) values (\'%s\', \'%s\', %d, %d, %d," +
                        " \'%s\', %d, %d, %d, %d, %d);";
        Random random = new Random();
        Date start = DateUtils.parseToDate("2017-11-01", DateFormat.YYYY_MM_DD);
        Date end = DateUtils.parseToDate("2018-04-01", DateFormat.YYYY_MM_DD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while(calendar.getTime().before(end))
        {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            int year, month, week, day;
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            String date = DateUtils.format(calendar.getTime(), DateFormat.YYYY_MM_DD_HH_MM_SS);
            String date1;
            if(hour == 0)
            {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(calendar.getTime());
                calendar1.add(Calendar.HOUR_OF_DAY, -1);
                date1 = DateUtils.format(calendar1.getTime(), DateFormat.YYYY_MM_DD);
                year = calendar1.get(Calendar.YEAR);
                month = calendar1.get(Calendar.MONTH) + 1;
                week = calendar1.get(Calendar.WEEK_OF_YEAR);
                day = calendar1.get(Calendar.DAY_OF_MONTH);
                hour = 24;
            }
            else
            {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                week = calendar.get(Calendar.WEEK_OF_YEAR);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                date1 = DateUtils.format(calendar.getTime(), DateFormat.YYYY_MM_DD);
            }
            System.out.println(String.format(template, date, date, random.nextInt(10), random.nextInt(1000000),
                    random.nextInt(100000), date1, year, month, week, day, hour));
        }*/
        System.out.println(getLatestNDaysRange(3));
    }
}
