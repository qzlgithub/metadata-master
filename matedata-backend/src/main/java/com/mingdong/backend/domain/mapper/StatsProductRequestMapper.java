package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.StatsProductRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatsProductRequestMapper
{
    void addAll(@Param("list") List<StatsProductRequest> list);
}
