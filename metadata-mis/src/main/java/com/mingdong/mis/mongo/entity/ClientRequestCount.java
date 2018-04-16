package com.mingdong.mis.mongo.entity;

public class ClientRequestCount
{
    private Long clientId;
    private Long count;

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getCount()
    {
        return count;
    }

    public void setCount(Long count)
    {
        this.count = count;
    }
}
