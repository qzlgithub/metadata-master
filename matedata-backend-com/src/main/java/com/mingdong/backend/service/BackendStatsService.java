package com.mingdong.backend.service;

import com.mingdong.backend.model.SummaryStatsDTO;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.dto.request.JobLogReqDTO;
import com.mingdong.core.model.dto.request.StatsClientRequestReqDTO;
import com.mingdong.core.model.dto.request.StatsDTO;
import com.mingdong.core.model.dto.request.StatsProductRequestReqDTO;
import com.mingdong.core.model.dto.request.StatsRechargeDTO;
import com.mingdong.core.model.dto.response.RechargeStatsDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;

import java.util.List;
import java.util.Map;

public interface BackendStatsService
{
    SummaryStatsDTO getSummaryStatisticsInfo();

    Map<String, Integer> getClientIncreaseTrend(DateRange dateRange, RangeUnit rangeUnit);

    Map<String, List<RechargeStatsDTO>> getClientRechargeTrend(DateRange dateRange, RangeUnit rangeUnit);

    ResponseDTO addStats(StatsDTO stats);

    ResponseDTO addStatsRechargeList(List<StatsRechargeDTO> statsRecharges);

    ResponseDTO addJobLog(JobLogReqDTO jobLogReqDTO);

    ResponseDTO addStatsRequest(List<StatsClientRequestReqDTO> addStatsClientRequest,
            List<StatsProductRequestReqDTO> addStatsProductRequest);
}
