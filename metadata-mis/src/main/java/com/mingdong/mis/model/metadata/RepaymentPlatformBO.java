package com.mingdong.mis.model.metadata;

public class RepaymentPlatformBO
{
    private String platformCode;
    private String platformType;
    private String repaymentEarliestTime;
    private String repaymentLatestTime;

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

    public String getRepaymentEarliestTime()
    {
        return repaymentEarliestTime;
    }

    public void setRepaymentEarliestTime(String repaymentEarliestTime)
    {
        this.repaymentEarliestTime = repaymentEarliestTime;
    }

    public String getRepaymentLatestTime()
    {
        return repaymentLatestTime;
    }

    public void setRepaymentLatestTime(String repaymentLatestTime)
    {
        this.repaymentLatestTime = repaymentLatestTime;
    }
}
