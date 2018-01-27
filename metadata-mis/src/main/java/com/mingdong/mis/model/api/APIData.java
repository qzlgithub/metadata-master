package com.mingdong.mis.model.api;

import com.mingdong.common.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class APIData implements APIPayload
{
    private List<Map<String, Object>> query;
    private List<Map<String, Object>> result;

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
        return !CollectionUtils.isEmpty(result);
    }
}
