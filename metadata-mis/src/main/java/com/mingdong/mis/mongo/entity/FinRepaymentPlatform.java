package com.mingdong.mis.mongo.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "fin_repayment_platform")
public class FinRepaymentPlatform
{
    @Field("person_id")
    private String personId;
    @Field("platform_code")
    private String platformCode;
    @Field("platform_type")
    private String platformType;
    @Field("repayment_earliest_date")
    private String repaymentEarliestDate;
    @Field("repayment_latest_date")
    private String repaymentLatestDate;

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

    public String getRepaymentEarliestDate()
    {
        return repaymentEarliestDate;
    }

    public void setRepaymentEarliestDate(String repaymentEarliestDate)
    {
        this.repaymentEarliestDate = repaymentEarliestDate;
    }

    public String getRepaymentLatestDate()
    {
        return repaymentLatestDate;
    }

    public void setRepaymentLatestDate(String repaymentLatestDate)
    {
        this.repaymentLatestDate = repaymentLatestDate;
    }
}
