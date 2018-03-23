package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDTO<T> implements Serializable
{
    private long total;
    private List<T> list;
    private Map<String, String> extradata;

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<T> getList()
    {
        return list;
    }

    public void setList(List<T> list)
    {
        this.list = list;
    }

    public Map<String, String> getExtradata()
    {
        return extradata;
    }

    @SuppressWarnings("unused")
    public void setExtradata(Map<String, String> extradata)
    {
        this.extradata = extradata;
    }

    public void addExtra(String k, String v)
    {
        if(extradata == null)
        {
            extradata = new HashMap<>();
        }
        extradata.put(k, v);
    }
}
