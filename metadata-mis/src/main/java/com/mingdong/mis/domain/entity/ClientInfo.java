package com.mingdong.mis.domain.entity;

import java.util.Date;

public class ClientInfo
{
    private Long clientId;
    private Date registerTime;
    private String corpName;
    private String shortName;
    private String license;
    private Long industryId;
    private String name;
    private String phone;
    private String email;
    private String username;
    private String managerName;
    private Integer accountQty;
    private Integer userEnabled;

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Date getRegisterTime()
    {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime)
    {
        this.registerTime = registerTime;
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

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Long getIndustryId()
    {
        return industryId;
    }

    public void setIndustryId(Long industryId)
    {
        this.industryId = industryId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getManagerName()
    {
        return managerName;
    }

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }

    public Integer getAccountQty()
    {
        return accountQty;
    }

    public void setAccountQty(Integer accountQty)
    {
        this.accountQty = accountQty;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public Integer getUserEnabled()
    {
        return userEnabled;
    }

    public void setUserEnabled(Integer userEnabled)
    {
        this.userEnabled = userEnabled;
    }
}