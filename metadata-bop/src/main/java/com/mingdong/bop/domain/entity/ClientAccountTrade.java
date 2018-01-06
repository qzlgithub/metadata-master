package com.mingdong.bop.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ClientAccountTrade
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String tradeNo;
    private Integer income;
    private Integer tradeType;
    private BigDecimal amount;
    private BigDecimal balance;
    private Long userId;
    private String remark;

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

    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public Integer getIncome()
    {
        return income;
    }

    public void setIncome(Integer income)
    {
        this.income = income;
    }

    public Integer getTradeType()
    {
        return tradeType;
    }

    public void setTradeType(Integer tradeType)
    {
        this.tradeType = tradeType;
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

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}


