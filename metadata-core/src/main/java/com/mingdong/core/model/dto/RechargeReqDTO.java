package com.mingdong.core.model.dto;

import com.mingdong.core.model.dto.base.RequestDTO;

import java.math.BigDecimal;
import java.util.Date;

public class RechargeReqDTO extends RequestDTO
{
    private Long clientId;
    private Long productId;
    private Long clientProductId;
    private Integer renew;
    private Date rechargeAt;
    private String rechargeNo;
    private String productName;
    private Integer rechargeType;
    private String rechargeTypeName;
    private BigDecimal amount;
    private BigDecimal balance;
    private Integer billPlan;
    private Date startDate;
    private Date endDate;
    private BigDecimal unitAmt;
    private String remark;
    private Long managerId;
    private String managerName;
    private String contractNo;

    public Integer getRenew()
    {
        return renew;
    }

    public void setRenew(Integer renew)
    {
        this.renew = renew;
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

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getClientProductId()
    {
        return clientProductId;
    }

    public void setClientProductId(Long clientProductId)
    {
        this.clientProductId = clientProductId;
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

    public Integer getRechargeType()
    {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType)
    {
        this.rechargeType = rechargeType;
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
}
