package com.mingdong.core.model.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AccessResDTO implements Serializable
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
    private String shortName;
    private String primaryUsername;
    private Long userId;//消费者帐号id

    public String getPrimaryUsername()
    {
        return primaryUsername;
    }

    public void setPrimaryUsername(String primaryUsername)
    {
        this.primaryUsername = primaryUsername;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

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
