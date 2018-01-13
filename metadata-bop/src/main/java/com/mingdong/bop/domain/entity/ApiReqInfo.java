package com.mingdong.bop.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ApiReqInfo
{
    private Long id;
    private Date callTime;
    private String corpName;
    private String shortName;
    private String username;
    private String productName;
    private Integer suc;
    private BigDecimal unitAmt;
    private BigDecimal balance;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getCallTime()
    {
        return callTime;
    }

    public void setCallTime(Date callTime)
    {
        this.callTime = callTime;
    }

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public Integer getSuc()
    {
        return suc;
    }

    public void setSuc(Integer suc)
    {
        this.suc = suc;
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
