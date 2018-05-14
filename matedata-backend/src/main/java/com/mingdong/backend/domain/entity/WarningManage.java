package com.mingdong.backend.domain.entity;

import java.util.Date;

public class WarningManage
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String warningCode;
    private Long productId;
    private Long clientId;
    private String thirdCode;
    private Long userId;
    private Integer dispose;
    private Date disposeTime;
    private String remark;

    public Date getDisposeTime()
    {
        return disposeTime;
    }

    public void setDisposeTime(Date disposeTime)
    {
        this.disposeTime = disposeTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

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

    public String getWarningCode()
    {
        return warningCode;
    }

    public void setWarningCode(String warningCode)
    {
        this.warningCode = warningCode;
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

    public String getThirdCode()
    {
        return thirdCode;
    }

    public void setThirdCode(String thirdCode)
    {
        this.thirdCode = thirdCode;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Integer getDispose()
    {
        return dispose;
    }

    public void setDispose(Integer dispose)
    {
        this.dispose = dispose;
    }
}
