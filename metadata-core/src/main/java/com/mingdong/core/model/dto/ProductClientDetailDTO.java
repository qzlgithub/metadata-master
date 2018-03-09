package com.mingdong.core.model.dto;

import com.mingdong.core.model.dto.base.RequestDTO;

import java.math.BigDecimal;
import java.util.Date;

public class ProductClientDetailDTO extends RequestDTO
{
    private Long productId;
    private Integer custom;
    private Integer productType;
    private String code;
    private String remark;
    private Long clientProductId;
    private Integer isOpened;
    private String name;
    private String appId;
    private Integer billPlan;
    private Date fromDate;
    private Date toDate;
    private BigDecimal amount;
    private BigDecimal unitAmt;
    private BigDecimal balance;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Integer getProductType()
    {
        return productType;
    }

    public void setProductType(Integer productType)
    {
        this.productType = productType;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getCustom()
    {
        return custom;
    }

    public void setCustom(Integer custom)
    {
        this.custom = custom;
    }

    public Integer getIsOpened()
    {
        return isOpened;
    }

    public void setIsOpened(Integer isOpened)
    {
        this.isOpened = isOpened;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public Integer getBillPlan()
    {
        return billPlan;
    }

    public void setBillPlan(Integer billPlan)
    {
        this.billPlan = billPlan;
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

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
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
