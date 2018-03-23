package com.mingdong.core.model;

import java.util.List;
import java.util.Map;

public class RestListResp extends RestResp
{
    private long total;
    private List<Map<String, Object>> list;

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
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
