package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.StatsRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatsRequestMapper
{
    void addAll(@Param("list") List<StatsRequest> list);

    List<StatsRequest> getProductTrafficByProductIds(@Param("productIds") List<Long> productIds,
            @Param("beforeDate") Date beforeDate, @Param("afterDate") Date afterDate);

    List<StatsRequest> getClientTrafficByClientIds(@Param("clientIds") List<Long> clientIds,
            @Param("beforeDate") Date beforeDate, @Param("afterDate") Date afterDate);

    List<StatsRequest> getRequestGroupByHour(@Param("beforeDate") Date beforeDate, @Param("afterDate") Date afterDate,
            @Param("clientId") Long clientId);

    List<StatsRequest> getRequestGroupByHourAndProductId(@Param("beforeDate") Date beforeDate,
            @Param("afterDate") Date afterDate, @Param("productIds") List<Long> productIds,
            @Param("clientId") Long clientId);

    List<StatsRequest> getRequestGroupByDay(@Param("beforeDate") Date beforeDate, @Param("afterDate") Date afterDate,
            @Param("clientId") Long clientId);

    List<StatsRequest> getRequestGroupByDayAndProductId(@Param("beforeDate") Date beforeDate,
            @Param("afterDate") Date afterDate, @Param("productIds") List<Long> productIds,
            @Param("clientId") Long clientId);

    List<StatsRequest> getRequestGroupByWeek(@Param("beforeDate") Date beforeDate, @Param("afterDate") Date afterDate,
            @Param("clientId") Long clientId);

    List<StatsRequest> getRequestGroupByWeekAndProductId(@Param("beforeDate") Date beforeDate,
            @Param("afterDate") Date afterDate, @Param("productIds") List<Long> productIds,
            @Param("clientId") Long clientId);

    List<StatsRequest> getRequestGroupByMonth(@Param("beforeDate") Date beforeDate, @Param("afterDate") Date afterDate,
            @Param("clientId") Long clientId);

    List<StatsRequest> getRequestGroupByMonthAndProductId(@Param("beforeDate") Date beforeDate,
            @Param("afterDate") Date afterDate, @Param("productIds") List<Long> productIds,
            @Param("clientId") Long clientId);
}
