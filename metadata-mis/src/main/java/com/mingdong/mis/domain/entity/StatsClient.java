package com.mingdong.mis.domain.entity;

import java.util.Date;

public class StatsClient
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String statsDay;
    private Integer statsHour;
    private Integer incQty;

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

    public String getStatsDay()
    {
        return statsDay;
    }

    public void setStatsDay(String statsDay)
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

    public Integer getIncQty()
    {
        return incQty;
    }

    public void setIncQty(Integer incQty)
    {
        this.incQty = incQty;
    }
}
