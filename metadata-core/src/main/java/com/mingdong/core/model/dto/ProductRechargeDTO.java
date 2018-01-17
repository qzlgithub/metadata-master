package com.mingdong.core.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductRechargeDTO implements Serializable
{
    private Long id;
    private Date tradeTime;
    private String tradeNo;
    private String corpName;
    private String shortName;
    private String username;
    private String productName;
    private Integer billPlan;
    private String rechargeType;
    private BigDecimal amount;
    private BigDecimal balance;
    private String contractNo;
    private String remark;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Date getTradeTime()
    {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime)
    {
        this.tradeTime = tradeTime;
    }

    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
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

    public Integer getBillPlan()
    {
        return billPlan;
    }

    public void setBillPlan(Integer billPlan)
    {
        this.billPlan = billPlan;
    }

    public String getRechargeType()
    {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType)
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

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public String getContractNo()
    {
        return contractNo;
    }

    public void setContractNo(String contractNo)
    {
        this.contractNo = contractNo;
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
