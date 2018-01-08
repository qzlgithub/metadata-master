package com.mingdong.bop.domain.entity;

import java.util.Date;

public class ClientOperateLog
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long clientUserId;
    private Long managerId;
    private String reason;

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

    public Long getClientUserId()
    {
        return clientUserId;
    }

    public void setClientUserId(Long clientUserId)
    {
        this.clientUserId = clientUserId;
    }

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }
}
