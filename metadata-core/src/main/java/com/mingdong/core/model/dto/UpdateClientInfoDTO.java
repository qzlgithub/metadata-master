package com.mingdong.core.model.dto;

import java.io.Serializable;

public class UpdateClientInfoDTO implements Serializable
{
    private Long clientId;
    private String corpName;
    private String shortName;
    private String license;
    private Long industryId;
    private String name;
    private String phone;
    private String email;
    private Integer userEnabled;
    private Integer accountEnable;

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

    public Integer getUserEnabled()
    {
        return userEnabled;
    }

    public void setUserEnabled(Integer userEnabled)
    {
        this.userEnabled = userEnabled;
    }

    public Integer getAccountEnable()
    {
        return accountEnable;
    }

    public void setAccountEnable(Integer accountEnable)
    {
        this.accountEnable = accountEnable;
    }
}
