package com.mingdong.mis.mongo.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "fin_loan_platform")
public class FinLoanPlatform
{
    @Field("person_id")
    private String personId;
    @Field("platform_code")
    private String platformCode;
    @Field("platform_type")
    private String platformType;
    @Field("loan_earliest_time")
    private String loanEarliestTime;
    @Field("loan_latest_time")
    private String loanLatestTime;

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
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
