package com.mingdong.mis.domain.entity;

import java.util.Date;

public class Client
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String corpName;
    private String shortName;
    private String license;
    private Long industryId;
    private Long primaryUserId;
    private Long managerId;
    private Integer accountQty;
    private Integer deleted;

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

    public Long getPrimaryUserId()
    {
        return primaryUserId;
    }

    public void setPrimaryUserId(Long primaryUserId)
    {
        this.primaryUserId = primaryUserId;
    }

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

    public Integer getAccountQty()
    {
        return accountQty;
    }

    public void setAccountQty(Integer accountQty)
    {
        this.accountQty = accountQty;
    }

    public Integer getDeleted()
    {
        return deleted;
    }

    public void setDeleted(Integer deleted)
    {
        this.deleted = deleted;
    }
}