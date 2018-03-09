package com.mingdong.core.model.dto;

import com.mingdong.core.model.dto.base.RequestDTO;

import java.util.List;

public class AdminUserDTO extends RequestDTO
{
    private Long userId;
    private Long groupId;
    private Integer roleType;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String qq;
    private List<Long> privilegeIdList;
    private Integer enabled;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Integer getRoleType()
    {
        return roleType;
    }

    public void setRoleType(Integer roleType)
    {
        this.roleType = roleType;
    }

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

    public String getQq()
    {
        return qq;
    }

    public void setQq(String qq)
    {
        this.qq = qq;
    }

    public List<Long> getPrivilegeIdList()
    {
        return privilegeIdList;
    }

    public void setPrivilegeIdList(List<Long> privilegeIdList)
    {
        this.privilegeIdList = privilegeIdList;
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
