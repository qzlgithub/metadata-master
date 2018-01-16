package com.mingdong.csp.model;

public class RequestHolder
{
    private Long clientId;
    private Long userId;
    private String username;
    private String currPage;
    private Integer primary;

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

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getCurrPage()
    {
        return currPage;
    }

    public void setCurrPage(String currPage)
    {
        this.currPage = currPage;
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
