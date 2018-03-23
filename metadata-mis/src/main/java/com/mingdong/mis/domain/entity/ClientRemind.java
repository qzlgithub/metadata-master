package com.mingdong.mis.domain.entity;

import java.util.Date;

public class ClientRemind
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Date remindDate;
    private Integer type;
    private Long clientId;
    private String linkName;
    private String linkPhone;
    private Long productId;
    private Integer count;
    private Integer day;
    private Long userId;
    private String remark;
    private Integer dispose;

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

    public Date getRemindDate()
    {
        return remindDate;
    }

    public void setRemindDate(Date remindDate)
    {
        this.remindDate = remindDate;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public String getLinkName()
    {
        return linkName;
    }

    public void setLinkName(String linkName)
    {
        this.linkName = linkName;
    }

    public String getLinkPhone()
    {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone)
    {
        this.linkPhone = linkPhone;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

    public Integer getDay()
    {
        return day;
    }

    public void setDay(Integer day)
    {
        this.day = day;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getDispose()
    {
        return dispose;
    }

    public void setDispose(Integer dispose)
    {
        this.dispose = dispose;
    }
}
