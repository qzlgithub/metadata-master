package com.mingdong.mis.mongo.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "fin_refuse_platform")
public class FinRefusePlatform
{
    @Field("person_id")
    private String personId;
    @Field("platform_code")
    private String platformCode;
    @Field("platform_type")
    private String platformType;
    @Field("refuse_earliest_time")
    private String refuseEarliestTime;
    @Field("refuse_latest_time")
    private String refuseLatestTime;

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
