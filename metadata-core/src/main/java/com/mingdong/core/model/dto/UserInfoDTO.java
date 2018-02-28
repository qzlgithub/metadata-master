package com.mingdong.core.model.dto;

import java.util.List;

public class UserInfoDTO
{
    private String username;
    private String name;
    private String phone;
    private String qq;
    private Long roleId;
    private Integer enabled;
    private List<Long> privilegeIdList;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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

    public List<Long> getPrivilegeIdList()
    {
        return privilegeIdList;
    }

    public void setPrivilegeIdList(List<Long> privilegeIdList)
    {
        this.privilegeIdList = privilegeIdList;
    }
}
