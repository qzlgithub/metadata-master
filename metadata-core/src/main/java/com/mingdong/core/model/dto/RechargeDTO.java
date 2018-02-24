package com.mingdong.core.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class RechargeDTO
{
    private Date rechargeAt;
    private String rechargeNo;
    private String productName;
    private String rechargeTypeName;
    private BigDecimal amount;
    private BigDecimal balance;
    private String managerName;
    private String contractNo;

    public Date getRechargeAt()
    {
        return rechargeAt;
    }

    public void setRechargeAt(Date rechargeAt)
    {
        this.rechargeAt = rechargeAt;
    }

    public String getRechargeNo()
    {
        return rechargeNo;
    }

    public void setRechargeNo(String rechargeNo)
    {
        this.rechargeNo = rechargeNo;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

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

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
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
}
