package com.mingdong.bop.model;

import java.util.List;

public class EChartLine
{
    private List<String> xAxis;
    private List<String> legendData;
    private List<ESerie> series;

    public List<String> getxAxis()
    {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis)
    {
        this.xAxis = xAxis;
    }

    public List<String> getLegendData()
    {
        return legendData;
    }

    public void setLegendData(List<String> legendData)
    {
        this.legendData = legendData;
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
