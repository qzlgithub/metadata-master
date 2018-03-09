package com.mingdong.core.model.dto.request;

import com.mingdong.core.model.dto.RequestDTO;

import java.util.Date;

public class ClientOperateLogReqDTO extends RequestDTO
{
    private Date operateTime;
    private Integer type;
    private String reason;
    private String managerName;

    public Date getOperateTime()
    {
        return operateTime;
    }

    public void setOperateTime(Date operateTime)
    {
        this.operateTime = operateTime;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getManagerName()
    {
        return managerName;
    }

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }
}
