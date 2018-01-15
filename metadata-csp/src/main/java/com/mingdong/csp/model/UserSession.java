package com.mingdong.csp.model;

public class UserSession
{
    private Long clientId;
    private Long userId;
    private String name;

    public UserSession(Long clientId, Long userId, String name)
    {
        this.clientId = clientId;
        this.userId = userId;
        this.name = name;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

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
}
