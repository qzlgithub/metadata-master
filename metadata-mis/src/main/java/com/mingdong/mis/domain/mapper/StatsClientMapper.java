package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.StatsDateInfo;
import com.mingdong.mis.domain.entity.StatsRechargeInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface StatsClientMapper
{
    int getAllClientCount(@Param("managerId") Long managerId);

    int getClientCountByDate(@Param("fromDate") Date start, @Param("toDate") Date end,
            @Param("managerId") Long managerId);

    BigDecimal getClientRechargeAll();

    int countClientRechargeByDate(@Param("fromDate") Date start, @Param("toDate") Date end);

    List<StatsDateInfo> getRequestListStats(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
            @Param("keyword") String keyword, @Param("productId") Long productId);

    List<StatsDateInfo> getRevenueListStats(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    List<StatsRechargeInfo> statsRechargeByData(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    BigDecimal getClientRechargeByDate(@Param("fromDate") Date monthFirst, @Param("toDate") Date currentDay,
            @Param("managerId") Long managerId);
}
