package com.mingdong.mis.domain.entity;

import java.util.Date;

public class ClientUserProduct
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long userId;
    private Long productId;
    private String appSecret;
    private String reqHost;
    private String accessToken;
    private Date validTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
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

    public String getAppSecret()
    {
        return appSecret;
    }

    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
    }

    public String getReqHost()
    {
        return reqHost;
    }

    public void setReqHost(String reqHost)
    {
        this.reqHost = reqHost;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public Date getValidTime()
    {
        return validTime;
    }

    public void setValidTime(Date validTime)
    {
        this.validTime = validTime;
    }
}
