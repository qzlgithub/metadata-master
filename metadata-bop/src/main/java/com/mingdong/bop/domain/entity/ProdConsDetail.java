package com.mingdong.bop.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 封装产品消费明细页面数据
 */
public class ProdConsDetail
{   // 记录添加时间
    private Date createTime;
    // 消费单号
    private String tradeNo;
    // 企业名称
    private String name;
    // 企业简称
    private String shortName;
    // 企业账户名
    private String username;
    // 产品服务(名称)
    private String productName;
    // 计费方式
    private Integer billPlan;
    // 是否成功
    private Integer suc;
    // 单价
    private BigDecimal unitAmt;
    // 产品账户余额
    private BigDecimal balance;

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public Integer getBillPlan()
    {
        return billPlan;
    }

    public void setBillPlan(Integer billPlan)
    {
        this.billPlan = billPlan;
    }

    public Integer getSuc()
    {
        return suc;
    }

    public void setSuc(Integer suc)
    {
        this.suc = suc;
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
