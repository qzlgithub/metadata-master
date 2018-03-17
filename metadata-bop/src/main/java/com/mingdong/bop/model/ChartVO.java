package com.mingdong.bop.model;

import java.util.Date;

public class ChartVO
{
    private Integer days;
    private Integer unit;
    private Date startDate;
    private Date endDate;
    private Date compareFrom;

    public Integer getDays()
    {
        return days;
    }

    public void setDays(Integer days)
    {
        this.days = days;
    }

    public Integer getUnit()
    {
        return unit;
    }

    public void setUnit(Integer unit)
    {
        this.unit = unit;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getCompareFrom()
    {
        return compareFrom;
    }

    public void setCompareFrom(Date compareFrom)
    {
        this.compareFrom = compareFrom;
    }
}
