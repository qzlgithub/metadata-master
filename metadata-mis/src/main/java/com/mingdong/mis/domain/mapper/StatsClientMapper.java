package com.mingdong.mis.domain.mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

public interface StatsClientMapper
{
    Integer getAllClientCount();

    Integer getClientCountByDate(@Param("start") Date start, @Param("end") Date end);

    BigDecimal getClientRechargeByDate(@Param("start") Date start, @Param("end") Date end);

    BigDecimal getClientRechargeAll();

    Integer countClientRechargeByDate(@Param("start") Date start, @Param("end") Date end);

}
