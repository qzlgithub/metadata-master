package com.mingdong.core.model;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class DateRange implements Serializable
{
    private Date start;
    private Date end;

    public DateRange()
    {
    }

    public DateRange(Date start, Date end)
    {
        this.start = start;
        this.end = end;
    }

    public Date getStart()
    {
        return start;
    }

    public void setStart(Date start)
    {
        this.start = start;
    }

    public Date getEnd()
    {
        return end;
    }

    public void setEnd(Date end)
    {
        this.end = end;
    }

    @Override
    public String toString()
    {
        return "[" + DateUtils.format(start, DateFormat.YYYY_MM_DD_HH_MM_SS) + ", " + DateUtils.format(end,
                DateFormat.YYYY_MM_DD_HH_MM_SS) + ")";
    }
}
