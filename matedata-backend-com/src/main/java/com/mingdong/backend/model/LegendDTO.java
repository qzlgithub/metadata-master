package com.mingdong.backend.model;

import java.io.Serializable;
import java.util.List;

public class LegendDTO implements Serializable
{
    private String name;
    private List<Long> series;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Long> getSeries()
    {
        return series;
    }

    public void setSeries(List<Long> series)
    {
        this.series = series;
    }
}
