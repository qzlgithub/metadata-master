package com.mingdong.core.model;

import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class TimeRange implements Serializable
{
    private Date startTime;
    private Date endTime;

    public TimeRange()
    {
    }

    public TimeRange(Date startTime, Date endTime)
    {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    @Override
    public String toString()
    {
        return DateUtils.format(startTime, DateFormat.YYYY_MM_DD_HH_MM_SS) + " - " + DateUtils.format(endTime,
                DateFormat.YYYY_MM_DD_HH_MM_SS);
    }
}
