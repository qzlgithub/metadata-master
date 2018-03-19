package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

public class RechargeStatsDTO implements Serializable
{
    private String rechargeTypeName;
    private BigDecimal amount;

    public String getRechargeTypeName()
    {
        return rechargeTypeName;
    }

    public void setRechargeTypeName(String rechargeTypeName)
    {
        this.rechargeTypeName = rechargeTypeName;
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
