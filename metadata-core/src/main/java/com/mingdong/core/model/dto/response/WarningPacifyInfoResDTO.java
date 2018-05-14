package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WarningPacifyInfoResDTO implements Serializable
{
    private Long pacifyId;
    private Long clientId;
    private String corpName;
    private String linkName;
    private String phone;
    private String productName;
    private Integer productCount;
    private Integer errorStatus;
    private Date errorTime;
    private Date warningDisposeTime;
    private Integer dispose;
    private Date pacifyDisposeTime;
    private String warningCode;
    private String warningUserName;
    private String pacifyUserName;
    private String warningRemark;
    private String pacifyRemark;
    private Integer warningType;
    private List<ClientContactResDTO> contacts;
    private List<WarningPacifyProductResDTO> pacifyProducts;

    public List<WarningPacifyProductResDTO> getPacifyProducts()
    {
        return pacifyProducts;
    }

    public void setPacifyProducts(List<WarningPacifyProductResDTO> pacifyProducts)
    {
        this.pacifyProducts = pacifyProducts;
    }

    public String getWarningUserName()
    {
        return warningUserName;
    }

    public void setWarningUserName(String warningUserName)
    {
        this.warningUserName = warningUserName;
    }

    public String getPacifyUserName()
    {
        return pacifyUserName;
    }

    public void setPacifyUserName(String pacifyUserName)
    {
        this.pacifyUserName = pacifyUserName;
    }

    public Date getWarningDisposeTime()
    {
        return warningDisposeTime;
    }

    public void setWarningDisposeTime(Date warningDisposeTime)
    {
        this.warningDisposeTime = warningDisposeTime;
    }

    public Date getPacifyDisposeTime()
    {
        return pacifyDisposeTime;
    }

    public void setPacifyDisposeTime(Date pacifyDisposeTime)
    {
        this.pacifyDisposeTime = pacifyDisposeTime;
    }

    public Integer getWarningType()
    {
        return warningType;
    }

    public void setWarningType(Integer warningType)
    {
        this.warningType = warningType;
    }

    public List<ClientContactResDTO> getContacts()
    {
        return contacts;
    }

    public void setContacts(List<ClientContactResDTO> contacts)
    {
        this.contacts = contacts;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public String getWarningCode()
    {
        return warningCode;
    }

    public void setWarningCode(String warningCode)
    {
        this.warningCode = warningCode;
    }

    public String getWarningRemark()
    {
        return warningRemark;
    }

    public void setWarningRemark(String warningRemark)
    {
        this.warningRemark = warningRemark;
    }

    public String getPacifyRemark()
    {
        return pacifyRemark;
    }

    public void setPacifyRemark(String pacifyRemark)
    {
        this.pacifyRemark = pacifyRemark;
    }

    public Integer getProductCount()
    {
        return productCount;
    }

    public void setProductCount(Integer productCount)
    {
        this.productCount = productCount;
    }

    public Long getPacifyId()
    {
        return pacifyId;
    }

    public void setPacifyId(Long pacifyId)
    {
        this.pacifyId = pacifyId;
    }

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
    }

    public String getLinkName()
    {
        return linkName;
    }

    public void setLinkName(String linkName)
    {
        this.linkName = linkName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public Integer getErrorStatus()
    {
        return errorStatus;
    }

    public void setErrorStatus(Integer errorStatus)
    {
        this.errorStatus = errorStatus;
    }

    public Date getErrorTime()
    {
        return errorTime;
    }

    public void setErrorTime(Date errorTime)
    {
        this.errorTime = errorTime;
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
