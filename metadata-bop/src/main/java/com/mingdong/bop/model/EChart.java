package com.mingdong.bop.model;

import java.util.List;

public class EChart
{
    private String name;
    private List<String> xAxis;
    private List<ESerie> series;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<String> getxAxis()
    {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis)
    {
        this.xAxis = xAxis;
    }

    public List<ESerie> getSeries()
    {
        return series;
    }

    public void setSeries(List<ESerie> series)
    {
        this.series = series;
    }
}
