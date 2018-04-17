package com.mingdong.backend.model;

import java.io.Serializable;
import java.util.List;

public class LineDiagramDTO implements Serializable
{
    private List<String> xAxis;
    private List<LegendDTO> legendList;

    public List<String> getxAxis()
    {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis)
    {
        this.xAxis = xAxis;
    }

    public List<LegendDTO> getLegendList()
    {
        return legendList;
    }

    public void setLegendList(List<LegendDTO> legendList)
    {
        this.legendList = legendList;
    }
}
