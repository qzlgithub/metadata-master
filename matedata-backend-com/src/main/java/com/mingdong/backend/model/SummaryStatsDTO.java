package com.mingdong.backend.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class SummaryStatsDTO implements Serializable
{
    private Integer clientIncIn30Days;
    private Integer clientIncIn7Days;
    private Integer clientIncThisMonth;
    private Integer clientTotal;
    private BigDecimal rechargeAmountToday;
    private BigDecimal rechargeAmountIn7Days;
    private BigDecimal rechargeAmountIn30Days;
    private BigDecimal rechargeAmountThisMonth;
    private BigDecimal rechargeAmountTotal;
    private BigDecimal profitAmountToday;
    private BigDecimal profitAmountYesterday;
    private Long requestToday;
    private Long requestYesterday;
    private Long requestThisMonth;
    private Long requestTotal;
    private Long requestFailedToday;
    private Long requestFailedYesterday;
    private Long requestFailedThisMonth;
    private Long requestFailedTotal;
    private Long request3rdFailedToday;
    private Long request3rdFailedYesterday;
    private Long request3rdFailedThisMonth;
    private Long request3rdFailedTotal;
    private Long requestNotHitToday;
    private Long requestNotHitYesterday;
    private Long requestNotHitThisMonth;
    private Long requestNotHitTotal;

    public Long getRequestNotHitToday()
    {
        return requestNotHitToday;
    }

    public void setRequestNotHitToday(Long requestNotHitToday)
    {
        this.requestNotHitToday = requestNotHitToday;
    }

    public Long getRequestNotHitYesterday()
    {
        return requestNotHitYesterday;
    }

    public void setRequestNotHitYesterday(Long requestNotHitYesterday)
    {
        this.requestNotHitYesterday = requestNotHitYesterday;
    }

    public Long getRequestNotHitThisMonth()
    {
        return requestNotHitThisMonth;
    }

    public void setRequestNotHitThisMonth(Long requestNotHitThisMonth)
    {
        this.requestNotHitThisMonth = requestNotHitThisMonth;
    }

    public Long getRequestNotHitTotal()
    {
        return requestNotHitTotal;
    }

    public void setRequestNotHitTotal(Long requestNotHitTotal)
    {
        this.requestNotHitTotal = requestNotHitTotal;
    }

    public Integer getClientIncIn7Days()
    {
        return clientIncIn7Days;
    }

    public void setClientIncIn7Days(Integer clientIncIn7Days)
    {
        this.clientIncIn7Days = clientIncIn7Days;
    }

    public Integer getClientIncThisMonth()
    {
        return clientIncThisMonth;
    }

    public void setClientIncThisMonth(Integer clientIncThisMonth)
    {
        this.clientIncThisMonth = clientIncThisMonth;
    }

    public BigDecimal getRechargeAmountToday()
    {
        return rechargeAmountToday;
    }

    public void setRechargeAmountToday(BigDecimal rechargeAmountToday)
    {
        this.rechargeAmountToday = rechargeAmountToday;
    }

    public BigDecimal getRechargeAmountThisMonth()
    {
        return rechargeAmountThisMonth;
    }

    public void setRechargeAmountThisMonth(BigDecimal rechargeAmountThisMonth)
    {
        this.rechargeAmountThisMonth = rechargeAmountThisMonth;
    }

    public BigDecimal getRechargeAmountTotal()
    {
        return rechargeAmountTotal;
    }

    public void setRechargeAmountTotal(BigDecimal rechargeAmountTotal)
    {
        this.rechargeAmountTotal = rechargeAmountTotal;
    }

    public Integer getClientIncIn30Days()
    {
        return clientIncIn30Days;
    }

    public void setClientIncIn30Days(Integer clientIncIn30Days)
    {
        this.clientIncIn30Days = clientIncIn30Days;
    }

    public Integer getClientTotal()
    {
        return clientTotal;
    }

    public void setClientTotal(Integer clientTotal)
    {
        this.clientTotal = clientTotal;
    }

    public BigDecimal getRechargeAmountIn7Days()
    {
        return rechargeAmountIn7Days;
    }

    public void setRechargeAmountIn7Days(BigDecimal rechargeAmountIn7Days)
    {
        this.rechargeAmountIn7Days = rechargeAmountIn7Days;
    }

    public BigDecimal getRechargeAmountIn30Days()
    {
        return rechargeAmountIn30Days;
    }

    public void setRechargeAmountIn30Days(BigDecimal rechargeAmountIn30Days)
    {
        this.rechargeAmountIn30Days = rechargeAmountIn30Days;
    }

    public BigDecimal getProfitAmountToday()
    {
        return profitAmountToday;
    }

    public void setProfitAmountToday(BigDecimal profitAmountToday)
    {
        this.profitAmountToday = profitAmountToday;
    }

    public BigDecimal getProfitAmountYesterday()
    {
        return profitAmountYesterday;
    }

    public void setProfitAmountYesterday(BigDecimal profitAmountYesterday)
    {
        this.profitAmountYesterday = profitAmountYesterday;
    }

    public Long getRequestToday()
    {
        return requestToday;
    }

    public void setRequestToday(Long requestToday)
    {
        this.requestToday = requestToday;
    }

    public Long getRequestYesterday()
    {
        return requestYesterday;
    }

    public void setRequestYesterday(Long requestYesterday)
    {
        this.requestYesterday = requestYesterday;
    }

    public Long getRequestThisMonth()
    {
        return requestThisMonth;
    }

    public void setRequestThisMonth(Long requestThisMonth)
    {
        this.requestThisMonth = requestThisMonth;
    }

    public Long getRequestTotal()
    {
        return requestTotal;
    }

    public void setRequestTotal(Long requestTotal)
    {
        this.requestTotal = requestTotal;
    }

    public Long getRequestFailedToday()
    {
        return requestFailedToday;
    }

    public void setRequestFailedToday(Long requestFailedToday)
    {
        this.requestFailedToday = requestFailedToday;
    }

    public Long getRequestFailedYesterday()
    {
        return requestFailedYesterday;
    }

    public void setRequestFailedYesterday(Long requestFailedYesterday)
    {
        this.requestFailedYesterday = requestFailedYesterday;
    }

    public Long getRequestFailedThisMonth()
    {
        return requestFailedThisMonth;
    }

    public void setRequestFailedThisMonth(Long requestFailedThisMonth)
    {
        this.requestFailedThisMonth = requestFailedThisMonth;
    }

    public Long getRequestFailedTotal()
    {
        return requestFailedTotal;
    }

    public void setRequestFailedTotal(Long requestFailedTotal)
    {
        this.requestFailedTotal = requestFailedTotal;
    }

    public Long getRequest3rdFailedToday()
    {
        return request3rdFailedToday;
    }

    public void setRequest3rdFailedToday(Long request3rdFailedToday)
    {
        this.request3rdFailedToday = request3rdFailedToday;
    }

    public Long getRequest3rdFailedYesterday()
    {
        return request3rdFailedYesterday;
    }

    public void setRequest3rdFailedYesterday(Long request3rdFailedYesterday)
    {
        this.request3rdFailedYesterday = request3rdFailedYesterday;
    }

    public Long getRequest3rdFailedThisMonth()
    {
        return request3rdFailedThisMonth;
    }

    public void setRequest3rdFailedThisMonth(Long request3rdFailedThisMonth)
    {
        this.request3rdFailedThisMonth = request3rdFailedThisMonth;
    }

    public Long getRequest3rdFailedTotal()
    {
        return request3rdFailedTotal;
    }

    public void setRequest3rdFailedTotal(Long request3rdFailedTotal)
    {
        this.request3rdFailedTotal = request3rdFailedTotal;
    }
}
