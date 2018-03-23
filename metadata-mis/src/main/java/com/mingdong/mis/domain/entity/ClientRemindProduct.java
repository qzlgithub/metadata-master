package com.mingdong.mis.domain.entity;

import java.util.Date;

public class ClientRemindProduct
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long remindId;
    private Long productId;
    private Long rechargeId;

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

    public Long getRemindId()
    {
        return remindId;
    }

    public void setRemindId(Long remindId)
    {
        this.remindId = remindId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getRechargeId()
    {
        return rechargeId;
    }

    public void setRechargeId(Long rechargeId)
    {
        this.rechargeId = rechargeId;
    }
}
