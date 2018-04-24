package com.mingdong.core.model.dto.response;

import java.io.Serializable;

public class RequestFailedCountResDTO implements Serializable
{
    private Long clientId;
    private Long productId;
    private Integer status;
    private Long count;

    public Long getCount()
    {
        return count;
    }

    public void setCount(Long count)
    {
        this.count = count;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }
}
