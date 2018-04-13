package com.mingdong.backend.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class StatsRecharge
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Integer rechargeType;
    private String rechargeTypeName;
    private Integer statsYear;
    private Integer statsMonth;
    private Integer statsWeek;
    private Integer statsDay;
    private Integer statsHour;
    private Date statsDate;
    private BigDecimal amount;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public Integer getRechargeType()
    {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType)
    {
        this.rechargeType = rechargeType;
    }

    public String getRechargeTypeName()
    {
        return rechargeTypeName;
    }

    public void setRechargeTypeName(String rechargeTypeName)
    {
        this.rechargeTypeName = rechargeTypeName;
    }

    public Integer getStatsYear()
    {
        return statsYear;
    }

    public void setStatsYear(Integer statsYear)
    {
        this.statsYear = statsYear;
    }

    public Integer getStatsMonth()
    {
        return statsMonth;
    }

    public void setStatsMonth(Integer statsMonth)
    {
        this.statsMonth = statsMonth;
    }

    public Integer getStatsWeek()
    {
        return statsWeek;
    }

    public void setStatsWeek(Integer statsWeek)
    {
        this.statsWeek = statsWeek;
    }

    public Integer getStatsDay()
    {
        return statsDay;
    }

    public void setStatsDay(Integer statsDay)
    {
        this.statsDay = statsDay;
    }

    public Integer getStatsHour()
    {
        return statsHour;
    }

    public void setStatsHour(Integer statsHour)
    {
        this.statsHour = statsHour;
    }

    public Date getStatsDate()
    {
        return statsDate;
    }

    public void setStatsDate(Date statsDate)
    {
        this.statsDate = statsDate;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}
