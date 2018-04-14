package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.StatsSummary;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatsSummaryMapper
{
    List<StatsSummary> findStatsBy(@Param("day") Date day, @Param("hour") Integer hour);

    void add(StatsSummary stats);

    List<StatsSummary> getListGroupByHour(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("type") Integer type);

    List<StatsSummary> getListByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<StatsSummary> getListGroupByDay(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("type") Integer type);

    List<StatsSummary> getListGroupByWeek(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("type") Integer type);

    List<StatsSummary> getListGroupByMonth(@Param("startTime") Date startTime, @Param("endTime") Date endTime,
            @Param("type") Integer type);

    StatsSummary getFullSummary();

    StatsSummary getSummaryStatsByDate(Date date);

    StatsSummary getSummaryStatsFromDate(Date date);
}
