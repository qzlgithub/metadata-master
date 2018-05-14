package com.mingdong.backend.domain.entity;

import java.util.Date;

public class WarningPacifyInfo
{
    private String warningCode;
    private Long pacifyId;
    private Date warningTime;
    private Integer warningStatus;
    private Date warningDisposeTime;
    private Long clientId;
    private Integer dispose;
    private Date pacifyDisposeTime;
    private Long warningUserId;
    private Long pacifyUserId;
    private String warningRemark;
    private String pacifyRemark;
    private Integer warningType;

    public Long getWarningUserId()
    {
        return warningUserId;
    }

    public void setWarningUserId(Long warningUserId)
    {
        this.warningUserId = warningUserId;
    }

    public Long getPacifyUserId()
    {
        return pacifyUserId;
    }

    public void setPacifyUserId(Long pacifyUserId)
    {
        this.pacifyUserId = pacifyUserId;
    }

    public Date getWarningDisposeTime()
    {
        return warningDisposeTime;
    }

    public void setWarningDisposeTime(Date warningDisposeTime)
    {
        this.warningDisposeTime = warningDisposeTime;
    }

    public Date getPacifyDisposeTime()
    {
        return pacifyDisposeTime;
    }

    public void setPacifyDisposeTime(Date pacifyDisposeTime)
    {
        this.pacifyDisposeTime = pacifyDisposeTime;
    }

    public Integer getWarningType()
    {
        return warningType;
    }

    public void setWarningType(Integer warningType)
    {
        this.warningType = warningType;
    }

    public String getWarningRemark()
    {
        return warningRemark;
    }

    public void setWarningRemark(String warningRemark)
    {
        this.warningRemark = warningRemark;
    }

    public String getPacifyRemark()
    {
        return pacifyRemark;
    }

    public void setPacifyRemark(String pacifyRemark)
    {
        this.pacifyRemark = pacifyRemark;
    }

    public String getWarningCode()
    {
        return warningCode;
    }

    public void setWarningCode(String warningCode)
    {
        this.warningCode = warningCode;
    }

    public Long getPacifyId()
    {
        return pacifyId;
    }

    public void setPacifyId(Long pacifyId)
    {
        this.pacifyId = pacifyId;
    }

    public Date getWarningTime()
    {
        return warningTime;
    }

    public void setWarningTime(Date warningTime)
    {
        this.warningTime = warningTime;
    }

    public Integer getWarningStatus()
    {
        return warningStatus;
    }

    public void setWarningStatus(Integer warningStatus)
    {
        this.warningStatus = warningStatus;
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
