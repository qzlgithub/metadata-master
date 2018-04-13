package com.mingdong.backend.service;

import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.dto.response.RechargeStatsDTO;

import java.util.List;
import java.util.Map;

public interface DataStatsService
{
    Map<String, Integer> getClientIncreaseTrend(DateRange dateRange, RangeUnit rangeUnit);

    Map<String, List<RechargeStatsDTO>> getClientRechargeTrend(DateRange dateRange, RangeUnit rangeUnit);

}
