package com.mingdong.bop.domain.mapper;

import java.math.BigDecimal;
import java.util.Date;

public interface StatsClientMapper
{
    Long getAllClientCount();

    Long getClientCountByDate(Date date);

    BigDecimal getClientRechargeByDate(Date date);

}
