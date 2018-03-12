package com.mingdong.core.model.dto.response;

import java.math.BigDecimal;
import java.util.Date;

public class ProductResDTO extends ResponseDTO
{
    private Long id;
    private String name;
    private String typeName;
    private String remark;
    private String content;
    private Integer billPlan;
    private Integer status;
    private BigDecimal costAmt;
    private BigDecimal balance;
    private Date arrearTime;
    private Date fromDate;
    private Date toDate;
    private String productCode;
    private Integer type;
    private Integer opened;
    private Integer enabled;
    private Integer custom;
    private Date createTime;

    public Integer getCustom()
    {
        return custom;
    }

    public void setCustom(Integer custom)
    {
        this.custom = custom;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

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

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
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

    public Date getArrearTime()
    {
        return arrearTime;
    }

    public void setArrearTime(Date arrearTime)
    {
        this.arrearTime = arrearTime;
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

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getOpened()
    {
        return opened;
    }

    public void setOpened(Integer opened)
    {
        this.opened = opened;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}
