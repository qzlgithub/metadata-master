package com.mingdong.bop.model;

import java.util.List;

public class NewManagerVO
{
    private String username;
    private String password;
    private String name;
    private String phone;
    private Long roleId;
    private Integer enabled;
    private List<Long> privilege;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
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
