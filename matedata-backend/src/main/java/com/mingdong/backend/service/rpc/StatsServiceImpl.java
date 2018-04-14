package com.mingdong.backend.service.rpc;

import com.mingdong.backend.domain.entity.StatsSummary;
import com.mingdong.backend.domain.mapper.StatsSummaryMapper;
import com.mingdong.backend.model.SummaryStatsDTO;
import com.mingdong.backend.service.StatsService;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

public class StatsServiceImpl implements StatsService
{
    @Resource
    private StatsSummaryMapper statsSummaryMapper;

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
        res.setClientTotal(stats.getClientIncrement());
        res.setRequestTotal(stats.getRequest());
        res.setRequestFailedTotal(stats.getRequestFailed());
        res.setRequest3rdFailedTotal(stats.getRequest3rdFailed());
        // 今日统计数据
        stats = statsSummaryMapper.getSummaryStatsByDate(today);
        res.setRequestToday(stats.getRequest());
        res.setRequestFailedToday(stats.getRequestFailed());
        res.setRequest3rdFailedToday(stats.getRequest3rdFailed());
        res.setProfitAmountToday(stats.getProfit());
        // 昨日统计数据
        stats = statsSummaryMapper.getSummaryStatsByDate(yesterday);
        res.setRequestYesterday(stats.getRequest());
        res.setRequestFailedYesterday(stats.getRequestFailed());
        res.setRequest3rdFailedYesterday(stats.getRequest3rdFailed());
        res.setProfitAmountYesterday(stats.getProfit());
        // 近7天统计数据
        stats = statsSummaryMapper.getSummaryStatsFromDate(latest7DaysFrom);
        res.setRechargeAmountIn7Days(stats.getRecharge());
        // 近30天统计数据
        stats = statsSummaryMapper.getSummaryStatsFromDate(latest30DaysFrom);
        res.setClientIncIn30Days(stats.getClientIncrement());
        res.setRechargeAmountIn30Days(stats.getRecharge());
        // 本月统计数据
        stats = statsSummaryMapper.getSummaryStatsFromDate(thisMonthFrom);
        res.setRequestThisMonth(stats.getRequest());
        res.setRequestFailedThisMonth(stats.getRequestFailed());
        res.setRequest3rdFailedThisMonth(stats.getRequest3rdFailed());
        return res;
    }
}
