package com.mingdong.bop.domain.mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

public interface StatsClientMapper
{
    Long getAllClientCount();

    Long getClientCountByDate(@Param("start") Date start, @Param("end") Date end);

    BigDecimal getClientRechargeByDate(@Param("start") Date start, @Param("end") Date end);

    BigDecimal getClientRechargeAll();

    Long countClientRechargeByDate(@Param("start") Date start, @Param("end") Date end);

}
