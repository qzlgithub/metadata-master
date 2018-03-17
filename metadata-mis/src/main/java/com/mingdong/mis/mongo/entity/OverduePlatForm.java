package com.mingdong.mis.mongo.entity;

import java.util.Date;

public class OverduePlatForm
{
    private String platformCode;
    private String platformType;
    private Date overdueEarliestTime;
    private Date overdueLatestTime;

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
