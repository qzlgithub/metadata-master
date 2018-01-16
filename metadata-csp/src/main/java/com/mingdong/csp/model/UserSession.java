package com.mingdong.csp.model;

public class UserSession
{
    private Long clientId;
    private Long userId;
    private String name;
    private Integer primary;

    public UserSession(Long clientId, Long userId, String name, Integer primary)
    {
        this.clientId = clientId;
        this.userId = userId;
        this.name = name;
        this.primary = primary;
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

    public Integer getPrimary()
    {
        return primary;
    }

    public void setPrimary(Integer primary)
    {
        this.primary = primary;
    }
}
