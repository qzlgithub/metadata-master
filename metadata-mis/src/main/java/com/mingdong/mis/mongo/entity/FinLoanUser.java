package com.mingdong.mis.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "fin_loan_user")
public class FinLoanUser
{
    @Id
    private String personId;
    @Field("loan_amount_max")
    private BigDecimal loanAmountMax;
    @Field("loan_earliest_date")
    private String loanEarliestDate;
    @Field("loan_latest_date")
    private String loanLatestDate;
    @Field("loan_platform_total")
    private Integer loanPlatformTotal;
    @Field("loan_platform_today")
    private Integer loanPlatformToday;
    @Field("loan_platform_3_days")
    private Integer loanPlatform3Days;
    @Field("loan_platform_7_days")
    private Integer loanPlatform7Days;
    @Field("loan_platform_15_days")
    private Integer loanPlatform15Days;
    @Field("loan_platform_30_days")
    private Integer loanPlatform30Days;
    @Field("loan_platform_60_days")
    private Integer loanPlatform60Days;
    @Field("loan_platform_90_days")
    private Integer loanPlatform90Days;

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public BigDecimal getLoanAmountMax()
    {
        return loanAmountMax;
    }

    public void setLoanAmountMax(BigDecimal loanAmountMax)
    {
        this.loanAmountMax = loanAmountMax;
    }

    public String getLoanEarliestDate()
    {
        return loanEarliestDate;
    }

    public void setLoanEarliestDate(String loanEarliestDate)
    {
        this.loanEarliestDate = loanEarliestDate;
    }

    public String getLoanLatestDate()
    {
        return loanLatestDate;
    }

    public void setLoanLatestDate(String loanLatestDate)
    {
        this.loanLatestDate = loanLatestDate;
    }

    public Integer getLoanPlatformTotal()
    {
        return loanPlatformTotal;
    }

    public void setLoanPlatformTotal(Integer loanPlatformTotal)
    {
        this.loanPlatformTotal = loanPlatformTotal;
    }

    public Integer getLoanPlatformToday()
    {
        return loanPlatformToday;
    }

    public void setLoanPlatformToday(Integer loanPlatformToday)
    {
        this.loanPlatformToday = loanPlatformToday;
    }

    public Integer getLoanPlatform3Days()
    {
        return loanPlatform3Days;
    }

    public void setLoanPlatform3Days(Integer loanPlatform3Days)
    {
        this.loanPlatform3Days = loanPlatform3Days;
    }

    public Integer getLoanPlatform7Days()
    {
        return loanPlatform7Days;
    }

    public void setLoanPlatform7Days(Integer loanPlatform7Days)
    {
        this.loanPlatform7Days = loanPlatform7Days;
    }

    public Integer getLoanPlatform15Days()
    {
        return loanPlatform15Days;
    }

    public void setLoanPlatform15Days(Integer loanPlatform15Days)
    {
        this.loanPlatform15Days = loanPlatform15Days;
    }

    public Integer getLoanPlatform30Days()
    {
        return loanPlatform30Days;
    }

    public void setLoanPlatform30Days(Integer loanPlatform30Days)
    {
        this.loanPlatform30Days = loanPlatform30Days;
    }

    public Integer getLoanPlatform60Days()
    {
        return loanPlatform60Days;
    }

    public void setLoanPlatform60Days(Integer loanPlatform60Days)
    {
        this.loanPlatform60Days = loanPlatform60Days;
    }

    public Integer getLoanPlatform90Days()
    {
        return loanPlatform90Days;
    }

    public void setLoanPlatform90Days(Integer loanPlatform90Days)
    {
        this.loanPlatform90Days = loanPlatform90Days;
    }
}
