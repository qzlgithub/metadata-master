package com.mingdong.mis.model;

public class RequestHolder
{
    private Long accountId;
    private Long clientId;
    private Long userId;
    private String appSecret;
    private String ip;
    private MetadataRes res;

    public Long getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
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

    public String getAppSecret()
    {
        return appSecret;
    }

    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public MetadataRes getRes()
    {
        return res;
    }

    public void setRes(MetadataRes res)
    {
        this.res = res;
    }
}
