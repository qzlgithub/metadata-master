package com.mingdong.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestListResp
{
    private String code;
    private String message;
    private Map<String, Object> data;
    private int total;
    private List<Map<String, Object>> list;

    public RestListResp()
    {
        code = "000000";
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public List<Map<String, Object>> getList()
    {
        return list;
    }

    public void setList(List<Map<String, Object>> list)
    {
        this.list = list;
    }

    public Map<String, Object> getData()
    {
        return data;
    }

    public void setData(Map<String, Object> data)
    {
        this.data = data;
    }

    public void addExtra(String k, Object v)
    {
        if(data == null)
        {
            data = new HashMap<>();
        }
        data.put(k, v);
    }
}
