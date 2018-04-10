package com.mingdong.mis.model;

import java.util.HashMap;
import java.util.Map;

public class MDRes
{
    private Integer status;
    private Map<String, Object> data;

    MDRes(Integer status)
    {
        this.status = status;
    }

    MDRes()
    {
        new MDRes(0);
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Map<String, Object> getData()
    {
        return data;
    }

    public void setData(Map<String, Object> data)
    {
        this.data = data;
    }

    public void addData(String k, Object v)
    {
        if(data == null)
        {
            data = new HashMap<>();
        }
        data.put(k, v);
    }
}
