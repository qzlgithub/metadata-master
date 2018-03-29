package com.mingdong.mis.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "fin_overdue_user")
public class FinOverdueUser
{
    @Id
    private String id;
    @Field("overdue_amount_max")
    private BigDecimal overdueAmountMax;
    @Field("overdue_days_max")
    private Integer overdueDaysMax;
    @Field("overdue_earliest_time")
    private String overdueEarliestTime;
    @Field("overdue_latest_time")
    private String overdueLatestTime;
    @Field("overdue_platform_total")
    private Integer overduePlatformTotal;
    @Field("overdue_platform_today")
    private Integer overduePlatformToday;
    @Field("overdue_platform_3_days")
    private Integer overduePlatform3Days;
    @Field("overdue_platform_7_days")
    private Integer overduePlatform7Days;
    @Field("overdue_platform_15_days")
    private Integer overduePlatform15Days;
    @Field("overdue_platform_30_days")
    private Integer overduePlatform30Days;
    @Field("overdue_platform_60_days")
    private Integer overduePlatform60Days;
    @Field("overdue_platform_90_days")
    private Integer overduePlatform90Days;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public BigDecimal getOverdueAmountMax()
    {
        return overdueAmountMax;
    }

    public void setOverdueAmountMax(BigDecimal overdueAmountMax)
    {
        this.overdueAmountMax = overdueAmountMax;
    }

    public Integer getOverdueDaysMax()
    {
        return overdueDaysMax;
    }

    public void setOverdueDaysMax(Integer overdueDaysMax)
    {
        this.overdueDaysMax = overdueDaysMax;
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

    public Integer getOverduePlatformTotal()
    {
        return overduePlatformTotal;
    }

    public void setOverduePlatformTotal(Integer overduePlatformTotal)
    {
        this.overduePlatformTotal = overduePlatformTotal;
    }

    public Integer getOverduePlatformToday()
    {
        return overduePlatformToday;
    }

    public void setOverduePlatformToday(Integer overduePlatformToday)
    {
        this.overduePlatformToday = overduePlatformToday;
    }

    public Integer getOverduePlatform3Days()
    {
        return overduePlatform3Days;
    }

    public void setOverduePlatform3Days(Integer overduePlatform3Days)
    {
        this.overduePlatform3Days = overduePlatform3Days;
    }

    public Integer getOverduePlatform7Days()
    {
        return overduePlatform7Days;
    }

    public void setOverduePlatform7Days(Integer overduePlatform7Days)
    {
        this.overduePlatform7Days = overduePlatform7Days;
    }

    public Integer getOverduePlatform15Days()
    {
        return overduePlatform15Days;
    }

    public void setOverduePlatform15Days(Integer overduePlatform15Days)
    {
        this.overduePlatform15Days = overduePlatform15Days;
    }

    public Integer getOverduePlatform30Days()
    {
        return overduePlatform30Days;
    }

    public void setOverduePlatform30Days(Integer overduePlatform30Days)
    {
        this.overduePlatform30Days = overduePlatform30Days;
    }

    public Integer getOverduePlatform60Days()
    {
        return overduePlatform60Days;
    }

    public void setOverduePlatform60Days(Integer overduePlatform60Days)
    {
        this.overduePlatform60Days = overduePlatform60Days;
    }

    public Integer getOverduePlatform90Days()
    {
        return overduePlatform90Days;
    }

    public void setOverduePlatform90Days(Integer overduePlatform90Days)
    {
        this.overduePlatform90Days = overduePlatform90Days;
    }
}