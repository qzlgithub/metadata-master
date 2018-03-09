package com.mingdong.core.model.dto;

import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.base.ResponseDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

// TODO review
public class ClientProductDTO implements Serializable
{
    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long clientId;
    private Long productId;
    private String appId;
    private Integer billPlan;
    private BigDecimal balance;
    private Long latestRechargeId;
    private Integer isOpened;
    private ResponseDTO responseDTO;

    public ClientProductDTO()
    {
        this.responseDTO = new ResponseDTO();
        responseDTO.setResult(RestResult.SUCCESS);
    }

    public Integer getIsOpened()
    {
        return isOpened;
    }

    public void setIsOpened(Integer isOpened)
    {
        this.isOpened = isOpened;
    }

    public ResponseDTO getResponseDTO()
    {
        return responseDTO;
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

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public Long getLatestRechargeId()
    {
        return latestRechargeId;
    }

    public void setLatestRechargeId(Long latestRechargeId)
    {
        this.latestRechargeId = latestRechargeId;
    }
}
