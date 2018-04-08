package com.mingdong.mis.mongo.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "fin_overdue_platform")
public class FinOverduePlatform
{
    @Field("person_id")
    private String personId;
    @Field("platform_code")
    private String platformCode;
    @Field("platform_type")
    private String platformType;
    @Field("overdue_earliest_time")
    private String overdueEarliestTime;
    @Field("overdue_latest_time")
    private String overdueLatestTime;

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

    public String getOverdueEarliestTime()
    {
        return overdueEarliestTime;
    }

    public void setOverdueEarliestTime(String overdueEarliestTime)
    {
        this.overdueEarliestTime = overdueEarliestTime;
    }

    public String getOverdueLatestTime()
    {
        return overdueLatestTime;
    }

    public void setOverdueLatestTime(String overdueLatestTime)
    {
        this.overdueLatestTime = overdueLatestTime;
    }
}
