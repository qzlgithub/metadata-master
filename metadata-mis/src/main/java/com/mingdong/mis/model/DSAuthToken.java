package com.mingdong.mis.model;

public class DSAuthToken
{
    private String token;
    private Long expire;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Long getExpire()
    {
        return expire;
    }

    public void setExpire(Long expire)
    {
        this.expire = expire;
    }
}
