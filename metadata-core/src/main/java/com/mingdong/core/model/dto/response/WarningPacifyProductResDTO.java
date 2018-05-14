package com.mingdong.core.model.dto.response;

import java.io.Serializable;

public class WarningPacifyProductResDTO implements Serializable
{
    private Long pacifyId;
    private Long productId;
    private String productName;

    public Long getPacifyId()
    {
        return pacifyId;
    }

    public void setPacifyId(Long pacifyId)
    {
        this.pacifyId = pacifyId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
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
