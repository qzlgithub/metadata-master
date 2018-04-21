package com.mingdong.backend.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class StatsSummary
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Integer statsYear;
    private Integer statsMonth;
    private Integer statsWeek;
    private Integer statsDay;
    private Integer statsHour;
    private Date statsDate;
    private Integer clientIncrement;
    private Long request;
    private Long requestFailed;
    private Long request3rdFailed;
    private Long requestNotHit;
    private BigDecimal recharge;
    private BigDecimal profit;

    public Long getRequestNotHit()
    {
        return requestNotHit;
    }

    public void setRequestNotHit(Long requestNotHit)
    {
        this.requestNotHit = requestNotHit;
    }

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

    public Integer getClientIncrement()
    {
        return clientIncrement;
    }

    public void setClientIncrement(Integer clientIncrement)
    {
        this.clientIncrement = clientIncrement;
    }

    public Long getRequest()
    {
        return request;
    }

    public void setRequest(Long request)
    {
        this.request = request;
    }

    public Long getRequestFailed()
    {
        return requestFailed;
    }

    public void setRequestFailed(Long requestFailed)
    {
        this.requestFailed = requestFailed;
    }

    public Long getRequest3rdFailed()
    {
        return request3rdFailed;
    }

    public void setRequest3rdFailed(Long request3rdFailed)
    {
        this.request3rdFailed = request3rdFailed;
    }

    public BigDecimal getRecharge()
    {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge)
    {
        this.recharge = recharge;
    }

    public BigDecimal getProfit()
    {
        return profit;
    }

    public void setProfit(BigDecimal profit)
    {
        this.profit = profit;
    }
}
