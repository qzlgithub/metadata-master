package com.mingdong.mis.model.metadata;

public class ReportBO
{
    //黑名单
    private Integer blacklistCode;
    //常欠客
    private Integer overdueCode;
    private OverdueBO overdueRes;
    //多头客
    private Integer multiRegisterCode;
    private MultiRegisterBO multiRegisterRes;
    //通过客
    private Integer creditableCode;
    private LoanBO creditableRes;
    //优良客
    private Integer favourableCode;
    private RepaymentBO favourableRes;
    //拒贷客
    private Integer rejecteeCode;
    private RefuseBO rejecteeRes;

    public Integer getBlacklistCode()
    {
        return blacklistCode;
    }

    public void setBlacklistCode(Integer blacklistCode)
    {
        this.blacklistCode = blacklistCode;
    }

    public Integer getOverdueCode()
    {
        return overdueCode;
    }

    public void setOverdueCode(Integer overdueCode)
    {
        this.overdueCode = overdueCode;
    }

    public OverdueBO getOverdueRes()
    {
        return overdueRes;
    }

    public void setOverdueRes(OverdueBO overdueRes)
    {
        this.overdueRes = overdueRes;
    }

    public Integer getMultiRegisterCode()
    {
        return multiRegisterCode;
    }

    public void setMultiRegisterCode(Integer multiRegisterCode)
    {
        this.multiRegisterCode = multiRegisterCode;
    }

    public MultiRegisterBO getMultiRegisterRes()
    {
        return multiRegisterRes;
    }

    public void setMultiRegisterRes(MultiRegisterBO multiRegisterRes)
    {
        this.multiRegisterRes = multiRegisterRes;
    }

    public Integer getCreditableCode()
    {
        return creditableCode;
    }

    public void setCreditableCode(Integer creditableCode)
    {
        this.creditableCode = creditableCode;
    }

    public LoanBO getCreditableRes()
    {
        return creditableRes;
    }

    public void setCreditableRes(LoanBO creditableRes)
    {
        this.creditableRes = creditableRes;
    }

    public Integer getFavourableCode()
    {
        return favourableCode;
    }

    public void setFavourableCode(Integer favourableCode)
    {
        this.favourableCode = favourableCode;
    }

    public RepaymentBO getFavourableRes()
    {
        return favourableRes;
    }

    public void setFavourableRes(RepaymentBO favourableRes)
    {
        this.favourableRes = favourableRes;
    }

    public Integer getRejecteeCode()
    {
        return rejecteeCode;
    }

    public void setRejecteeCode(Integer rejecteeCode)
    {
        this.rejecteeCode = rejecteeCode;
    }

    public RefuseBO getRejecteeRes()
    {
        return rejecteeRes;
    }

    public void setRejecteeRes(RefuseBO rejecteeRes)
    {
        this.rejecteeRes = rejecteeRes;
    }
}
