package com.mingdong.mis.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ApiReqInfo
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long primaryUserId;//客户主帐号id
    private Long userId;//消费者帐号id
    private Long clientId;
    private Long productId;
    private Integer hit;//成功与否
    private Integer billPlan;//消费方式
    private String requestNo;//消费单号
    private String requestIp;//连接ip
    private BigDecimal fee;//消费金额
    private BigDecimal balance;//余额
    private String corpName;
    private String shortName;
    private String username;
    private String productName;
    private String billPlanName;
    private Integer requestNumber;//请求次数-统计那边用到

    public Long getPrimaryUserId()
    {
        return primaryUserId;
    }

    public void setPrimaryUserId(Long primaryUserId)
    {
        this.primaryUserId = primaryUserId;
    }

    public Integer getRequestNumber()
    {
        return requestNumber;
    }

    public void setRequestNumber(Integer requestNumber)
    {
        this.requestNumber = requestNumber;
    }

    public String getRequestIp()
    {
        return requestIp;
    }

    public void setRequestIp(String requestIp)
    {
        this.requestIp = requestIp;
    }

    public BigDecimal getFee()
    {
        return fee;
    }

    public void setFee(BigDecimal fee)
    {
        this.fee = fee;
    }

    public String getRequestNo()
    {
        return requestNo;
    }

    public void setRequestNo(String requestNo)
    {
        this.requestNo = requestNo;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getBillPlanName()
    {
        return billPlanName;
    }

    public void setBillPlanName(String billPlanName)
    {
        this.billPlanName = billPlanName;
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

    public Integer getHit()
    {
        return hit;
    }

    public void setHit(Integer hit)
    {
        this.hit = hit;
    }

    public Integer getBillPlan()
    {
        return billPlan;
    }

    public void setBillPlan(Integer billPlan)
    {
        this.billPlan = billPlan;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
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
}
