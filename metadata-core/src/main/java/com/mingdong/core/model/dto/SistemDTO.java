package com.mingdong.core.model.dto;

import java.io.Serializable;

public class SistemDTO implements Serializable
{
    private Integer clientUserMax;
    private String serviceQQ;
    private String filePath;

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

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
