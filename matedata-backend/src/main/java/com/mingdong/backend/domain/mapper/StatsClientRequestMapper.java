package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.StatsClientRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatsClientRequestMapper
{
    void addAll(@Param("list") List<StatsClientRequest> list);

    List<StatsClientRequest> getClientTrafficByClientIds(@Param("clientIds") List<Long> clientIds,
            @Param("beforeDate") Date beforeDate, @Param("afterDate") Date afterDate);
}
