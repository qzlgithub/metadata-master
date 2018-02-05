package com.mingdong.mis.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Product
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Integer type;
    private String code;
    private String name;
    private BigDecimal costAmt;
    private String remark;
    private Integer enabled;
    private Integer custom;

    public Integer getCustom()
    {
        return custom;
    }

    public void setCustom(Integer custom)
    {
        this.custom = custom;
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

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public BigDecimal getCostAmt()
    {
        return costAmt;
    }

    public void setCostAmt(BigDecimal costAmt)
    {
        this.costAmt = costAmt;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }
}


