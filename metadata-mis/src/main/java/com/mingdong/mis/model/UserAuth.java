package com.mingdong.mis.model;

import com.mingdong.common.util.StringUtils;

public class UserAuth
{
    private Long productId;
    private Long clientId;
    private Long userId;
    private String host;

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
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

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public boolean auth()
    {
        return !StringUtils.isNullBlank(host) && productId != null && clientId != null && userId != null;
    }
}
