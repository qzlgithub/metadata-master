package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.StatsDateInfoDTO;
import com.mingdong.core.service.RemoteStatsService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.core.util.IDUtils;
import com.mingdong.mis.domain.entity.ClientInfo;
import com.mingdong.mis.domain.entity.ProductRechargeInfo;
import com.mingdong.mis.domain.entity.Stats;
import com.mingdong.mis.domain.entity.StatsDateInfo;
import com.mingdong.mis.domain.mapper.ApiReqMapper;
import com.mingdong.mis.domain.mapper.ClientInfoMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeInfoMapper;
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

public class RemoteStatsServiceImpl implements RemoteStatsService
{
    private static Logger logger = LoggerFactory.getLogger(RemoteStatsServiceImpl.class);
    @Resource
    private StatsClientMapper statsClientMapper;
    @Resource
    private ClientInfoMapper clientInfoMapper;
    @Resource
    private ProductRechargeInfoMapper productRechargeInfoMapper;
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
    public ListDTO<ClientInfoDTO> getClientInfoListByDate(Date date, Date currentDay, Page page)
    {
        ListDTO<ClientInfoDTO> listDTO = new ListDTO<>();
        int total = statsClientMapper.getClientCountByDate(date, currentDay);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ClientInfo> dataList = clientInfoMapper.getClientInfoListByDate(date, currentDay);
            if(!CollectionUtils.isEmpty(dataList))
            {
                List<ClientInfoDTO> list = new ArrayList<>(dataList.size());
                for(ClientInfo o : dataList)
                {
                    ClientInfoDTO ci = new ClientInfoDTO();
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
    public ListDTO<ClientInfoDTO> getClientInfoListByDate(Date fromDate, Date toDate)
    {
        ListDTO<ClientInfoDTO> listDTO = new ListDTO<>();
        List<ClientInfo> dataList = clientInfoMapper.getClientInfoListByDate(fromDate, toDate);
        listDTO.setTotal(dataList.size());
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<ClientInfoDTO> list = new ArrayList<>();
            for(ClientInfo o : dataList)
            {
                ClientInfoDTO ci = new ClientInfoDTO();
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
    public ListDTO<ProductRechargeInfoDTO> getProductRechargeInfoListBy(Date date, Date currentDay, Page page)
    {
        ListDTO<ProductRechargeInfoDTO> listDTO = new ListDTO<>();
        int total = statsClientMapper.countClientRechargeByDate(date, currentDay);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductRechargeInfo> productRechargeInfoList = productRechargeInfoMapper.getListBy(null, null, date,
                    currentDay);
            if(!CollectionUtils.isEmpty(productRechargeInfoList))
            {
                List<ProductRechargeInfoDTO> dataList = new ArrayList<>();
                findProductRechargeInfoDTO(productRechargeInfoList, dataList);
                listDTO.setList(dataList);
            }
        }
        return listDTO;
    }

    @Override
    public ListDTO<ProductRechargeInfoDTO> getRechargeInfoListBy(Date fromDate, Date toDate)
    {
        ListDTO<ProductRechargeInfoDTO> listDTO = new ListDTO<>();
        List<ProductRechargeInfo> productRechargeInfoList = productRechargeInfoMapper.getListByTime(fromDate, toDate);
        listDTO.setTotal(productRechargeInfoList.size());
        if(!CollectionUtils.isEmpty(productRechargeInfoList))
        {
            List<ProductRechargeInfoDTO> list = new ArrayList<>();
            for(ProductRechargeInfo o : productRechargeInfoList)
            {
                ProductRechargeInfoDTO pri = new ProductRechargeInfoDTO();
                pri.setRechargeType(o.getRechargeType());
                pri.setAmount(o.getAmount());
                pri.setBalance(o.getBalance());
                pri.setTradeTime(o.getTradeTime());
                list.add(pri);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public Integer getClientRechargeCountByDate(Date date, Date currentDay)
    {
        return statsClientMapper.countClientRechargeByDate(date, currentDay);
    }

    @Override
    public ListDTO<StatsDateInfoDTO> getRequestListStats(Date beforeDate, Date currentDay, String name, Long productId)
    {
        List<StatsDateInfo> list = statsClientMapper.getRequestListStats(beforeDate, currentDay, name, productId);
        ListDTO<StatsDateInfoDTO> listDTO = new ListDTO<>();
        List<StatsDateInfoDTO> listData = new ArrayList<>();
        StatsDateInfoDTO statsDateDTO;
        for(StatsDateInfo item : list)
        {
            statsDateDTO = new StatsDateInfoDTO();
            statsDateDTO.setDate(item.getDate());
            statsDateDTO.setCount(item.getCount());
            statsDateDTO.setMissCount(item.getMissCount());
            listData.add(statsDateDTO);
        }
        listDTO.setList(listData);
        return listDTO;
    }

    @Override
    public ListDTO<StatsDateInfoDTO> getRevenueListStats(Date fromDate, Date toDate)
    {
        List<StatsDateInfo> list = statsClientMapper.getRevenueListStats(fromDate, toDate);
        ListDTO<StatsDateInfoDTO> listDTO = new ListDTO<>();
        List<StatsDateInfoDTO> listData = new ArrayList<>();
        StatsDateInfoDTO statsDateDTO;
        for(StatsDateInfo item : list)
        {
            statsDateDTO = new StatsDateInfoDTO();
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
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR_OF_DAY), 0, 0);
        Date hourBefore = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date hourAfter = calendar.getTime();
        Date day;
        try
        {
            day = longSdf.parse(shortSdf.format(calendar.getTime()) + " 00:00:00");
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
            return;
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        List<Stats> statsList = statsMapper.findStatsBy(day, hour);
        if(!CollectionUtils.isEmpty(statsList))
        {
            logger.error("......");
            return;
        }
        Integer clientCount = statsClientMapper.getClientCountByDate(hourBefore, hourAfter);
        int requestCount = apiReqMapper.countBy(null, null, null, hourBefore, hourAfter);
        BigDecimal rechargeSum = statsClientMapper.getClientRechargeByDate(hourBefore, hourAfter);
        Stats stats = new Stats();
        stats.setId(IDUtils.getStatsId(1));
        Date nowDate = new Date();
        stats.setCreateTime(nowDate);
        stats.setUpdateTime(nowDate);
        stats.setStatsDay(day);
        stats.setStatsHour(hour);
        stats.setClientIncrement(clientCount != null ? clientCount : 0);
        stats.setClientRequest(Long.valueOf(requestCount + ""));
        stats.setClientRecharge(rechargeSum != null ? rechargeSum : new BigDecimal(0));
        statsMapper.add(stats);
    }

    private void findClientInfoDTO(List<ClientInfo> clientInfoList, List<ClientInfoDTO> dataList)
    {
        ClientInfoDTO clientInfoDTO;
        for(ClientInfo item : clientInfoList)
        {
            clientInfoDTO = new ClientInfoDTO();
            EntityUtils.copyProperties(item, clientInfoDTO);
            dataList.add(clientInfoDTO);
        }
    }

    private void findProductRechargeInfoDTO(List<ProductRechargeInfo> productRechargeInfoList,
            List<ProductRechargeInfoDTO> dataList)
    {
        ProductRechargeInfoDTO productRechargeInfoDTO;
        for(ProductRechargeInfo item : productRechargeInfoList)
        {
            productRechargeInfoDTO = new ProductRechargeInfoDTO();
            dataList.add(productRechargeInfoDTO);
            EntityUtils.copyProperties(item, productRechargeInfoDTO);
        }
    }
}
