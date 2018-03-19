package com.mingdong.mis.mongo.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class OverduePlatForm
{
    @Id
    private String id;
    private String phone;
    private String name;
    private String idNo;
    private String platformCode;
    private String platformType;
    private Date overdueEarliestTime;
    private Date overdueLatestTime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIdNo()
    {
        return idNo;
    }

    public void setIdNo(String idNo)
    {
        this.idNo = idNo;
    }

    public String getPlatformCode()
    {
        return platformCode;
    }

    public void setPlatformCode(String platformCode)
    {
        this.platformCode = platformCode;
    }

    public String getPlatformType()
    {
        return platformType;
    }

    public void setPlatformType(String platformType)
    {
        this.platformType = platformType;
    }

    public Date getOverdueEarliestTime()
    {
        return overdueEarliestTime;
    }

    public void setOverdueEarliestTime(Date overdueEarliestTime)
    {
        this.overdueEarliestTime = overdueEarliestTime;
    }

    public Date getOverdueLatestTime()
    {
        return overdueLatestTime;
    }

    public void setOverdueLatestTime(Date overdueLatestTime)
    {
        this.overdueLatestTime = overdueLatestTime;
    }
}
