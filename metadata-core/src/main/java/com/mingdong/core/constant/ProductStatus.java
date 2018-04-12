package com.mingdong.core.constant;

import com.mingdong.core.util.DateCalculateUtils;

import java.math.BigDecimal;
import java.util.Date;

public enum ProductStatus
{
    NORMAL(0),
    NOT_START(1),
    NEARLY_EXPIRE(2),
    EXPIRED(3),
    INSUFFICIENT_BALANCE(4),
    IN_ARREAR(5);

    private final Integer id;

    ProductStatus(Integer id)
    {
        this.id = id;
    }

    public static Integer getStatusByDate(Date fromDate, Date toDate)
    {
        Date now = DateCalculateUtils.getCurrentDate(new Date());
        if(now.before(fromDate))
        {
            return NOT_START.getId();
        }
        else if(0 <= (toDate.getTime() - now.getTime()) && (toDate.getTime() - now.getTime()) < 7 * 24 * 3600 * 1000)
        {
            return NEARLY_EXPIRE.getId();
        }
        else if(now.after(toDate))
        {
            return EXPIRED.getId();
        }
        return NORMAL.getId();
    }

    public static Integer getStatusByBalance(BigDecimal unitAmt, BigDecimal balance)
    {
        if(balance.compareTo(unitAmt) < 0)
        {
            return IN_ARREAR.getId();
        }
        BigDecimal threshold = unitAmt.multiply(new BigDecimal(5000));
        if(balance.compareTo(threshold) < 0)
        {
            return INSUFFICIENT_BALANCE.getId();
        }
        return NORMAL.getId();
    }

    public Integer getId()
    {
        return id;
    }
}
