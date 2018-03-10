package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RechargeResDTO implements Serializable
{
    private Date rechargeAt;
    private String rechargeNo;
    private String productName;
    private String rechargeTypeName;
    private BigDecimal amount;
    private BigDecimal balance;
    private Integer billPlan;
    private Date startDate;
    private Date endDate;
    private BigDecimal unitAmt;
    private String remark;
    private String managerName;
    private String contractNo;

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

    public BigDecimal getUnitAmt()
    {
        return unitAmt;
    }

    public void setUnitAmt(BigDecimal unitAmt)
    {
        this.unitAmt = unitAmt;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

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
