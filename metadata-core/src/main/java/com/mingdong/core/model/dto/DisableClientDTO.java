package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.List;

public class DisableClientDTO implements Serializable
{
    private List<Long> clientIdList;
    private Integer enabled;
    private String reason;
    private Long managerId;

    public List<Long> getClientIdList()
    {
        return clientIdList;
    }

    public void setClientIdList(List<Long> clientIdList)
    {
        this.clientIdList = clientIdList;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }
}
