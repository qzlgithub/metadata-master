package com.mingdong.bop.model;

import java.util.Date;

public class ChartVO
{
    private Integer scope;
    private Integer unit;
    private Date startDate;
    private Date endDate;
    private Date compareFrom;

    public Integer getScope()
    {
        return scope;
    }

    public void setScope(Integer scope)
    {
        this.scope = scope;
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
