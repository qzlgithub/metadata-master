package com.mingdong.core.model.dto.request;

import java.io.Serializable;
import java.util.Date;

public class WarningPacifyReqDTO implements Serializable
{
    private Long id;
    private String remark;
    private Integer dispose;
    private Date disposeTime;
    private Long managerId;

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

    public Date getDisposeTime()
    {
        return disposeTime;
    }

    public void setDisposeTime(Date disposeTime)
    {
        this.disposeTime = disposeTime;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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
