package com.mingdong.core.model.dto;

import java.io.Serializable;

public class PrivilegeDTO implements Serializable
{
    private Long privilegeId;
    private String name;
    private Integer enabled;

    public Long getPrivilegeId()
    {
        return privilegeId;
    }

    public void setPrivilegeId(Long privilegeId)
    {
        this.privilegeId = privilegeId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }
}
