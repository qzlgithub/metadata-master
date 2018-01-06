package com.mingdong.bop.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ProdRechargeInfo
{
    private Long tradeId;
    private Date tradeAt;
    private String tradeNo;
    private String corpName;
    private String shortName;
    private String account;
    private String product;
    private String rechargeType;
    private BigDecimal amount;
    private BigDecimal balance;
    private Long managerId;
    private String managerName;
    private String contractNo;
    private String remark;

    public Long getTradeId()
    {
        return tradeId;
    }

    public void setTradeId(Long tradeId)
    {
        this.tradeId = tradeId;
    }

    public Date getTradeAt()
    {
        return tradeAt;
    }

    public void setTradeAt(Date tradeAt)
    {
        this.tradeAt = tradeAt;
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

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
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

    public Long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(Long managerId)
    {
        this.managerId = managerId;
    }

    public String getManagerName()
    {
        return managerName;
    }

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
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
