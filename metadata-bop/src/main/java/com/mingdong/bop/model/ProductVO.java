package com.mingdong.bop.model;

import java.math.BigDecimal;

public class ProductVO
{
    private Long id;
    private Long productType;
    private String code;
    private String name;
    private BigDecimal costAmt;
    private Integer enabled;
    private String remark;
    private String content;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getProductType()
    {
        return productType;
    }

    public void setProductType(Long productType)
    {
        this.productType = productType;
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

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
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
}
