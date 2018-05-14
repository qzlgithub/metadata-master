package com.mingdong.backend.model;

public class Traffic
{
    private Integer status;
    private Boolean hit;
    private Long clientId;
    private String corpName;
    private Long productId;
    private String productName;
    private Long userId;
    private Long timestamp;
    private String host;
    private Integer payloadId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Boolean getHit()
    {
        return hit;
    }

    public void setHit(Boolean hit)
    {
        this.hit = hit;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public Long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public Integer getPayloadId()
    {
        return payloadId;
    }

    public void setPayloadId(Integer payloadId)
    {
        this.payloadId = payloadId;
    }
}
