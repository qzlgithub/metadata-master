package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.StatsRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatsRechargeMapper
{
    List<StatsRecharge> findStatsRechargeBy(@Param("day") Date day, @Param("hour") Integer hour);

    void add(StatsRecharge stats);

    void addAll(@Param("list") List<StatsRecharge> statsRecharges);
}
