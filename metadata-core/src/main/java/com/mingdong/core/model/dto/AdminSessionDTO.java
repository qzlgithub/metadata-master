package com.mingdong.core.model.dto;

import java.util.List;

public class AdminSessionDTO extends ResultDTO
{
    private Long userId;
    private String name;
    private Integer roleType;
    private String sessionId;
    private List<String> functionList;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getRoleType()
    {
        return roleType;
    }

    public void setRoleType(Integer roleType)
    {
        this.roleType = roleType;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public List<String> getFunctionList()
    {
        return functionList;
    }

    public void setFunctionList(List<String> functionList)
    {
        this.functionList = functionList;
    }
}
