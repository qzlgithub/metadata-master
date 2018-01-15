package com.mingdong.csp.model;

public class RequestHolder
{
    private Long clientId;
    private Long userId;
    private String currPage;

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

    public String getCurrPage()
    {
        return currPage;
    }

    public void setCurrPage(String currPage)
    {
        this.currPage = currPage;
    }
}
