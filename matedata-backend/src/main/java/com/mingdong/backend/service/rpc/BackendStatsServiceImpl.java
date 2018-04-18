package com.mingdong.backend.service.rpc;

import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.domain.entity.Job;
import com.mingdong.backend.domain.entity.JobLog;
import com.mingdong.backend.domain.entity.StatsClientRequest;
import com.mingdong.backend.domain.entity.StatsProductRequest;
import com.mingdong.backend.domain.entity.StatsRecharge;
import com.mingdong.backend.domain.entity.StatsSummary;
import com.mingdong.backend.domain.mapper.JobLogMapper;
import com.mingdong.backend.domain.mapper.JobMapper;
import com.mingdong.backend.domain.mapper.StatsClientRequestMapper;
import com.mingdong.backend.domain.mapper.StatsProductRequestMapper;
import com.mingdong.backend.domain.mapper.StatsRechargeMapper;
import com.mingdong.backend.domain.mapper.StatsSummaryMapper;
import com.mingdong.backend.model.SummaryStatsDTO;
import com.mingdong.backend.service.BackendStatsService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.JobLogReqDTO;
import com.mingdong.core.model.dto.request.StatsClientRequestReqDTO;
import com.mingdong.core.model.dto.request.StatsDTO;
import com.mingdong.core.model.dto.request.StatsProductRequestReqDTO;
import com.mingdong.core.model.dto.request.StatsRechargeDTO;
import com.mingdong.core.model.dto.response.DictRechargeTypeResDTO;
import com.mingdong.core.model.dto.response.RechargeStatsDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.StatsClientRequestResDTO;
import com.mingdong.core.model.dto.response.StatsProductRequestResDTO;
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
    private StatsClientRequestMapper statsClientRequestMapper;
    @Resource
    private StatsProductRequestMapper statsProductRequestMapper;
    @Resource
    private SystemRpcService systemRpcService;

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
        }
        // 昨日统计数据
        stats = statsSummaryMapper.getSummaryStatsByDate(yesterday);
        if(stats != null)
        {
            res.setRequestYesterday(stats.getRequest());
            res.setRequestFailedYesterday(stats.getRequestFailed());
            res.setRequest3rdFailedYesterday(stats.getRequest3rdFailed());
            res.setProfitAmountYesterday(stats.getProfit());
        }
        // 近7天统计数据
        stats = statsSummaryMapper.getSummaryStatsFromDate(latest7DaysFrom);
        if(stats != null)
        {
            res.setRechargeAmountIn7Days(stats.getRecharge());
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
        Map<Integer,String> rechargeTypeNameMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(list1)){
            for(DictRechargeTypeResDTO item : list1){
                rechargeTypeNameMap.put(item.getId(),item.getName());
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
        stats.setRequest(statsDTO.getClientRequest());
        stats.setRecharge(statsDTO.getClientRecharge());
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
    public ResponseDTO addStatsRequest(List<StatsClientRequestReqDTO> addStatsClientRequest,
            List<StatsProductRequestReqDTO> addStatsProductRequest)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        List<StatsClientRequest> statsClientRequests = new ArrayList<>();
        List<StatsProductRequest> statsProductRequests = new ArrayList<>();
        Date date = new Date();
        if(!CollectionUtils.isEmpty(addStatsClientRequest))
        {
            StatsClientRequest statsClientRequest;
            for(StatsClientRequestReqDTO item : addStatsClientRequest)
            {
                statsClientRequest = new StatsClientRequest();
                statsClientRequest.setCreateTime(date);
                statsClientRequest.setUpdateTime(date);
                statsClientRequest.setStatsDate(item.getStatsDate());
                statsClientRequest.setStatsDay(item.getStatsDay());
                statsClientRequest.setStatsHour(item.getStatsHour());
                statsClientRequest.setStatsMonth(item.getStatsMonth());
                statsClientRequest.setStatsWeek(item.getStatsWeek());
                statsClientRequest.setStatsYear(item.getStatsYear());
                statsClientRequest.setClientId(item.getClientId());
                statsClientRequest.setRequest(item.getRequest());
                statsClientRequests.add(statsClientRequest);
            }
            statsClientRequestMapper.addAll(statsClientRequests);
        }
        if(!CollectionUtils.isEmpty(addStatsProductRequest))
        {
            StatsProductRequest statsProductRequest;
            for(StatsProductRequestReqDTO item : addStatsProductRequest)
            {
                statsProductRequest = new StatsProductRequest();
                statsProductRequest.setCreateTime(date);
                statsProductRequest.setUpdateTime(date);
                statsProductRequest.setStatsDate(item.getStatsDate());
                statsProductRequest.setStatsDay(item.getStatsDay());
                statsProductRequest.setStatsHour(item.getStatsHour());
                statsProductRequest.setStatsMonth(item.getStatsMonth());
                statsProductRequest.setStatsWeek(item.getStatsWeek());
                statsProductRequest.setStatsYear(item.getStatsYear());
                statsProductRequest.setProductId(item.getProductId());
                statsProductRequest.setRequest(item.getRequest());
                statsProductRequests.add(statsProductRequest);
            }
            statsProductRequestMapper.addAll(statsProductRequests);
        }
        return responseDTO;
    }

    @Override
    public ListDTO<StatsProductRequestResDTO> getProductTrafficByProductIds(List<Long> productIds, Date beforeDate,
            Date afterDate)
    {
        ListDTO<StatsProductRequestResDTO> listDTO = new ListDTO<>();
        List<StatsProductRequest> list = statsProductRequestMapper.getProductTrafficByProductIds(productIds, beforeDate,
                afterDate);
        List<StatsProductRequestResDTO> dataList = new ArrayList<>();
        listDTO.setList(dataList);
        if(!CollectionUtils.isEmpty(list))
        {
            StatsProductRequestResDTO statsProductRequestResDTO;
            for(StatsProductRequest item : list)
            {
                statsProductRequestResDTO = new StatsProductRequestResDTO();
                statsProductRequestResDTO.setProductId(item.getProductId());
                statsProductRequestResDTO.setRequest(item.getRequest());
                statsProductRequestResDTO.setStatsDate(item.getStatsDate());
                statsProductRequestResDTO.setStatsDay(item.getStatsDay());
                statsProductRequestResDTO.setStatsHour(item.getStatsHour());
                statsProductRequestResDTO.setStatsMonth(item.getStatsMonth());
                statsProductRequestResDTO.setStatsWeek(item.getStatsWeek());
                statsProductRequestResDTO.setStatsYear(item.getStatsYear());
                dataList.add(statsProductRequestResDTO);
            }
        }
        return listDTO;
    }

    @Override
    public ListDTO<StatsClientRequestResDTO> getClientTrafficByClientIds(List<Long> clientIds, Date beforeDate,
            Date afterDate)
    {
        ListDTO<StatsClientRequestResDTO> listDTO = new ListDTO<>();
        List<StatsClientRequest> list = statsClientRequestMapper.getClientTrafficByClientIds(clientIds, beforeDate,
                afterDate);
        List<StatsClientRequestResDTO> dataList = new ArrayList<>();
        listDTO.setList(dataList);
        if(!CollectionUtils.isEmpty(list))
        {
            StatsClientRequestResDTO statsClientRequestResDTO;
            for(StatsClientRequest item : list)
            {
                statsClientRequestResDTO = new StatsClientRequestResDTO();
                statsClientRequestResDTO.setClientId(item.getClientId());
                statsClientRequestResDTO.setRequest(item.getRequest());
                statsClientRequestResDTO.setStatsDate(item.getStatsDate());
                statsClientRequestResDTO.setStatsDay(item.getStatsDay());
                statsClientRequestResDTO.setStatsHour(item.getStatsHour());
                statsClientRequestResDTO.setStatsMonth(item.getStatsMonth());
                statsClientRequestResDTO.setStatsWeek(item.getStatsWeek());
                statsClientRequestResDTO.setStatsYear(item.getStatsYear());
                dataList.add(statsClientRequestResDTO);
            }
        }
        return listDTO;
    }

}
