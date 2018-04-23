package com.mingdong.core.model.dto.request;

import java.io.Serializable;
import java.util.List;

public class ClientReqDTO implements Serializable
{
    private Long clientId;
    private String corpName;
    private String shortName;
    private String license;
    private Long industryId;
    private String username;
    private Integer accountTotalQty;
    private Integer enabled;
    private List<ClientContactReqDTO> contactList;
    private Long managerId;

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public Long getIndustryId()
    {
        return industryId;
    }

    public void setIndustryId(Long industryId)
    {
        this.industryId = industryId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Integer getAccountTotalQty()
    {
        return accountTotalQty;
    }

    public void setAccountTotalQty(Integer accountTotalQty)
    {
        this.accountTotalQty = accountTotalQty;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public List<ClientContactReqDTO> getContactList()
    {
        return contactList;
    }

    public void setContactList(List<ClientContactReqDTO> contactList)
    {
        this.contactList = contactList;
    }

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }
}
