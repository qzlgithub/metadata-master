package com.mingdong.mis.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class StatsDateInfo
{
    private Date date;
    //request
    private Integer count;
    private Integer missCount;
    //revenue
    private BigDecimal fee;

    public BigDecimal getFee()
    {
        return fee;
    }

    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }

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
