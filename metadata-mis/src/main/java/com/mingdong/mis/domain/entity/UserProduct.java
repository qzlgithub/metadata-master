package com.mingdong.mis.domain.entity;

import java.util.Date;

public class UserProduct
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long userId;
    private Long productId;
    private String appKey;
    private String reqHost;

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

    public String getAppKey()
    {
        return appKey;
    }

    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String getReqHost()
    {
        return reqHost;
    }

    public void setReqHost(String reqHost)
    {
        this.reqHost = reqHost;
    }
}
