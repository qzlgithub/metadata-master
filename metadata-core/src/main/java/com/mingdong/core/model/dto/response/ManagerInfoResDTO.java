package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.util.Date;

public class ManagerInfoResDTO implements Serializable
{
    private Long managerId;
    private Integer roleType;
    private String name;
    private String phone;
    private String username;
    private Long groupId;
    private Date registerTime;
    private Integer enabled;
    private String groupName;
    private Integer alarm;
    private Integer pacify;

    public Integer getAlarm()
    {
        return alarm;
    }

    public void setAlarm(Integer alarm)
    {
        this.alarm = alarm;
    }

    public Integer getPacify()
    {
        return pacify;
    }

    public void setPacify(Integer pacify)
    {
        this.pacify = pacify;
    }

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

    public Integer getRoleType()
    {
        return roleType;
    }

    public void setRoleType(Integer roleType)
    {
        this.roleType = roleType;
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

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
    }

    public Date getRegisterTime()
    {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime)
    {
        this.registerTime = registerTime;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
}
