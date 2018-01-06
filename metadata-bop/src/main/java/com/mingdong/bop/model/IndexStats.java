package com.mingdong.bop.model;

import java.math.BigDecimal;

public class IndexStats
{
    private long monthClientAddQty;
    private long totalClientQty;
    private BigDecimal rechargeAmtLatest7Days;
    private BigDecimal rechargeAmtLatest30Days;
    private BigDecimal todayIncomeAmt;
    private BigDecimal yesterdayIncomeAmt;
    private long todayReq;
    private long yesterdayReq;
    private long thisMonthReq;
    private long totalReq;
    private long todayFailReq;
    private long yesterdayFailReq;
    private long thisMonthFailReq;
    private long totalFailReq;

    public long getMonthClientAddQty()
    {
        return monthClientAddQty;
    }

    public void setMonthClientAddQty(long monthClientAddQty)
    {
        this.monthClientAddQty = monthClientAddQty;
    }

    public long getTotalClientQty()
    {
        return totalClientQty;
    }

    public void setTotalClientQty(long totalClientQty)
    {
        this.totalClientQty = totalClientQty;
    }

    public BigDecimal getRechargeAmtLatest7Days()
    {
        return rechargeAmtLatest7Days;
    }

    public void setRechargeAmtLatest7Days(BigDecimal rechargeAmtLatest7Days)
    {
        this.rechargeAmtLatest7Days = rechargeAmtLatest7Days;
    }

    public BigDecimal getRechargeAmtLatest30Days()
    {
        return rechargeAmtLatest30Days;
    }

    public void setRechargeAmtLatest30Days(BigDecimal rechargeAmtLatest30Days)
    {
        this.rechargeAmtLatest30Days = rechargeAmtLatest30Days;
    }

    public BigDecimal getTodayIncomeAmt()
    {
        return todayIncomeAmt;
    }

    public void setTodayIncomeAmt(BigDecimal todayIncomeAmt)
    {
        this.todayIncomeAmt = todayIncomeAmt;
    }

    public BigDecimal getYesterdayIncomeAmt()
    {
        return yesterdayIncomeAmt;
    }

    public void setYesterdayIncomeAmt(BigDecimal yesterdayIncomeAmt)
    {
        this.yesterdayIncomeAmt = yesterdayIncomeAmt;
    }

    public long getTodayReq()
    {
        return todayReq;
    }

    public void setTodayReq(long todayReq)
    {
        this.todayReq = todayReq;
    }

    public long getYesterdayReq()
    {
        return yesterdayReq;
    }

    public void setYesterdayReq(long yesterdayReq)
    {
        this.yesterdayReq = yesterdayReq;
    }

    public long getThisMonthReq()
    {
        return thisMonthReq;
    }

    public void setThisMonthReq(long thisMonthReq)
    {
        this.thisMonthReq = thisMonthReq;
    }

    public long getTotalReq()
    {
        return totalReq;
    }

    public void setTotalReq(long totalReq)
    {
        this.totalReq = totalReq;
    }

    public long getTodayFailReq()
    {
        return todayFailReq;
    }

    public void setTodayFailReq(long todayFailReq)
    {
        this.todayFailReq = todayFailReq;
    }

    public long getYesterdayFailReq()
    {
        return yesterdayFailReq;
    }

    public void setYesterdayFailReq(long yesterdayFailReq)
    {
        this.yesterdayFailReq = yesterdayFailReq;
    }

    public long getThisMonthFailReq()
    {
        return thisMonthFailReq;
    }

    public void setThisMonthFailReq(long thisMonthFailReq)
    {
        this.thisMonthFailReq = thisMonthFailReq;
    }

    public long getTotalFailReq()
    {
        return totalFailReq;
    }

    public void setTotalFailReq(long totalFailReq)
    {
        this.totalFailReq = totalFailReq;
    }
}
