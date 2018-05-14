package com.mingdong.core.model.dto.response;

import java.util.Date;
import java.util.List;

public class ClientDetailResDTO extends ResponseDTO
{
    private Long clientId;
    private String username;
    private String corpName;
    private String shortName;
    private String license;
    private Long industryId;
    private Integer accountTotalQty;
    private Integer enabled;
    private List<ClientContactResDTO> contacts;
    private List<ClientUserResDTO> users;
    private Date addTime;
    private Long managerId;
    private String managerName;

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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

    public List<ClientContactResDTO> getContacts()
    {
        return contacts;
    }

    public void setContacts(List<ClientContactResDTO> contacts)
    {
        this.contacts = contacts;
    }

    public List<ClientUserResDTO> getUsers()
    {
        return users;
    }

    public void setUsers(List<ClientUserResDTO> users)
    {
        this.users = users;
    }

    public Date getAddTime()
    {
        return addTime;
    }

    public void setAddTime(Date addTime)
    {
        this.addTime = addTime;
    }

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

    public String getManagerName()
    {
        return managerName;
    }

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }
}
