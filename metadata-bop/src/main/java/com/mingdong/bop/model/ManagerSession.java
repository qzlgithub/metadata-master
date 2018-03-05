package com.mingdong.bop.model;

import java.util.List;

public class ManagerSession
{
    private Long managerId;
    private String name;
    private String roleCode;
    private String avatar;
    private List<String> privileges;
    private Long addAt;

    public String getRoleCode()
    {
        return roleCode;
    }

    public void setRoleCode(String roleCode)
    {
        this.roleCode = roleCode;
    }

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public List<String> getPrivileges()
    {
        return privileges;
    }

    public void setPrivileges(List<String> privileges)
    {
        this.privileges = privileges;
    }

    public Long getAddAt()
    {
        return addAt;
    }

    public void setAddAt(Long addAt)
    {
        this.addAt = addAt;
    }
}
