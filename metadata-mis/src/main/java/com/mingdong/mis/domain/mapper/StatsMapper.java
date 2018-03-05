package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Stats;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatsMapper
{
    List<Stats> findStatsBy(@Param("day") Date day,@Param("hour") Integer hour);

    void add(Stats stats);
}
