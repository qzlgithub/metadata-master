package com.mingdong.mis.mongo.entity;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

public class Overdue
{
    @Id
    private String id;
    private String phone;
    private String name;
    private String idNo;
    private BigDecimal overdueAmountMax;
    private Integer overdueDaysMax;
    private Date overdueEarliestTime;
    private Date overdueLatestTime;
    private Integer overduePlatformTotal;
    private Integer overduePlatformToday;
    private Integer overduePlatform3Days;
    private Integer overduePlatform7Days;
    private Integer overduePlatform15Days;
    private Integer overduePlatform30Days;
    private Integer overduePlatform60Days;
    private Integer overduePlatform90Days;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIdNo()
    {
        return idNo;
    }

    public void setIdNo(String idNo)
    {
        this.idNo = idNo;
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

    public Date getOverdueEarliestTime()
    {
        return overdueEarliestTime;
    }

    public void setOverdueEarliestTime(Date overdueEarliestTime)
    {
        this.overdueEarliestTime = overdueEarliestTime;
    }

    public Date getOverdueLatestTime()
    {
        return overdueLatestTime;
    }

    public void setOverdueLatestTime(Date overdueLatestTime)
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