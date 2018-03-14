package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.IntervalReqDTO;
import com.mingdong.core.model.dto.response.ClientInfoResDTO;
import com.mingdong.core.model.dto.response.StatsDateInfoResDTO;
import com.mingdong.core.service.StatsRpcService;
import com.mingdong.mis.domain.entity.ClientInfo;
import com.mingdong.mis.domain.entity.Stats;
import com.mingdong.mis.domain.entity.StatsDateInfo;
import com.mingdong.mis.domain.mapper.ApiReqMapper;
import com.mingdong.mis.domain.mapper.ClientInfoMapper;
import com.mingdong.mis.domain.mapper.StatsClientMapper;
import com.mingdong.mis.domain.mapper.StatsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatsRpcServiceImpl implements StatsRpcService
{
    private static final Logger logger = LoggerFactory.getLogger(StatsRpcServiceImpl.class);
    @Resource
    private StatsClientMapper statsClientMapper;
    @Resource
    private ClientInfoMapper clientInfoMapper;
    @Resource
    private ApiReqMapper apiReqMapper;
    @Resource
    private StatsMapper statsMapper;

    @Override
    public Integer getAllClientCount()
    {
        return statsClientMapper.getAllClientCount();
    }

    @Override
    public Integer getClientCountByDate(Date monthDay, Date currentDay)
    {
        return statsClientMapper.getClientCountByDate(monthDay, currentDay);
    }

    @Override
    public ListDTO<ClientInfoResDTO> getClientInfoListByDate(Date date, Date currentDay, Page page)
    {
        ListDTO<ClientInfoResDTO> listDTO = new ListDTO<>();
        int total = statsClientMapper.getClientCountByDate(date, currentDay);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientInfo> dataList = clientInfoMapper.getClientInfoListByDate(date, currentDay);
            if(!CollectionUtils.isEmpty(dataList))
            {
                List<ClientInfoResDTO> list = new ArrayList<>(dataList.size());
                for(ClientInfo o : dataList)
                {
                    ClientInfoResDTO ci = new ClientInfoResDTO();
                    ci.setRegisterTime(o.getRegisterTime());
                    ci.setCorpName(o.getCorpName());
                    ci.setShortName(o.getShortName());
                    ci.setUsername(o.getUsername());
                    ci.setManagerName(o.getManagerName());
                    list.add(ci);
                }
                listDTO.setList(list);
            }
        }
        return listDTO;
    }

    @Override
    public ListDTO<ClientInfoResDTO> getClientInfoListByDate(Date fromDate, Date toDate)
    {
        ListDTO<ClientInfoResDTO> listDTO = new ListDTO<>();
        List<ClientInfo> dataList = clientInfoMapper.getClientInfoListByDate(fromDate, toDate);
        listDTO.setTotal(dataList.size());
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<ClientInfoResDTO> list = new ArrayList<>();
            for(ClientInfo o : dataList)
            {
                ClientInfoResDTO ci = new ClientInfoResDTO();
                ci.setRegisterTime(o.getRegisterTime());
                ci.setCorpName(o.getCorpName());
                ci.setShortName(o.getShortName());
                ci.setUsername(o.getUsername());
                ci.setManagerName(o.getManagerName());
                list.add(ci);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public BigDecimal getClientRechargeStatsByDate(Date nowDay, Date currentDay)
    {
        return statsClientMapper.getClientRechargeByDate(nowDay, currentDay);
    }

    @Override
    public BigDecimal getClientRechargeStatsAll()
    {
        return statsClientMapper.getClientRechargeAll();
    }

    @Override
    public Integer getClientRechargeCountByDate(Date date, Date currentDay)
    {
        return statsClientMapper.countClientRechargeByDate(date, currentDay);
    }

    @Override
    public ListDTO<StatsDateInfoResDTO> getRequestListStats(Date beforeDate, Date currentDay, String name,
            Long productId)
    {
        List<StatsDateInfo> list = statsClientMapper.getRequestListStats(beforeDate, currentDay, name, productId);
        ListDTO<StatsDateInfoResDTO> listDTO = new ListDTO<>();
        List<StatsDateInfoResDTO> listData = new ArrayList<>();
        StatsDateInfoResDTO statsDateDTO;
        for(StatsDateInfo item : list)
        {
            statsDateDTO = new StatsDateInfoResDTO();
            statsDateDTO.setDate(item.getDate());
            statsDateDTO.setCount(item.getCount());
            statsDateDTO.setMissCount(item.getMissCount());
            listData.add(statsDateDTO);
        }
        listDTO.setList(listData);
        return listDTO;
    }

    @Override
    public ListDTO<StatsDateInfoResDTO> getRevenueListStats(Date fromDate, Date toDate)
    {
        List<StatsDateInfo> list = statsClientMapper.getRevenueListStats(fromDate, toDate);
        ListDTO<StatsDateInfoResDTO> listDTO = new ListDTO<>();
        List<StatsDateInfoResDTO> listData = new ArrayList<>();
        StatsDateInfoResDTO statsDateDTO;
        for(StatsDateInfo item : list)
        {
            statsDateDTO = new StatsDateInfoResDTO();
            statsDateDTO.setDate(item.getDate());
            statsDateDTO.setFee(item.getFee());
            listData.add(statsDateDTO);
        }
        listDTO.setList(listData);
        return listDTO;
    }

    @Override
    @Transactional
    public void statsDataForHour(Date date)
    {
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour == 0)
        {
            hour = 24;
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR_OF_DAY), 0, 0);
        Date hourAfter = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date hourBefore = calendar.getTime();
        Date dayDate;
        try
        {
            dayDate = longSdf.parse(shortSdf.format(calendar.getTime()) + " 00:00:00");
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
            return;
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        List<Stats> statsList = statsMapper.findStatsBy(dayDate, hour);
        if(!CollectionUtils.isEmpty(statsList))
        {
            logger.info("定时统计---" + longSdf.format(calendar.getTime()) + "统计已存在！");
            return;
        }
        int clientCount = statsClientMapper.getClientCountByDate(hourBefore, hourAfter);
        int requestCount = apiReqMapper.countBy(null, null, null, hourBefore, hourAfter);
        BigDecimal rechargeSum = statsClientMapper.getClientRechargeByDate(hourBefore, hourAfter);
        if(clientCount == 0 && requestCount == 0 && "0.00".equals(NumberUtils.formatAmount(rechargeSum)))
        {
            logger.info("定时统计---" + longSdf.format(calendar.getTime()) + "没有数据可记录！");
            return;
        }
        logger.info("定时统计---" + longSdf.format(calendar.getTime()));
        Stats stats = new Stats();
        Date nowDate = new Date();
        logger.info("定时统计nowDate===" + longSdf.format(nowDate));
        stats.setCreateTime(nowDate);
        stats.setUpdateTime(nowDate);
        stats.setStatsYear(year);
        stats.setStatsMonth(month);
        stats.setStatsWeek(week);
        stats.setStatsDay(day);
        stats.setStatsHour(hour);
        stats.setStatsDate(dayDate);
        stats.setClientIncrement(clientCount);
        stats.setClientRequest(Long.valueOf(requestCount + ""));
        stats.setClientRecharge(rechargeSum != null ? rechargeSum : new BigDecimal(0));
        statsMapper.add(stats);
    }

    @Override
    public void clientAccessTrend(List<Long> clientIdList, List<IntervalReqDTO> intervalList) // TODO 客户请求走势图
    {
        if(!CollectionUtils.isEmpty(clientIdList) || !CollectionUtils.isEmpty(intervalList))
        {
            return;
        }
        logger.info("{}: {}", clientIdList, intervalList);
    }
}
