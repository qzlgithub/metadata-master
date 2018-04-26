package com.mingdong.mis.model.metadata;

public class RefusePlatformBO
{
    private String platformCode;
    private String platformType;
    private String refuseEarliestTime;
    private String refuseLatestTime;

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

    public String getRefuseEarliestTime()
    {
        return refuseEarliestTime;
    }

    public void setRefuseEarliestTime(String refuseEarliestTime)
    {
        this.refuseEarliestTime = refuseEarliestTime;
    }

    public String getRefuseLatestTime()
    {
        return refuseLatestTime;
    }

    public void setRefuseLatestTime(String refuseLatestTime)
    {
        this.refuseLatestTime = refuseLatestTime;
    }
}
