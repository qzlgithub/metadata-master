package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.util.List;

public class UserInfoResDTO implements Serializable
{
    private String username;
    private String name;
    private String phone;
    private String qq;
    private Long groupId;
    private Integer roleType;
    private Integer enabled;
    private Integer alarm;
    private Integer pacify;
    private String sessionId;
    private List<Long> privilegeIdList;

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

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
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

    public Long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Long groupId)
    {
        this.groupId = groupId;
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
