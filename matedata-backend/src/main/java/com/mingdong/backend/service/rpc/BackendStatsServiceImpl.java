package com.mingdong.backend.service.rpc;

import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.domain.entity.Job;
import com.mingdong.backend.domain.entity.JobLog;
import com.mingdong.backend.domain.entity.StatsRecharge;
import com.mingdong.backend.domain.entity.StatsRequest;
import com.mingdong.backend.domain.entity.StatsSummary;
import com.mingdong.backend.domain.mapper.JobLogMapper;
import com.mingdong.backend.domain.mapper.JobMapper;
import com.mingdong.backend.domain.mapper.StatsRechargeMapper;
import com.mingdong.backend.domain.mapper.StatsRequestMapper;
import com.mingdong.backend.domain.mapper.StatsSummaryMapper;
import com.mingdong.backend.model.SummaryStatsDTO;
import com.mingdong.backend.service.BackendStatsService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.JobLogReqDTO;
import com.mingdong.core.model.dto.request.StatsDTO;
import com.mingdong.core.model.dto.request.StatsRechargeDTO;
import com.mingdong.core.model.dto.request.StatsRequestReqDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.DictRechargeTypeResDTO;
import com.mingdong.core.model.dto.response.ProductResDTO;
import com.mingdong.core.model.dto.response.RechargeStatsDTO;
import com.mingdong.core.model.dto.response.RequestStatsResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.StatsRequestResDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.ProductRpcService;
import com.mingdong.core.service.SystemRpcService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackendStatsServiceImpl implements BackendStatsService
{
    private static final Integer INC_STAT = 1;
    // private static final Integer REQ_STAT = 2;
    // private static final Integer RCG_STAT = 3;

    @Resource
    private RedisDao redisDao;
    @Resource
    private StatsSummaryMapper statsSummaryMapper;
    @Resource
    private StatsRechargeMapper statsRechargeMapper;
    @Resource
    private JobLogMapper jobLogMapper;
    @Resource
    private JobMapper jobMapper;
    @Resource
    private StatsRequestMapper statsRequestMapper;
    @Resource
    private SystemRpcService systemRpcService;
    @Resource
    private ClientRpcService clientRpcService;
    @Resource
    private ProductRpcService productRpcService;

    @Override
    public ListDTO<Dict> getMonitoredClient()
    {
        ListDTO<Dict> listDTO = new ListDTO<>();
        Map<String, String> clientMap = redisDao.getAllClient();
        if(clientMap != null)
        {
            List<Dict> list = new ArrayList<>();
            Dict dict;
            for(Map.Entry<String, String> entry : clientMap.entrySet())
            {
                dict = new Dict(entry.getKey(), entry.getValue());
                list.add(dict);
            }
            listDTO.setList(list);
            listDTO.setTotal(list.size());
        }
        return listDTO;
    }

    @Override
    public SummaryStatsDTO getSummaryStatisticsInfo()
    {
        SummaryStatsDTO res = new SummaryStatsDTO();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime(); // 今日
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime(); // 昨日
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        Date latest7DaysFrom = calendar.getTime(); // 近7日
        calendar.add(Calendar.DAY_OF_MONTH, -23);
        Date latest30DaysFrom = calendar.getTime(); // 近30日
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date thisMonthFrom = calendar.getTime(); // 当月1日
        // 全量统计
        StatsSummary stats = statsSummaryMapper.getFullSummary();
        if(stats != null)
        {
            res.setClientTotal(stats.getClientIncrement());
            res.setRequestTotal(stats.getRequest());
            res.setRequestFailedTotal(stats.getRequestFailed());
            res.setRequest3rdFailedTotal(stats.getRequest3rdFailed());
            res.setRechargeAmountTotal(stats.getRecharge());
            res.setRequestNotHitTotal(stats.getRequestNotHit());
        }
        // 今日统计数据
        stats = statsSummaryMapper.getSummaryStatsByDate(today);
        if(stats != null)
        {
            res.setRequestToday(stats.getRequest());
            res.setRequestFailedToday(stats.getRequestFailed());
            res.setRequest3rdFailedToday(stats.getRequest3rdFailed());
            res.setProfitAmountToday(stats.getProfit());
            res.setRechargeAmountToday(stats.getRecharge());
            res.setRequestNotHitToday(stats.getRequestNotHit());
        }
        // 昨日统计数据
        stats = statsSummaryMapper.getSummaryStatsByDate(yesterday);
        if(stats != null)
        {
            res.setRequestYesterday(stats.getRequest());
            res.setRequestFailedYesterday(stats.getRequestFailed());
            res.setRequest3rdFailedYesterday(stats.getRequest3rdFailed());
            res.setProfitAmountYesterday(stats.getProfit());
            res.setRequestNotHitYesterday(stats.getRequestNotHit());
        }
        // 近7天统计数据
        stats = statsSummaryMapper.getSummaryStatsFromDate(latest7DaysFrom);
        if(stats != null)
        {
            res.setRechargeAmountIn7Days(stats.getRecharge());
            res.setClientIncIn7Days(stats.getClientIncrement());
        }
        // 近30天统计数据
        stats = statsSummaryMapper.getSummaryStatsFromDate(latest30DaysFrom);
        if(stats != null)
        {
            res.setClientIncIn30Days(stats.getClientIncrement());
            res.setRechargeAmountIn30Days(stats.getRecharge());
        }
        // 本月统计数据
        stats = statsSummaryMapper.getSummaryStatsFromDate(thisMonthFrom);
        if(stats != null)
        {
            res.setRequestThisMonth(stats.getRequest());
            res.setRequestFailedThisMonth(stats.getRequestFailed());
            res.setRequest3rdFailedThisMonth(stats.getRequest3rdFailed());
            res.setRechargeAmountThisMonth(stats.getRecharge());
            res.setClientIncThisMonth(stats.getClientIncrement());
            res.setRequestNotHitThisMonth(stats.getRequestNotHit());
        }
        return res;
    }

    @Override
    public Map<String, Integer> getClientIncreaseTrend(DateRange dateRange, RangeUnit rangeUnit)
    {
        Map<String, Integer> map = new HashMap<>();
        if(rangeUnit == RangeUnit.HOUR)
        {
            List<StatsSummary> statsList = statsSummaryMapper.getListGroupByHour(dateRange.getStart(),
                    dateRange.getEnd(), INC_STAT);
            for(StatsSummary o : statsList)
            {
                if(o.getClientIncrement() != 0)
                {
                    map.put(o.getStatsHour() + "", o.getClientIncrement());
                }
            }
        }
        else if(rangeUnit == RangeUnit.DAY || rangeUnit == RangeUnit.WEEK || rangeUnit == RangeUnit.MONTH)
        {
            if(rangeUnit == RangeUnit.DAY)
            {
                List<StatsSummary> statsList1 = statsSummaryMapper.getListGroupByDay(dateRange.getStart(),
                        dateRange.getEnd(), INC_STAT);
                for(StatsSummary o : statsList1)
                {
                    map.put(DateUtils.format(o.getStatsDate(), DateFormat.YYYY_MM_DD_2), o.getClientIncrement());
                }
            }
            else if(rangeUnit == RangeUnit.WEEK)
            {
                List<StatsSummary> statsList2 = statsSummaryMapper.getListGroupByWeek(dateRange.getStart(),
                        dateRange.getEnd(), INC_STAT);
                for(StatsSummary o : statsList2)
                {
                    map.put(o.getStatsYear() + String.format("%02d", o.getStatsWeek()), o.getClientIncrement());
                }
            }
            else
            {
                List<StatsSummary> statsList3 = statsSummaryMapper.getListGroupByMonth(dateRange.getStart(),
                        dateRange.getEnd(), INC_STAT);
                for(StatsSummary o : statsList3)
                {
                    map.put(o.getStatsYear() + String.format("/%02d", o.getStatsMonth()), o.getClientIncrement());
                }
            }

        }
        return map;
    }

    @Override
    public Map<String, List<RechargeStatsDTO>> getClientRechargeTrend(DateRange dateRange, RangeUnit rangeUnit)
    {
        Map<String, List<RechargeStatsDTO>> map = new HashMap<>();
        List<StatsRecharge> dataList;
        if(rangeUnit == RangeUnit.HOUR)
        {
            dataList = statsRechargeMapper.getListGroupByHour(dateRange.getStart(), dateRange.getEnd());
        }
        else if(rangeUnit == RangeUnit.WEEK)
        {
            dataList = statsRechargeMapper.getListGroupByWeek(dateRange.getStart(), dateRange.getEnd());
        }
        else if(rangeUnit == RangeUnit.MONTH)
        {
            dataList = statsRechargeMapper.getListGroupByMonth(dateRange.getStart(), dateRange.getEnd());
        }
        else
        {
            dataList = statsRechargeMapper.getListGroupByDay(dateRange.getStart(), dateRange.getEnd());
        }
        ListDTO<DictRechargeTypeResDTO> rechargeTypeList = systemRpcService.getRechargeTypeList(null, null);
        List<DictRechargeTypeResDTO> list1 = rechargeTypeList.getList();
        Map<Integer, String> rechargeTypeNameMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(list1))
        {
            for(DictRechargeTypeResDTO item : list1)
            {
                rechargeTypeNameMap.put(item.getId(), item.getName());
            }
        }
        for(StatsRecharge o : dataList)
        {
            String name;
            if(rangeUnit == RangeUnit.HOUR)
            {
                name = o.getStatsHour() + "";
            }
            else if(rangeUnit == RangeUnit.WEEK)
            {
                name = o.getStatsYear() + String.format("%02d", o.getStatsWeek());
            }
            else if(rangeUnit == RangeUnit.MONTH)
            {
                name = o.getStatsYear() + String.format("/%02d", o.getStatsMonth());
            }
            else
            {
                name = DateUtils.format(o.getStatsDate(), DateFormat.YYYY_MM_DD_2);
            }
            List<RechargeStatsDTO> list = map.get(name);
            if(CollectionUtils.isEmpty(list))
            {
                list = new ArrayList<>();
            }
            RechargeStatsDTO dto = new RechargeStatsDTO();
            dto.setRechargeTypeName(rechargeTypeNameMap.get(o.getRechargeType()));
            dto.setAmount(o.getAmount());
            list.add(dto);
            map.put(name, list);
        }
        return map;
    }

    @Override
    @Transactional
    public ResponseDTO addStats(StatsDTO statsDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        StatsSummary stats = new StatsSummary();
        Date date = new Date();
        stats.setCreateTime(date);
        stats.setUpdateTime(date);
        stats.setStatsYear(statsDTO.getStatsYear());
        stats.setStatsMonth(statsDTO.getStatsMonth());
        stats.setStatsWeek(statsDTO.getStatsWeek());
        stats.setStatsDay(statsDTO.getStatsDay());
        stats.setStatsHour(statsDTO.getStatsHour());
        stats.setStatsDate(statsDTO.getStatsDate());
        stats.setClientIncrement(statsDTO.getClientIncrement());
        stats.setRecharge(statsDTO.getClientRecharge());
        stats.setRequest(statsDTO.getRequest());
        stats.setRequestNotHit(statsDTO.getRequestNotHit());
        stats.setRequestFailed(statsDTO.getRequestFailed());
        statsSummaryMapper.add(stats);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO addStatsRechargeList(List<StatsRechargeDTO> statsRecharges)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        List<StatsRecharge> statsRechargeList = new ArrayList<>();
        StatsRecharge statsRecharge;
        Date date = new Date();
        for(StatsRechargeDTO item : statsRecharges)
        {
            statsRecharge = new StatsRecharge();
            statsRecharge.setCreateTime(date);
            statsRecharge.setUpdateTime(date);
            statsRecharge.setRechargeType(item.getRechargeType());
            statsRecharge.setAmount(item.getAmount());
            statsRecharge.setStatsYear(item.getStatsYear());
            statsRecharge.setStatsMonth(item.getStatsMonth());
            statsRecharge.setStatsWeek(item.getStatsWeek());
            statsRecharge.setStatsDay(item.getStatsDay());
            statsRecharge.setStatsHour(item.getStatsHour());
            statsRecharge.setStatsDate(item.getStatsDate());
            statsRechargeList.add(statsRecharge);
        }
        statsRechargeMapper.addAll(statsRechargeList);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO addJobLog(JobLogReqDTO jobLogReqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        JobLog addJobLog = new JobLog();
        Date date = new Date();
        addJobLog.setCreateTime(date);
        addJobLog.setJobCode(jobLogReqDTO.getJobCode());
        addJobLog.setSuccess(jobLogReqDTO.getSuccess());
        addJobLog.setRemark(jobLogReqDTO.getRemark());
        jobLogMapper.add(addJobLog);
        if(TrueOrFalse.TRUE.equals(jobLogReqDTO.getSuccess()))
        {
            Job job = new Job();
            job.setCode(jobLogReqDTO.getJobCode());
            job.setLastSucTime(date);
            jobMapper.updateSkipNull(job);
        }
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO addStatsRequest(List<StatsRequestReqDTO> addStatsRequest)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        List<StatsRequest> statsRequests = new ArrayList<>();
        Date date = new Date();
        if(!CollectionUtils.isEmpty(addStatsRequest))
        {
            StatsRequest statsRequest;
            for(StatsRequestReqDTO item : addStatsRequest)
            {
                statsRequest = new StatsRequest();
                statsRequest.setCreateTime(date);
                statsRequest.setUpdateTime(date);
                statsRequest.setStatsDate(item.getStatsDate());
                statsRequest.setStatsDay(item.getStatsDay());
                statsRequest.setStatsHour(item.getStatsHour());
                statsRequest.setStatsMonth(item.getStatsMonth());
                statsRequest.setStatsWeek(item.getStatsWeek());
                statsRequest.setStatsYear(item.getStatsYear());
                statsRequest.setProductId(item.getProductId());
                statsRequest.setClientId(item.getClientId());
                statsRequest.setRequest(item.getRequest());
                statsRequest.setRequestNotHit(item.getRequestNotHit());
                statsRequest.setRequestFailed(item.getRequestFailed());
                statsRequests.add(statsRequest);
            }
            statsRequestMapper.addAll(statsRequests);
        }
        return responseDTO;
    }

    @Override
    public ListDTO<StatsRequestResDTO> getProductTrafficByProductIds(List<Long> productIds, Date beforeDate,
            Date afterDate)
    {
        ListDTO<StatsRequestResDTO> listDTO = new ListDTO<>();
        List<StatsRequest> list = statsRequestMapper.getProductTrafficByProductIds(productIds, beforeDate, afterDate);
        List<StatsRequestResDTO> dataList = new ArrayList<>();
        listDTO.setList(dataList);
        if(!CollectionUtils.isEmpty(list))
        {
            StatsRequestResDTO statsRequestResDTO;
            for(StatsRequest item : list)
            {
                statsRequestResDTO = new StatsRequestResDTO();
                statsRequestResDTO.setProductId(item.getProductId());
                statsRequestResDTO.setRequest(item.getRequest());
                statsRequestResDTO.setStatsDate(item.getStatsDate());
                statsRequestResDTO.setStatsDay(item.getStatsDay());
                statsRequestResDTO.setStatsHour(item.getStatsHour());
                statsRequestResDTO.setStatsMonth(item.getStatsMonth());
                statsRequestResDTO.setStatsWeek(item.getStatsWeek());
                statsRequestResDTO.setStatsYear(item.getStatsYear());
                dataList.add(statsRequestResDTO);
            }
        }
        return listDTO;
    }

    @Override
    public ListDTO<StatsRequestResDTO> getClientTrafficByClientIds(List<Long> clientIds, Date beforeDate,
            Date afterDate)
    {
        ListDTO<StatsRequestResDTO> listDTO = new ListDTO<>();
        List<StatsRequest> list = statsRequestMapper.getClientTrafficByClientIds(clientIds, beforeDate, afterDate);
        List<StatsRequestResDTO> dataList = new ArrayList<>();
        listDTO.setList(dataList);
        if(!CollectionUtils.isEmpty(list))
        {
            StatsRequestResDTO statsRequestResDTO;
            for(StatsRequest item : list)
            {
                statsRequestResDTO = new StatsRequestResDTO();
                statsRequestResDTO.setClientId(item.getClientId());
                statsRequestResDTO.setRequest(item.getRequest());
                statsRequestResDTO.setStatsDate(item.getStatsDate());
                statsRequestResDTO.setStatsDay(item.getStatsDay());
                statsRequestResDTO.setStatsHour(item.getStatsHour());
                statsRequestResDTO.setStatsMonth(item.getStatsMonth());
                statsRequestResDTO.setStatsWeek(item.getStatsWeek());
                statsRequestResDTO.setStatsYear(item.getStatsYear());
                dataList.add(statsRequestResDTO);
            }
        }
        return listDTO;
    }

    @Override
    public List<RechargeStatsDTO> getClientRechargeTypeTotal(DateRange dateRange)
    {
        List<RechargeStatsDTO> list = new ArrayList<>();
        List<StatsRecharge> dataList = statsRechargeMapper.getListGroupByType(dateRange.getStart(), dateRange.getEnd());
        ListDTO<DictRechargeTypeResDTO> rechargeTypeList = systemRpcService.getRechargeTypeList(null, null);
        List<DictRechargeTypeResDTO> list1 = rechargeTypeList.getList();
        Map<Integer, String> rechargeTypeNameMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(list1))
        {
            for(DictRechargeTypeResDTO item : list1)
            {
                rechargeTypeNameMap.put(item.getId(), item.getName());
            }
        }
        RechargeStatsDTO rechargeStatsDTO;
        for(StatsRecharge o : dataList)
        {
            rechargeStatsDTO = new RechargeStatsDTO();
            rechargeStatsDTO.setRechargeTypeName(rechargeTypeNameMap.get(o.getRechargeType()));
            rechargeStatsDTO.setAmount(o.getAmount());
            list.add(rechargeStatsDTO);
        }
        return list;
    }

    @Override
    public List<RequestStatsResDTO> getRequestStats(DateRange range, RangeUnit unit, List<Long> productIds,
            String clientName)
    {
        List<RequestStatsResDTO> requestStatsResDTOS = new ArrayList<>();
        List<Long> clientIds = new ArrayList<>();
        if(!StringUtils.isNullBlank(clientName))
        {
            List<ClientInfoResDTO> clientInfoResDTOS = clientRpcService.getClientByCorpName(clientName);
            for(ClientInfoResDTO item : clientInfoResDTOS)
            {
                clientIds.add(item.getClientId());
            }
        }
        List<StatsRequest> dataList;
        if(unit == RangeUnit.HOUR)
        {
            if(!CollectionUtils.isEmpty(productIds))
            {
                dataList = statsRequestMapper.getRequestGroupByHourAndProductId(range.getStart(), range.getEnd(),
                        productIds, clientIds.size() > 0 ? clientIds.get(0) : null);
            }
            else
            {
                dataList = statsRequestMapper.getRequestGroupByHour(range.getStart(), range.getEnd(),
                        clientIds.size() > 0 ? clientIds.get(0) : null);
            }
        }
        else if(unit == RangeUnit.WEEK)
        {
            if(!CollectionUtils.isEmpty(productIds))
            {
                dataList = statsRequestMapper.getRequestGroupByWeekAndProductId(range.getStart(), range.getEnd(),
                        productIds, clientIds.size() > 0 ? clientIds.get(0) : null);
            }
            else
            {
                dataList = statsRequestMapper.getRequestGroupByWeek(range.getStart(), range.getEnd(),
                        clientIds.size() > 0 ? clientIds.get(0) : null);
            }
        }
        else if(unit == RangeUnit.MONTH)
        {
            if(!CollectionUtils.isEmpty(productIds))
            {
                dataList = statsRequestMapper.getRequestGroupByMonthAndProductId(range.getStart(), range.getEnd(),
                        productIds, clientIds.size() > 0 ? clientIds.get(0) : null);
            }
            else
            {
                dataList = statsRequestMapper.getRequestGroupByMonth(range.getStart(), range.getEnd(),
                        clientIds.size() > 0 ? clientIds.get(0) : null);
            }
        }
        else
        {
            if(!CollectionUtils.isEmpty(productIds))
            {
                dataList = statsRequestMapper.getRequestGroupByDayAndProductId(range.getStart(), range.getEnd(),
                        productIds, clientIds.size() > 0 ? clientIds.get(0) : null);
            }
            else
            {
                dataList = statsRequestMapper.getRequestGroupByDay(range.getStart(), range.getEnd(),
                        clientIds.size() > 0 ? clientIds.get(0) : null);
            }
        }
        if(!CollectionUtils.isEmpty(productIds))
        {
            List<ProductResDTO> productResDTOS = productRpcService.getProductList(productIds);
            Map<Long, ProductResDTO> productResDTOMap = new HashMap<>();
            for(ProductResDTO item : productResDTOS)
            {
                productResDTOMap.put(item.getId(), item);
            }
            Map<Long, RequestStatsResDTO> requestStatsResDTOMap = new HashMap<>();
            handleStatsRequest(requestStatsResDTOS, unit, dataList, requestStatsResDTOMap, productResDTOMap);
        }
        else
        {
            RequestStatsResDTO requestStatsResDTO = new RequestStatsResDTO();
            requestStatsResDTOS.add(requestStatsResDTO);
            Map<String, Long> requestMap = new HashMap<>();
            Map<String, Long> requestNotHitMap = new HashMap<>();
            Map<String, Long> requestFailedMap = new HashMap<>();
            requestStatsResDTO.setName("请求总量");
            requestStatsResDTO.setRequestMap(requestMap);
            for(StatsRequest o : dataList)
            {
                String name;
                if(unit == RangeUnit.HOUR)
                {
                    name = o.getStatsHour() + "";
                }
                else if(unit == RangeUnit.WEEK)
                {
                    name = o.getStatsYear() + String.format("%02d", o.getStatsWeek());
                }
                else if(unit == RangeUnit.MONTH)
                {
                    name = o.getStatsYear() + String.format("/%02d", o.getStatsMonth());
                }
                else
                {
                    name = DateUtils.format(o.getStatsDate(), DateFormat.YYYY_MM_DD_2);
                }
                Long longNumber = requestMap.computeIfAbsent(name, k -> 0l);
                requestMap.put(name, longNumber + o.getRequest());
                longNumber = requestNotHitMap.computeIfAbsent(name, k -> 0l);
                requestNotHitMap.put(name, longNumber + o.getRequestNotHit());
                longNumber = requestFailedMap.computeIfAbsent(name, k -> 0l);
                requestFailedMap.put(name, longNumber + o.getRequestFailed());
            }
        }
        return requestStatsResDTOS;
    }

    private void handleStatsRequest(List<RequestStatsResDTO> requestStatsResDTOS, RangeUnit unit,
            List<StatsRequest> dataList, Map<Long, RequestStatsResDTO> requestStatsResDTOMap,
            Map<Long, ProductResDTO> productResDTOMap)
    {
        RequestStatsResDTO requestStatsResDTO;
        Map<String, Long> requestMapTemp;
        Map<String, Long> requestFailedMapTemp;
        Map<String, Long> requestNotHitMapTemp;
        for(StatsRequest o : dataList)
        {
            requestStatsResDTO = requestStatsResDTOMap.get(o.getProductId());
            if(requestStatsResDTO == null)
            {
                requestStatsResDTO = new RequestStatsResDTO();
                requestStatsResDTO.setName(productResDTOMap.get(o.getProductId()).getName());
                requestStatsResDTOS.add(requestStatsResDTO);
                requestStatsResDTOMap.put(o.getProductId(), requestStatsResDTO);
            }
            requestMapTemp = requestStatsResDTO.getRequestMap();
            if(requestMapTemp == null)
            {
                requestMapTemp = new HashMap<>();
                requestStatsResDTO.setRequestMap(requestMapTemp);
            }
            requestFailedMapTemp = requestStatsResDTO.getRequestFailedMap();
            if(requestFailedMapTemp == null)
            {
                requestFailedMapTemp = new HashMap<>();
                requestStatsResDTO.setRequestFailedMap(requestFailedMapTemp);
            }
            requestNotHitMapTemp = requestStatsResDTO.getRequestNotHitMap();
            if(requestNotHitMapTemp == null)
            {
                requestNotHitMapTemp = new HashMap<>();
                requestStatsResDTO.setRequestNotHitMap(requestNotHitMapTemp);
            }
            String name;
            if(unit == RangeUnit.HOUR)
            {
                name = o.getStatsHour() + "";
            }
            else if(unit == RangeUnit.WEEK)
            {
                name = o.getStatsYear() + String.format("%02d", o.getStatsWeek());
            }
            else if(unit == RangeUnit.MONTH)
            {
                name = o.getStatsYear() + String.format("/%02d", o.getStatsMonth());
            }
            else
            {
                name = DateUtils.format(o.getStatsDate(), DateFormat.YYYY_MM_DD_2);
            }
            Long longNumber = requestMapTemp.computeIfAbsent(name, k -> 0l);
            requestMapTemp.put(name, longNumber + o.getRequest());
            longNumber = requestNotHitMapTemp.computeIfAbsent(name, k -> 0l);
            requestNotHitMapTemp.put(name, longNumber + o.getRequestNotHit());
            longNumber = requestFailedMapTemp.computeIfAbsent(name, k -> 0l);
            requestFailedMapTemp.put(name, longNumber + o.getRequestFailed());
        }
    }

    @Override
    public List<RequestStatsResDTO> getRequestStatsGroupByProduct(DateRange range, RangeUnit unit, String clientName)
    {
        List<RequestStatsResDTO> requestStatsResDTOS = new ArrayList<>();
        List<Long> clientIds = new ArrayList<>();
        if(!StringUtils.isNullBlank(clientName))
        {
            List<ClientInfoResDTO> clientInfoResDTOS = clientRpcService.getClientByCorpName(clientName);
            for(ClientInfoResDTO item : clientInfoResDTOS)
            {
                clientIds.add(item.getClientId());
            }
        }
        List<StatsRequest> dataList;
        if(unit == RangeUnit.HOUR)
        {
            dataList = statsRequestMapper.getRequestGroupByHourAndProductId(range.getStart(), range.getEnd(), null,
                    clientIds.size() > 0 ? clientIds.get(0) : null);
        }
        else if(unit == RangeUnit.WEEK)
        {
            dataList = statsRequestMapper.getRequestGroupByWeekAndProductId(range.getStart(), range.getEnd(), null,
                    clientIds.size() > 0 ? clientIds.get(0) : null);
        }
        else if(unit == RangeUnit.MONTH)
        {
            dataList = statsRequestMapper.getRequestGroupByMonthAndProductId(range.getStart(), range.getEnd(), null,
                    clientIds.size() > 0 ? clientIds.get(0) : null);
        }
        else
        {
            dataList = statsRequestMapper.getRequestGroupByDayAndProductId(range.getStart(), range.getEnd(), null,
                    clientIds.size() > 0 ? clientIds.get(0) : null);
        }
        List<Long> productIds = new ArrayList<>();
        for(StatsRequest item : dataList)
        {
            productIds.add(item.getProductId());
        }
        List<ProductResDTO> productResDTOS = productRpcService.getProductList(productIds);
        Map<Long, ProductResDTO> productResDTOMap = new HashMap<>();
        for(ProductResDTO item : productResDTOS)
        {
            productResDTOMap.put(item.getId(), item);
        }
        Map<Long, RequestStatsResDTO> requestStatsResDTOMap = new HashMap<>();
        handleStatsRequest(requestStatsResDTOS, unit, dataList, requestStatsResDTOMap, productResDTOMap);
        return requestStatsResDTOS;
    }

}
