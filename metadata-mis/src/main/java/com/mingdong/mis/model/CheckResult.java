package com.mingdong.mis.model;

import com.mingdong.mis.constant.MDResult;

import java.math.BigDecimal;

public class CheckResult
{
    private MDResult result;
    private Long clientProductId;
    private BigDecimal unitAmt;
    private BigDecimal balance;

    public CheckResult()
    {
        result = MDResult.OK;
    }

    public MDResult getResult()
    {
        return result;
    }

    public void setResult(MDResult result)
    {
        this.result = result;
    }

    public Long getClientProductId()
    {
        return clientProductId;
    }

    public void setClientProductId(Long clientProductId)
    {
        this.clientProductId = clientProductId;
    }

    public BigDecimal getUnitAmt()
    {
        return unitAmt;
    }

    public void setUnitAmt(BigDecimal unitAmt)
    {
        this.unitAmt = unitAmt;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }
}
