package com.mingdong.bop.model;

import java.util.List;

public class EChartPie
{
    private List<String> legendData;
    private List<ESeriePie> series;

    public List<String> getLegendData()
    {
        return legendData;
    }

    public void setLegendData(List<String> legendData)
    {
        this.legendData = legendData;
    }

    public List<ESeriePie> getSeries()
    {
        return series;
    }

    public void setSeries(List<ESeriePie> series)
    {
        this.series = series;
    }
}
