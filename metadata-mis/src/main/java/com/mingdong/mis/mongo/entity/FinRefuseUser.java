package com.mingdong.mis.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "fin_refuse_user")
public class FinRefuseUser
{
    @Id
    private String personId;
    @Field("refuse_amount_max")
    private BigDecimal refuseAmountMax;
    @Field("refuse_earliest_date")
    private String refuseEarliestDate;
    @Field("refuse_latest_date")
    private String refuseLatestDate;
    @Field("refuse_platform_total")
    private Integer refusePlatformTotal;
    @Field("refuse_platform_today")
    private Integer refusePlatformToday;
    @Field("refuse_platform_3_days")
    private Integer refusePlatform3Days;
    @Field("refuse_platform_7_days")
    private Integer refusePlatform7Days;
    @Field("refuse_platform_15_days")
    private Integer refusePlatform15Days;
    @Field("refuse_platform_30_days")
    private Integer refusePlatform30Days;
    @Field("refuse_platform_60_days")
    private Integer refusePlatform60Days;
    @Field("refuse_platform_90_days")
    private Integer refusePlatform90Days;

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public BigDecimal getRefuseAmountMax()
    {
        return refuseAmountMax;
    }

    public void setRefuseAmountMax(BigDecimal refuseAmountMax)
    {
        this.refuseAmountMax = refuseAmountMax;
    }

    public String getRefuseEarliestDate()
    {
        return refuseEarliestDate;
    }

    public void setRefuseEarliestDate(String refuseEarliestDate)
    {
        this.refuseEarliestDate = refuseEarliestDate;
    }

    public String getRefuseLatestDate()
    {
        return refuseLatestDate;
    }

    public void setRefuseLatestDate(String refuseLatestDate)
    {
        this.refuseLatestDate = refuseLatestDate;
    }

    public Integer getRefusePlatformTotal()
    {
        return refusePlatformTotal;
    }

    public void setRefusePlatformTotal(Integer refusePlatformTotal)
    {
        this.refusePlatformTotal = refusePlatformTotal;
    }

    public Integer getRefusePlatformToday()
    {
        return refusePlatformToday;
    }

    public void setRefusePlatformToday(Integer refusePlatformToday)
    {
        this.refusePlatformToday = refusePlatformToday;
    }

    public Integer getRefusePlatform3Days()
    {
        return refusePlatform3Days;
    }

    public void setRefusePlatform3Days(Integer refusePlatform3Days)
    {
        this.refusePlatform3Days = refusePlatform3Days;
    }

    public Integer getRefusePlatform7Days()
    {
        return refusePlatform7Days;
    }

    public void setRefusePlatform7Days(Integer refusePlatform7Days)
    {
        this.refusePlatform7Days = refusePlatform7Days;
    }

    public Integer getRefusePlatform15Days()
    {
        return refusePlatform15Days;
    }

    public void setRefusePlatform15Days(Integer refusePlatform15Days)
    {
        this.refusePlatform15Days = refusePlatform15Days;
    }

    public Integer getRefusePlatform30Days()
    {
        return refusePlatform30Days;
    }

    public void setRefusePlatform30Days(Integer refusePlatform30Days)
    {
        this.refusePlatform30Days = refusePlatform30Days;
    }

    public Integer getRefusePlatform60Days()
    {
        return refusePlatform60Days;
    }

    public void setRefusePlatform60Days(Integer refusePlatform60Days)
    {
        this.refusePlatform60Days = refusePlatform60Days;
    }

    public Integer getRefusePlatform90Days()
    {
        return refusePlatform90Days;
    }

    public void setRefusePlatform90Days(Integer refusePlatform90Days)
    {
        this.refusePlatform90Days = refusePlatform90Days;
    }
}
