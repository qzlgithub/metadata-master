package com.mingdong.mis.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "fin_register_user")
public class FinRegisterUser
{
    @Id
    private String personId;
    @Field("register_in_one_day_max")
    private int registerInOneDayMax;
    @Field("register_earliest_date")
    private String registerEarliestDate;
    @Field("register_latest_date")
    private String registerLatestDate;
    @Field("register_platform_total")
    private int registerPlatformTotal;
    @Field("register_platform_today")
    private int registerPlatformToday;
    @Field("register_platform_3_days")
    private int registerPlatform3Days;
    @Field("register_platform_7_days")
    private int registerPlatform7Days;
    @Field("register_platform_15_days")
    private int registerPlatform15Days;
    @Field("register_platform_30_days")
    private int registerPlatform30Days;
    @Field("register_platform_60_days")
    private int registerPlatform60Days;
    @Field("register_platform_90_days")
    private int registerPlatform90Days;

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public int getRegisterInOneDayMax()
    {
        return registerInOneDayMax;
    }

    public void setRegisterInOneDayMax(int registerInOneDayMax)
    {
        this.registerInOneDayMax = registerInOneDayMax;
    }

    public String getRegisterEarliestDate()
    {
        return registerEarliestDate;
    }

    public void setRegisterEarliestDate(String registerEarliestDate)
    {
        this.registerEarliestDate = registerEarliestDate;
    }

    public String getRegisterLatestDate()
    {
        return registerLatestDate;
    }

    public void setRegisterLatestDate(String registerLatestDate)
    {
        this.registerLatestDate = registerLatestDate;
    }

    public int getRegisterPlatformTotal()
    {
        return registerPlatformTotal;
    }

    public void setRegisterPlatformTotal(int registerPlatformTotal)
    {
        this.registerPlatformTotal = registerPlatformTotal;
    }

    public int getRegisterPlatformToday()
    {
        return registerPlatformToday;
    }

    public void setRegisterPlatformToday(int registerPlatformToday)
    {
        this.registerPlatformToday = registerPlatformToday;
    }

    public int getRegisterPlatform3Days()
    {
        return registerPlatform3Days;
    }

    public void setRegisterPlatform3Days(int registerPlatform3Days)
    {
        this.registerPlatform3Days = registerPlatform3Days;
    }

    public int getRegisterPlatform7Days()
    {
        return registerPlatform7Days;
    }

    public void setRegisterPlatform7Days(int registerPlatform7Days)
    {
        this.registerPlatform7Days = registerPlatform7Days;
    }

    public int getRegisterPlatform15Days()
    {
        return registerPlatform15Days;
    }

    public void setRegisterPlatform15Days(int registerPlatform15Days)
    {
        this.registerPlatform15Days = registerPlatform15Days;
    }

    public int getRegisterPlatform30Days()
    {
        return registerPlatform30Days;
    }

    public void setRegisterPlatform30Days(int registerPlatform30Days)
    {
        this.registerPlatform30Days = registerPlatform30Days;
    }

    public int getRegisterPlatform60Days()
    {
        return registerPlatform60Days;
    }

    public void setRegisterPlatform60Days(int registerPlatform60Days)
    {
        this.registerPlatform60Days = registerPlatform60Days;
    }

    public int getRegisterPlatform90Days()
    {
        return registerPlatform90Days;
    }

    public void setRegisterPlatform90Days(int registerPlatform90Days)
    {
        this.registerPlatform90Days = registerPlatform90Days;
    }
}
