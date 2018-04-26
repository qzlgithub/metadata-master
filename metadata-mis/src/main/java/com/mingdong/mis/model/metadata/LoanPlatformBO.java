package com.mingdong.mis.model.metadata;

public class LoanPlatformBO
{
    private String platformCode;
    private String platformType;
    private String loanEarliestTime;
    private String loanLatestTime;

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

    public String getLoanEarliestTime()
    {
        return loanEarliestTime;
    }

    public void setLoanEarliestTime(String loanEarliestTime)
    {
        this.loanEarliestTime = loanEarliestTime;
    }

    public String getLoanLatestTime()
    {
        return loanLatestTime;
    }

    public void setLoanLatestTime(String loanLatestTime)
    {
        this.loanLatestTime = loanLatestTime;
    }
}
