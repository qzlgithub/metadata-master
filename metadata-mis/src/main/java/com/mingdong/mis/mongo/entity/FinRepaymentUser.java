package com.mingdong.mis.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "fin_repayment_user")
public class FinRepaymentUser
{
    @Id
    private String personId;
    @Field("repayment_amount_max")
    private BigDecimal repaymentAmountMax;
    @Field("repayment_days_max")
    private Integer repaymentDaysMax;
    @Field("repayment_earliest_date")
    private String repaymentEarliestDate;
    @Field("repayment_latest_date")
    private String repaymentLatestDate;
    @Field("repayment_platform_total")
    private Integer repaymentPlatformTotal;
    @Field("repayment_platform_today")
    private Integer repaymentPlatformToday;
    @Field("repayment_platform_3_days")
    private Integer repaymentPlatform3Days;
    @Field("repayment_platform_7_days")
    private Integer repaymentPlatform7Days;
    @Field("repayment_platform_15_days")
    private Integer repaymentPlatform15Days;
    @Field("repayment_platform_30_days")
    private Integer repaymentPlatform30Days;
    @Field("repayment_platform_60_days")
    private Integer repaymentPlatform60Days;
    @Field("repayment_platform_90_days")
    private Integer repaymentPlatform90Days;

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public BigDecimal getRepaymentAmountMax()
    {
        return repaymentAmountMax;
    }

    public void setRepaymentAmountMax(BigDecimal repaymentAmountMax)
    {
        this.repaymentAmountMax = repaymentAmountMax;
    }

    public Integer getRepaymentDaysMax()
    {
        return repaymentDaysMax;
    }

    public void setRepaymentDaysMax(Integer repaymentDaysMax)
    {
        this.repaymentDaysMax = repaymentDaysMax;
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

    public Integer getRepaymentPlatformTotal()
    {
        return repaymentPlatformTotal;
    }

    public void setRepaymentPlatformTotal(Integer repaymentPlatformTotal)
    {
        this.repaymentPlatformTotal = repaymentPlatformTotal;
    }

    public Integer getRepaymentPlatformToday()
    {
        return repaymentPlatformToday;
    }

    public void setRepaymentPlatformToday(Integer repaymentPlatformToday)
    {
        this.repaymentPlatformToday = repaymentPlatformToday;
    }

    public Integer getRepaymentPlatform3Days()
    {
        return repaymentPlatform3Days;
    }

    public void setRepaymentPlatform3Days(Integer repaymentPlatform3Days)
    {
        this.repaymentPlatform3Days = repaymentPlatform3Days;
    }

    public Integer getRepaymentPlatform7Days()
    {
        return repaymentPlatform7Days;
    }

    public void setRepaymentPlatform7Days(Integer repaymentPlatform7Days)
    {
        this.repaymentPlatform7Days = repaymentPlatform7Days;
    }

    public Integer getRepaymentPlatform15Days()
    {
        return repaymentPlatform15Days;
    }

    public void setRepaymentPlatform15Days(Integer repaymentPlatform15Days)
    {
        this.repaymentPlatform15Days = repaymentPlatform15Days;
    }

    public Integer getRepaymentPlatform30Days()
    {
        return repaymentPlatform30Days;
    }

    public void setRepaymentPlatform30Days(Integer repaymentPlatform30Days)
    {
        this.repaymentPlatform30Days = repaymentPlatform30Days;
    }

    public Integer getRepaymentPlatform60Days()
    {
        return repaymentPlatform60Days;
    }

    public void setRepaymentPlatform60Days(Integer repaymentPlatform60Days)
    {
        this.repaymentPlatform60Days = repaymentPlatform60Days;
    }

    public Integer getRepaymentPlatform90Days()
    {
        return repaymentPlatform90Days;
    }

    public void setRepaymentPlatform90Days(Integer repaymentPlatform90Days)
    {
        this.repaymentPlatform90Days = repaymentPlatform90Days;
    }
}
