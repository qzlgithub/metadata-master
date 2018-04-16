package com.mingdong.backend.service.rpc;

import com.mingdong.backend.domain.entity.StatsRecharge;
import com.mingdong.backend.domain.entity.StatsSummary;
import com.mingdong.backend.domain.mapper.StatsRechargeMapper;
import com.mingdong.backend.domain.mapper.StatsSummaryMapper;
import com.mingdong.backend.model.SummaryStatsDTO;
import com.mingdong.backend.service.BackendStatsService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.dto.request.StatsDTO;
import com.mingdong.core.model.dto.request.StatsRechargeDTO;
import com.mingdong.core.model.dto.response.RechargeStatsDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;

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
    private StatsSummaryMapper statsSummaryMapper;
    @Resource
    private StatsRechargeMapper statsRechargeMapper;

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
        }
        // 今日统计数据
        stats = statsSummaryMapper.getSummaryStatsByDate(today);
        if(stats != null)
        {
            res.setRequestToday(stats.getRequest());
            res.setRequestFailedToday(stats.getRequestFailed());
            res.setRequest3rdFailedToday(stats.getRequest3rdFailed());
            res.setProfitAmountToday(stats.getProfit());
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
        if(rangeUnit == RangeUnit.DAY)
        {
            dataList = statsRechargeMapper.getListGroupByDay(dateRange.getStart(), dateRange.getEnd());
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
            dataList = statsRechargeMapper.getListGroupByHour(dateRange.getStart(), dateRange.getEnd());
        }
        for(StatsRecharge o : dataList)
        {
            String name;
            if(rangeUnit == RangeUnit.DAY)
            {
                name = DateUtils.format(o.getStatsDate(), DateFormat.YYYY_MM_DD_2);
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
                name = o.getStatsHour() + "";
            }
            List<RechargeStatsDTO> list = map.get(name);
            if(CollectionUtils.isEmpty(list))
            {
                list = new ArrayList<>();
            }
            RechargeStatsDTO dto = new RechargeStatsDTO();
            dto.setRechargeTypeName(o.getRechargeTypeName());
            dto.setAmount(o.getAmount());
            list.add(dto);
            map.put(name, list);
        }
        return map;
    }

    @Override
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
}
