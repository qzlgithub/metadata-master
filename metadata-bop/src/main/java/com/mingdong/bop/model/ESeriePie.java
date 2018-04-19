package com.mingdong.bop.model;

import java.util.List;
import java.util.Map;

public class ESeriePie
{
    private String name;
    private List<Map<String,String>> data;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Map<String, String>> getData()
    {
        return data;
    }

    public void setData(List<Map<String, String>> data)
    {
        this.data = data;
    }
}
