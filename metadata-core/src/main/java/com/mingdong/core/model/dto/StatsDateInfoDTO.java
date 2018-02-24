package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.util.Date;

public class StatsDateInfoDTO implements Serializable
{
    private Date date;
    private Integer count;
    private Integer missCount;

    public Integer getMissCount()
    {
        return missCount;
    }

    public void setMissCount(Integer missCount)
    {
        this.missCount = missCount;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }
}
