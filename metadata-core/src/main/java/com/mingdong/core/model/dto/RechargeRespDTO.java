package com.mingdong.core.model.dto;

import com.mingdong.core.model.dto.base.ResponseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class RechargeRespDTO extends ResponseDTO
{
    private Integer billPlan;
    private BigDecimal amount;
    private BigDecimal balance;
    private BigDecimal unitAmt;
    private Date startDate;
    private Date endDate;
    private BigDecimal totalRecharge;

    public Integer getBillPlan()
    {
        return billPlan;
    }

    public void setBillPlan(Integer billPlan)
    {
        this.billPlan = billPlan;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public BigDecimal getUnitAmt()
    {
        return unitAmt;
    }

    public void setUnitAmt(BigDecimal unitAmt)
    {
        this.unitAmt = unitAmt;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public BigDecimal getTotalRecharge()
    {
        return totalRecharge;
    }

    public void setTotalRecharge(BigDecimal totalRecharge)
    {
        this.totalRecharge = totalRecharge;
    }
}
