package com.mingdong.core.model;

public class DateAxis
{
    private String name;
    private DateRange range;

    public DateAxis()
    {
    }

    public DateAxis(String name, DateRange range)
    {
        this.name = name;
        this.range = range;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public DateRange getRange()
    {
        return range;
    }

    public void setRange(DateRange range)
    {
        this.range = range;
    }
}
