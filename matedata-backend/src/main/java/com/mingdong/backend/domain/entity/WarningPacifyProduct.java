package com.mingdong.backend.domain.entity;

import java.util.Date;

public class WarningPacifyProduct
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long pacifyId;
    private Long productId;

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

    public Long getPacifyId()
    {
        return pacifyId;
    }

    public void setPacifyId(Long pacifyId)
    {
        this.pacifyId = pacifyId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }
}
