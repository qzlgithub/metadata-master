package com.mingdong.core.model.dto.request;

import java.io.Serializable;

public class LoginReqDTO implements Serializable
{
    private String username;
    private String password;
    private String sessionId;

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

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }
}
