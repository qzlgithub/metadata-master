package com.mingdong.core.model;

public class UserSession
{
    private Long userId;
    private String name;

    public UserSession(Long userId, String name)
    {
        this.userId = userId;
        this.name = name;
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
