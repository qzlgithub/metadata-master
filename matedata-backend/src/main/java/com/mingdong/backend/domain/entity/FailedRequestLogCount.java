package com.mingdong.backend.domain.entity;

public class FailedRequestLogCount
{
    private Long clientId;
    private Long productId;
    private Integer status;
    private Long count;

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

    public Long getCount()
    {
        return count;
    }

    public void setCount(Long count)
    {
        this.count = count;
    }
}
