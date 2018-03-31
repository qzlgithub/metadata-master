package com.mingdong.mis.model;

import com.mingdong.mis.constant.MDResult;

import java.math.BigDecimal;

public class CheckResult
{
    private MDResult result;
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
