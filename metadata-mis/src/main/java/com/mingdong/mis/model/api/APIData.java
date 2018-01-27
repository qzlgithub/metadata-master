package com.mingdong.mis.model.api;

import java.util.List;
import java.util.Map;

public class APIData implements APIPayload
{
    private Integer hit;
    private List<Map<String, Object>> query;
    private List<Map<String, Object>> result;

    public Integer getHit()
    {
        return hit;
    }

    public void setHit(Integer hit)
    {
        this.hit = hit;
    }

    public List<Map<String, Object>> getQuery()
    {
        return query;
    }

    public void setQuery(List<Map<String, Object>> query)
    {
        this.query = query;
    }

    public List<Map<String, Object>> getResult()
    {
        return result;
    }

    public void setResult(List<Map<String, Object>> result)
    {
        this.result = result;
    }

    @Override
    public boolean hit()
    {
        return false;
    }
}
