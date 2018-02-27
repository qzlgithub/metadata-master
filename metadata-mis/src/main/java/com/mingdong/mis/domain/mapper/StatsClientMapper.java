package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.StatsDateInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface StatsClientMapper
{
    Integer getAllClientCount();

    Integer getClientCountByDate(@Param("start") Date start, @Param("end") Date end);

    BigDecimal getClientRechargeByDate(@Param("start") Date start, @Param("end") Date end);

    BigDecimal getClientRechargeAll();

    Integer countClientRechargeByDate(@Param("start") Date start, @Param("end") Date end);

    List<StatsDateInfo> getRequestListStats(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
            @Param("keyword") String keyword, @Param("productId") Long productId);
}
