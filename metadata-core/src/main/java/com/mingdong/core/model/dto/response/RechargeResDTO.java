package com.mingdong.core.model.dto.response;

import java.math.BigDecimal;
import java.util.Date;

public class RechargeResDTO extends ResponseDTO
{
    private Integer billPlan;
    private BigDecimal amount;
    private BigDecimal balance;
    private BigDecimal unitAmt;
    private Date startDate;
    private Date endDate;
    private Date rechargeAt;
    private String rechargeNo;
    private String productName;
    private String rechargeTypeName;
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

}
