package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ClientDetailDTO extends ResultDTO implements Serializable
{
    private Long clientId;
    private String corpName;
    private String shortName;
    private String license;
    private Long industryId;
    private String username;
    private BigDecimal balance;
    private Integer userStatus;
    private Integer accountStatus;
    private List<ClientContactDTO> contacts;
    private List<ClientUserDTO> users;
    private Date addTime;
    private String managerName;

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

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public Integer getUserStatus()
    {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus)
    {
        this.userStatus = userStatus;
    }

    public Integer getAccountStatus()
    {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus)
    {
        this.accountStatus = accountStatus;
    }

    public List<ClientContactDTO> getContacts()
    {
        return contacts;
    }

    public void setContacts(List<ClientContactDTO> contacts)
    {
        this.contacts = contacts;
    }

    public List<ClientUserDTO> getUsers()
    {
        return users;
    }

    public void setUsers(List<ClientUserDTO> users)
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

    public String getManagerName()
    {
        return managerName;
    }

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }
}