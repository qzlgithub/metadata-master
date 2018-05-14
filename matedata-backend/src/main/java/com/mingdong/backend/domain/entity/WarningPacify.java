package com.mingdong.backend.domain.entity;

import java.util.Date;

public class WarningPacify
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long manageId;
    private Long clientId;
    private Long userId;
    private Integer dispose;
    private Date disposeTime;
    private String remark;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Date getDisposeTime()
    {
        return disposeTime;
    }

    public void setDisposeTime(Date disposeTime)
    {
        this.disposeTime = disposeTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

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

    public Long getManageId()
    {
        return manageId;
    }

    public void setManageId(Long manageId)
    {
        this.manageId = manageId;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
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
