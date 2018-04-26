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
    @Field("loan_earliest_date")
    private String loanEarliestDate;
    @Field("loan_latest_date")
    private String loanLatestDate;

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

    public String getLoanEarliestDate()
    {
        return loanEarliestDate;
    }

    public void setLoanEarliestDate(String loanEarliestDate)
    {
        this.loanEarliestDate = loanEarliestDate;
    }

    public String getLoanLatestDate()
    {
        return loanLatestDate;
    }

    public void setLoanLatestDate(String loanLatestDate)
    {
        this.loanLatestDate = loanLatestDate;
    }
}
