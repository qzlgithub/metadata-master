package com.mingdong.mis.model;

import com.mingdong.common.util.StringUtils;

public class UserAuth
{
    private String product;
    private Long clientId;
    private Long accountId;
    private Long userId;
    private String appSecret;
    private String validIP;

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
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

    public String getValidIP()
    {
        return validIP;
    }

    public void setValidIP(String validIP)
    {
        this.validIP = validIP;
    }

    public boolean auth()
    {
        return !StringUtils.isNullBlank(validIP) && !StringUtils.isNullBlank(product) && clientId != null &&
                accountId != null && userId != null;
    }
}
