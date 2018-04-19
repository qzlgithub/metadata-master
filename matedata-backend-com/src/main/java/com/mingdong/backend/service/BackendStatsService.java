package com.mingdong.backend.service;

import com.mingdong.backend.model.SummaryStatsDTO;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.JobLogReqDTO;
import com.mingdong.core.model.dto.request.StatsClientRequestReqDTO;
import com.mingdong.core.model.dto.request.StatsDTO;
import com.mingdong.core.model.dto.request.StatsProductRequestReqDTO;
import com.mingdong.core.model.dto.request.StatsRechargeDTO;
import com.mingdong.core.model.dto.response.RechargeStatsDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.StatsClientRequestResDTO;
import com.mingdong.core.model.dto.response.StatsProductRequestResDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BackendStatsService
{
    /**
     * 获取可监控的客户字典数据
     */
    ListDTO<Dict> getMonitoredClient();

    SummaryStatsDTO getSummaryStatisticsInfo();

    Map<String, Integer> getClientIncreaseTrend(DateRange dateRange, RangeUnit rangeUnit);

    Map<String, List<RechargeStatsDTO>> getClientRechargeTrend(DateRange dateRange, RangeUnit rangeUnit);

    ResponseDTO addStats(StatsDTO stats);

    ResponseDTO addStatsRechargeList(List<StatsRechargeDTO> statsRecharges);

    ResponseDTO addJobLog(JobLogReqDTO jobLogReqDTO);

    ResponseDTO addStatsRequest(List<StatsClientRequestReqDTO> addStatsClientRequest,
            List<StatsProductRequestReqDTO> addStatsProductRequest);

    ListDTO<StatsProductRequestResDTO> getProductTrafficByProductIds(List<Long> productIds, Date beforeDate,
            Date afterDate);

    ListDTO<StatsClientRequestResDTO> getClientTrafficByClientIds(List<Long> clientIds, Date beforeDate,
            Date afterDate);

    List<RechargeStatsDTO> getClientRechargeTypeTotal(DateRange range);
}
