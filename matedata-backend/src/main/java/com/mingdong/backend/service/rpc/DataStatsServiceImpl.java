package com.mingdong.backend.service.rpc;

import com.mingdong.backend.domain.entity.Stats;
import com.mingdong.backend.domain.entity.StatsRecharge;
import com.mingdong.backend.domain.mapper.StatsMapper;
import com.mingdong.backend.domain.mapper.StatsRechargeMapper;
import com.mingdong.backend.service.DataStatsService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStatsServiceImpl implements DataStatsService
{
    private static final Integer INC_STAT = 1;
    private static final Integer REQ_STAT = 2;
    private static final Integer RCG_STAT = 3;

    @Resource
    private StatsMapper statsMapper;
    @Resource
    private StatsRechargeMapper statsRechargeMapper;

    @Override
    public Map<String, Integer> getClientIncreaseTrend(DateRange dateRange, RangeUnit rangeUnit)
    {
        Map<String, Integer> map = new HashMap<>();
        if(rangeUnit == RangeUnit.HOUR)
        {
            List<Stats> statsList = statsMapper.getListGroupByHour(dateRange.getStart(), dateRange.getEnd(), INC_STAT);
            for(Stats o : statsList)
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
                List<Stats> statsList1 = statsMapper.getListGroupByDay(dateRange.getStart(), dateRange.getEnd(),
                        INC_STAT);
                for(Stats o : statsList1)
                {
                    map.put(DateUtils.format(o.getStatsDate(), DateFormat.YYYY_MM_DD_2), o.getClientIncrement());
                }
            }
            else if(rangeUnit == RangeUnit.WEEK)
            {
                List<Stats> statsList2 = statsMapper.getListGroupByWeek(dateRange.getStart(), dateRange.getEnd(),
                        INC_STAT);
                for(Stats o : statsList2)
                {
                    map.put(o.getStatsYear() + String.format("%02d", o.getStatsWeek()), o.getClientIncrement());
                }
            }
            else
            {
                List<Stats> statsList3 = statsMapper.getListGroupByMonth(dateRange.getStart(), dateRange.getEnd(),
                        INC_STAT);
                for(Stats o : statsList3)
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
        Stats stats = new Stats();
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
        stats.setClientRequest(statsDTO.getClientRequest());
        stats.setClientRecharge(statsDTO.getClientRecharge());
        statsMapper.add(stats);
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
