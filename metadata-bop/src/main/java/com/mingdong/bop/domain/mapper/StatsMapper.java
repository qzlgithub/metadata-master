package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.Stats;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatsMapper
{
    List<Stats> findStatsBy(@Param("day") Date day, @Param("hour") Integer hour);

    void add(Stats stats);

    List<Stats> getListGroupByHour(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("type") Integer type);

    List<Stats> getListByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<Stats> getListGroupByDay(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("type") Integer type);

    List<Stats> getListGroupByWeek(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("type") Integer type);

    List<Stats> getListGroupByMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("type") Integer type);
}
