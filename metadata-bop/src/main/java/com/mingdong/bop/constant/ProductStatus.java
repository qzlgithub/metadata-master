package com.mingdong.bop.constant;

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
        Date now = new Date();
        if(now.before(fromDate))
        {
            return NOT_START.getId();
        }
        else if(now.after(toDate))
        {
            return EXPIRED.getId();
        }
        else if(toDate.getTime() - now.getTime() < 7 * 24 * 3600 * 1000)
        {
            return NEARLY_EXPIRE.getId();
        }
        return NORMAL.getId();
    }

    public static Integer getStatusByBalance(BigDecimal costAmt, BigDecimal balance)
    {
        if(balance.compareTo(costAmt) < 0)
        {
            return IN_ARREAR.getId();
        }
        BigDecimal threshold = costAmt.multiply(new BigDecimal(5000));
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
