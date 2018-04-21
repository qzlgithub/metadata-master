package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.util.Map;

public class RequestStatsResDTO implements Serializable
{
    private String name;
    private Map<String, Long> requestMap;
    private Map<String, Long> requestNotHitMap;
    private Map<String, Long> requestFailedMap;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Map<String, Long> getRequestMap()
    {
        return requestMap;
    }

    public void setRequestMap(Map<String, Long> requestMap)
    {
        this.requestMap = requestMap;
    }

    public Map<String, Long> getRequestNotHitMap()
    {
        return requestNotHitMap;
    }

    public void setRequestNotHitMap(Map<String, Long> requestNotHitMap)
    {
        this.requestNotHitMap = requestNotHitMap;
    }

    public Map<String, Long> getRequestFailedMap()
    {
        return requestFailedMap;
    }

    public void setRequestFailedMap(Map<String, Long> requestFailedMap)
    {
        this.requestFailedMap = requestFailedMap;
    }
}
