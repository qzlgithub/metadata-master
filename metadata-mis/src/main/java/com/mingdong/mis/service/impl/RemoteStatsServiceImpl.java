package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.model.dto.ResultDTO;
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
    public ClientInfoListDTO getClientInfoListByDate(Date date, Date currentDay, Page page)
    {
        ClientInfoListDTO clientInfoListDTO = new ClientInfoListDTO();
        List<ClientInfoDTO> dataList = new ArrayList<>();
        clientInfoListDTO.setDataList(dataList);
        if(page == null)
        {
            List<ClientInfo> clientInfoList = clientInfoMapper.getClientInfoListByDate(date, currentDay);
            if(!CollectionUtils.isEmpty(clientInfoList))
            {
                findClientInfoDTO(clientInfoList, dataList);
            }
        }
        else
        {
            int total = statsClientMapper.getClientCountByDate(date, currentDay);
            int pages = page.getTotalPage(total);
            clientInfoListDTO.setPages(pages);
            clientInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ClientInfo> clientInfoList = clientInfoMapper.getClientInfoListByDate(date, currentDay);
                if(!CollectionUtils.isEmpty(clientInfoList))
                {
                    findClientInfoDTO(clientInfoList, dataList);
                }
            }
        }
        return clientInfoListDTO;
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
    public ProductRechargeInfoListDTO getProductRechargeInfoListBy(Date date, Date currentDay, Page page)
    {
        ProductRechargeInfoListDTO productRechargeInfoListDTO = new ProductRechargeInfoListDTO();
        List<ProductRechargeInfoDTO> dataList = new ArrayList<>();
        productRechargeInfoListDTO.setDataList(dataList);
        if(page == null)
        {
            List<ProductRechargeInfo> productRechargeInfoList = productRechargeInfoMapper.getListBy(null, null, date,
                    currentDay);
            if(!CollectionUtils.isEmpty(productRechargeInfoList))
            {
                findProductRechargeInfoDTO(productRechargeInfoList, dataList);
            }
        }
        else
        {
            int total = statsClientMapper.countClientRechargeByDate(date, currentDay);
            int pages = page.getTotalPage(total);
            productRechargeInfoListDTO.setPages(pages);
            productRechargeInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ProductRechargeInfo> productRechargeInfoList = productRechargeInfoMapper.getListBy(null, null,
                        date, currentDay);
                if(!CollectionUtils.isEmpty(productRechargeInfoList))
                {
                    findProductRechargeInfoDTO(productRechargeInfoList, dataList);
                }
            }
        }
        return productRechargeInfoListDTO;
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
    public ResultDTO statsDataForHour(Date date)
    {
        ResultDTO resultDTO = new ResultDTO();
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
            e.printStackTrace();
            resultDTO.setResult(RestResult.SYSTEM_ERROR);
            return resultDTO;
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        List<Stats> statsList = statsMapper.findStatsBy(day, hour);
        if(!CollectionUtils.isEmpty(statsList))
        {
            resultDTO.setResult(RestResult.STATS_EXIST);
            return resultDTO;
        }
        Integer clientCount = statsClientMapper.getClientCountByDate(hourBefore, hourAfter);
        Long requestCount = apiReqMapper.countBy(null, null, null, hourBefore, hourAfter);
        BigDecimal rechargeSum = statsClientMapper.getClientRechargeByDate(hourBefore, hourAfter);
        Stats stats = new Stats();
        stats.setId(IDUtils.getStatsId(1));
        Date nowDate = new Date();
        stats.setCreateTime(nowDate);
        stats.setUpdateTime(nowDate);
        stats.setStatsDay(day);
        stats.setStatsHour(hour);
        stats.setClientIncrement(clientCount != null ? clientCount : 0);
        stats.setClientRequest(requestCount != null ? requestCount : 0);
        stats.setClientRecharge(rechargeSum != null ? rechargeSum : new BigDecimal(0));
        statsMapper.add(stats);
        return resultDTO;
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
