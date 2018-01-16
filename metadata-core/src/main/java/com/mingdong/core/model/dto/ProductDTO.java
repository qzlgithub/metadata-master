package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductDTO implements Serializable
{
    private Long id;
    private String name;
    private String remark;
    private Integer billPlan;
    private Integer status;
    private BigDecimal costAmt;
    private BigDecimal balance;
    private Date fromDate;
    private Date toDate;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getBillPlan()
    {
        return billPlan;
    }

    public void setBillPlan(Integer billPlan)
    {
        this.billPlan = billPlan;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public BigDecimal getCostAmt()
    {
        return costAmt;
    }

    public void setCostAmt(BigDecimal costAmt)
    {
        this.costAmt = costAmt;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public Date getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate;
    }

    public Date getToDate()
    {
        return toDate;
    }

    public void setToDate(Date toDate)
    {
        this.toDate = toDate;
    }
}
