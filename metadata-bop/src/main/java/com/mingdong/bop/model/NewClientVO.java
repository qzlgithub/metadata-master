package com.mingdong.bop.model;

import java.util.List;

public class NewClientVO
{
    private Long clientId;
    private String corpName;
    private String shortName;
    private String license;
    private Long industryId;
    private String username;
    private String password;
    private Integer enabled;
    private Long managerId;
    private List<ContactVO> contacts;
    private List<Long> contactDel;

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public List<ContactVO> getContacts()
    {
        return contacts;
    }

    public void setContacts(List<ContactVO> contacts)
    {
        this.contacts = contacts;
    }

    public List<Long> getContactDel()
    {
        return contactDel;
    }

    public void setContactDel(List<Long> contactDel)
    {
        this.contactDel = contactDel;
    }
}
