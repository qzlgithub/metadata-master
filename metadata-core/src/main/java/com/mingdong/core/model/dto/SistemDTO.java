package com.mingdong.core.model.dto;

import java.io.Serializable;

public class SistemDTO implements Serializable
{
    private Integer clientUserMax;
    private String serviceQQ;

    public Integer getClientUserMax()
    {
        return clientUserMax;
    }

    public void setClientUserMax(Integer clientUserMax)
    {
        this.clientUserMax = clientUserMax;
    }

    public String getServiceQQ()
    {
        return serviceQQ;
    }

    public void setServiceQQ(String serviceQQ)
    {
        this.serviceQQ = serviceQQ;
    }
}
