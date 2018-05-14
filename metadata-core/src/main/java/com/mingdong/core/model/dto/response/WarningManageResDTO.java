package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.util.Date;

public class WarningManageResDTO implements Serializable
{
    private Long id;
    private Date warningAt;
    private String warningCode;
    private String thirdCode;
    private Integer dispose;
    private Date disposeTime;
    private String productName;
    private String corpName;
    private Long userId;
    private String userName;
    private Integer warningType;
    private String remark;

    public Date getDisposeTime()
    {
        return disposeTime;
    }

    public void setDisposeTime(Date disposeTime)
    {
        this.disposeTime = disposeTime;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getWarningType()
    {
        return warningType;
    }

    public void setWarningType(Integer warningType)
    {
        this.warningType = warningType;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getWarningAt()
    {
        return warningAt;
    }

    public void setWarningAt(Date warningAt)
    {
        this.warningAt = warningAt;
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

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getWarningCode()
    {
        return warningCode;
    }

    public void setWarningCode(String warningCode)
    {
        this.warningCode = warningCode;
    }

    public String getThirdCode()
    {
        return thirdCode;
    }

    public void setThirdCode(String thirdCode)
    {
        this.thirdCode = thirdCode;
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
