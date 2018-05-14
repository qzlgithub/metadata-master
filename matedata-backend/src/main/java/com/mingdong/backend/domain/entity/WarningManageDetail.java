package com.mingdong.backend.domain.entity;

import java.util.Date;

public class WarningManageDetail
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Date time;
    private Long manageId;
    private Long productId;
    private Long clientId;
    private Long clientUserId;
    private String requestIp;
    private Integer errorType;
    private String thirdCode;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    public Long getManageId()
    {
        return manageId;
    }

    public void setManageId(Long manageId)
    {
        this.manageId = manageId;
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

    public String getThirdCode()
    {
        return thirdCode;
    }

    public void setThirdCode(String thirdCode)
    {
        this.thirdCode = thirdCode;
    }
}
