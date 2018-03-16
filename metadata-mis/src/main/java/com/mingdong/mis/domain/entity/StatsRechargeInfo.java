package com.mingdong.mis.domain.entity;

import java.math.BigDecimal;

public class StatsRechargeInfo
{
    private Integer rechargeType;
    private BigDecimal amount;

    public Integer getRechargeType()
    {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType)
    {
        this.rechargeType = rechargeType;
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
