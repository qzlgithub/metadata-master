package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.StatsProductRequest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StatsProductRequestMapper
{
    void addAll(@Param("list") List<StatsProductRequest> list);

    List<StatsProductRequest> getProductTrafficByProductIds(@Param("productIds") List<Long> productIds,
            @Param("beforeDate") Date beforeDate, @Param("afterDate") Date afterDate);
}
