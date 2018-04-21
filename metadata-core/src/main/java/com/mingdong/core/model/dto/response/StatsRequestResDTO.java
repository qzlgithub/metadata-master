package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.util.Date;

public class StatsRequestResDTO implements Serializable
{
    private Long productId;
    private Long clientId;
    private Integer statsYear;
    private Integer statsMonth;
    private Integer statsWeek;
    private Integer statsDay;
    private Integer statsHour;
    private Date statsDate;
    private Long request;
    private Long requestFailed;
    private Long requestNotHit;

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getRequestFailed()
    {
        return requestFailed;
    }

    public void setRequestFailed(Long requestFailed)
    {
        this.requestFailed = requestFailed;
    }

    public Long getRequestNotHit()
    {
        return requestNotHit;
    }

    public void setRequestNotHit(Long requestNotHit)
    {
        this.requestNotHit = requestNotHit;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
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

    public Long getRequest()
    {
        return request;
    }

    public void setRequest(Long request)
    {
        this.request = request;
    }
}
