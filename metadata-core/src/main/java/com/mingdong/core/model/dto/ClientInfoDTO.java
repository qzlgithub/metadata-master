package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.Date;

public class ClientInfoDTO implements Serializable
{
    private Long clientId;
    private Date registerTime; // 注册时间
    private String corpName; // 公司名称
    private String shortName; // 公司简称
    private String license; // 组织机构代码
    private Long industryId; // 客户类型
    private String name; // 联系人
    private String phone; // 联系电话
    private String email; // 邮箱
    private String username; // 客户账号
    private String managerName; // 商务经理
    private Integer accountQty; // 子账号（个）
    private Integer userEnabled; // 账号状态

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

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
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

    public Integer getUserEnabled()
    {
        return userEnabled;
    }

    public void setUserEnabled(Integer userEnabled)
    {
        this.userEnabled = userEnabled;
    }
}
