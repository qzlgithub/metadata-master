package com.mingdong.bop.model;

import java.util.List;

public class ManagerVO
{
    private Long managerId;
    private String name;
    private String phone;
    private String qq;
    private Long roleId;
    private Integer roleType;
    private Integer enabled;
    private List<Long> privilege;

    public Integer getRoleType()
    {
        return roleType;
    }

    public void setRoleType(Integer roleType)
    {
        this.roleType = roleType;
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

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getQq()
    {
        return qq;
    }

    public void setQq(String qq)
    {
        this.qq = qq;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public List<Long> getPrivilege()
    {
        return privilege;
    }

    public void setPrivilege(List<Long> privilege)
    {
        this.privilege = privilege;
    }
}
