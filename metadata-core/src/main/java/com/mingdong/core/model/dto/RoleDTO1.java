package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.List;

public class RoleDTO1 implements Serializable
{
    private Long id;
    private String name;
    private List<Long> privilegeIdList;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Long> getPrivilegeIdList()
    {
        return privilegeIdList;
    }

    public void setPrivilegeIdList(List<Long> privilegeIdList)
    {
        this.privilegeIdList = privilegeIdList;
    }
}
