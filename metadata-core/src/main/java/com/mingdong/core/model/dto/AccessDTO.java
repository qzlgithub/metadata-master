package com.mingdong.core.model.dto;

import com.mingdong.core.model.dto.base.RequestDTO;

import java.math.BigDecimal;
import java.util.Date;

public class AccessDTO extends RequestDTO
{
    private Date requestAt;
    private String requestNo;
    private String corpName;
    private String username;
    private String productName;
    private Integer billPlan;
    private Integer hit;
    private BigDecimal fee;
    private BigDecimal balance;

    public Date getRequestAt()
    {
        return requestAt;
    }

    public void setRequestAt(Date requestAt)
    {
        this.requestAt = requestAt;
    }

    public String getRequestNo()
    {
        return requestNo;
    }

    public void setRequestNo(String requestNo)
    {
        this.requestNo = requestNo;
    }

    public String getCorpName()
    {
        return corpName;
    }

    public void setCorpName(String corpName)
    {
        this.corpName = corpName;
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

    public Integer getHit()
    {
        return hit;
    }

    public void setHit(Integer hit)
    {
        this.hit = hit;
    }

    public BigDecimal getFee()
    {
        return fee;
    }

    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
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
