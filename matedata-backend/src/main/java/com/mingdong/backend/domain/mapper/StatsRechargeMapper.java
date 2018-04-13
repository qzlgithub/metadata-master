package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.StatsRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatsRechargeMapper
{
    List<StatsRecharge> findStatsRechargeBy(@Param("day") Date day, @Param("hour") Integer hour);

    void add(StatsRecharge stats);

    void addAll(@Param("list") List<StatsRecharge> statsRecharges);

    List<StatsRecharge> getListGroupByHour(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<StatsRecharge> getListGroupByDay(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<StatsRecharge> getListGroupByWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<StatsRecharge> getListGroupByMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
