package com.mingdong.core.model;

import java.util.List;
import java.util.Map;

public class ListRes extends BLResp
{
    private int code;
    private String message;
    private int total;
    private List<Map<String, Object>> list;

    public ListRes()
    {
        code = 0;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
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
}
