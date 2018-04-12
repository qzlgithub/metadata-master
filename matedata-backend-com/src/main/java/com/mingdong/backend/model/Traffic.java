package com.mingdong.backend.model;

public class Traffic
{
    private Long clientId;
    private String corpName;
    private Long productId;
    private String productName;
    private Long timestamp;
    private String host;
    private int hashCode;

    public Traffic(Long clientId, String corpName, Long productId, String productName, Long timestamp, String host)
    {
        this.clientId = clientId;
        this.corpName = corpName;
        this.productId = productId;
        this.productName = productName;
        this.timestamp = timestamp;
        this.host = host;
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

    public int getHashCode()
    {
        return hashCode;
    }

    public void setHashCode(int hashCode)
    {
        this.hashCode = hashCode;
    }
}
