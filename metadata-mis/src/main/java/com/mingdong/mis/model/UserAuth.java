package com.mingdong.mis.model;

public class UserAuth
{
    private Long clientId;
    private Long userId;
    private Long productId;
    private Long clientProductId;
    private Integer billPlan;
    private Long start;
    private Long end;
    private String product;
    private String secretKey;
    private String host;

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

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getClientProductId()
    {
        return clientProductId;
    }

    public void setClientProductId(Long clientProductId)
    {
        this.clientProductId = clientProductId;
    }

    public Integer getBillPlan()
    {
        return billPlan;
    }

    public void setBillPlan(Integer billPlan)
    {
        this.billPlan = billPlan;
    }

    public Long getStart()
    {
        return start;
    }

    public void setStart(Long start)
    {
        this.start = start;
    }

    public Long getEnd()
    {
        return end;
    }

    public void setEnd(Long end)
    {
        this.end = end;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getSecretKey()
    {
        return secretKey;
    }

    public void setSecretKey(String secretKey)
    {
        this.secretKey = secretKey;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }
}
