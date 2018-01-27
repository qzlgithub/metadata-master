package com.mingdong.mis.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ApiReq
{
    private Long id;
    private Long userId;
    private Long clientId;
    private Long productId;
    private Date createTime;
    private Date updateTime;
    private Integer hit;//成功与否
    private Integer billPlan;//消费方式
    private String linkIp;//连接ip
    private BigDecimal unitAmt;//单价
    private BigDecimal balance;//余额
    private String consumptionNo;//消费单号
    private String resultCode;//结果状态

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getConsumptionNo()
    {
        return consumptionNo;
    }

    public void setConsumptionNo(String consumptionNo)
    {
        this.consumptionNo = consumptionNo;
    }

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public Long getClientId()
    {
        return clientId;
    }

    public void setClientId(Long clientId)
    {
        this.clientId = clientId;
    }

    public Integer getHit()
    {
        return hit;
    }

    public void setHit(Integer hit)
    {
        this.hit = hit;
    }

    public String getLinkIp()
    {
        return linkIp;
    }

    public void setLinkIp(String linkIp)
    {
        this.linkIp = linkIp;
    }

    public Integer getBillPlan()
    {
        return billPlan;
    }

    public void setBillPlan(Integer billPlan)
    {
        this.billPlan = billPlan;
    }

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

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
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
