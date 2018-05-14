package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.util.Date;

public class WarningManageDetailResDTO implements Serializable
{
    private Date time;
    private Long productId;
    private Long clientId;
    private Long clientUserId;
    private String requestIp;
    private Integer errorType;
    private String productName;
    private String corpName;
    private Date warningAt;
    private String userName;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
    }

    public Date getWarningAt()
    {
        return warningAt;
    }

    public void setWarningAt(Date warningAt)
    {
        this.warningAt = warningAt;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

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

    public Long getClientUserId()
    {
        return clientUserId;
    }

    public void setClientUserId(Long clientUserId)
    {
        this.clientUserId = clientUserId;
    }

    public String getRequestIp()
    {
        return requestIp;
    }

    public void setRequestIp(String requestIp)
    {
        this.requestIp = requestIp;
    }

    public Integer getErrorType()
    {
        return errorType;
    }

    public void setErrorType(Integer errorType)
    {
        this.errorType = errorType;
    }
}
