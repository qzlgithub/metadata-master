package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.StatsClientRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatsClientRequestMapper
{
    void addAll(@Param("list") List<StatsClientRequest> list);
}
