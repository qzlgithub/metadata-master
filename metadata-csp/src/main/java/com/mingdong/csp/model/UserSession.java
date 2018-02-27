package com.mingdong.csp.model;

public class UserSession
{
    private Long clientId;
    private Long userId;
    private String name;
    private String username;
    private Integer primary;

    public UserSession(Long clientId, Long userId, String name, String username, Integer primary)
    {
        this.clientId = clientId;
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.primary = primary;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public String getName()
    {
        return name;
    }

    public String getUsername()
    {
        return username;
    }

    public Integer getPrimary()
    {
        return primary;
    }

}
