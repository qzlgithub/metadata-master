package com.mingdong.core.model.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StatsDTO implements Serializable
{
    private Integer statsYear;
    private Integer statsMonth;
    private Integer statsWeek;
    private Integer statsDay;
    private Integer statsHour;
    private Date statsDate;
    private Integer clientIncrement;
    private Long clientRequest;
    private BigDecimal clientRecharge;

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

    public Long getClientRequest()
    {
        return clientRequest;
    }

    public void setClientRequest(Long clientRequest)
    {
        this.clientRequest = clientRequest;
    }

    public BigDecimal getClientRecharge()
    {
        return clientRecharge;
    }

    public void setClientRecharge(BigDecimal clientRecharge)
    {
        this.clientRecharge = clientRecharge;
    }
}
