package com.mingdong.mis.model;

import com.mingdong.mis.constant.APIProduct;

class RequestHolder
{
    private Long clientId;
    private Long userId;
    private Long productId;
    private Long clientProductId;
    private APIProduct product;
    private String appSecret;
    private Integer billPlan;
    private Long start;
    private Long end;
    private String host;
    private MDResp resp;

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

    public APIProduct getProduct()
    {
        return product;
    }

    public void setProduct(APIProduct product)
    {
        this.product = product;
    }

    public String getAppSecret()
    {
        return appSecret;
    }

    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
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

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public MDResp getResp()
    {
        return resp;
    }

    public void setResp(MDResp resp)
    {
        this.resp = resp;
    }
}
