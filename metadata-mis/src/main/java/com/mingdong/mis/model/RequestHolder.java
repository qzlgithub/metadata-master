package com.mingdong.mis.model;

public class RequestHolder
{
    private String ip;
    private Long productId;
    private Long clientId;
    private Long userId;
    private MetadataRes res;

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public MetadataRes getRes()
    {
        return res;
    }

    public void setRes(MetadataRes res)
    {
        this.res = res;
    }
}
