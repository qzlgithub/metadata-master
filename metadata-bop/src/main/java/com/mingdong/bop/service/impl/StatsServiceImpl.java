package com.mingdong.bop.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.bop.domain.entity.ClientInfo;
import com.mingdong.bop.domain.mapper.ClientInfoMapper;
import com.mingdong.bop.domain.mapper.StatsClientMapper;
import com.mingdong.bop.service.StatsService;
import com.mingdong.bop.util.DateCalculateUtils;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private StatsClientMapper statsClientMapper;
    @Resource
    private ClientInfoMapper clientInfoMapper;

    @Override
    public BLResp getIndexStats()
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        Date nowDay = DateCalculateUtils.getCurrentDate(currentDay);//今日00:00:00
        Date previousDay = DateCalculateUtils.getBeforeDayDate(currentDay, 1, true);//昨天00:00:00
        Date weekDay = DateCalculateUtils.getBeforeDayDate(currentDay, 6, true);//近7天00:00:00
        Date monthDay = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);//近30天00:00:00
        Long allClientCount = statsClientMapper.getAllClientCount();
        Long clientCountByDate = statsClientMapper.getClientCountByDate(monthDay);
        BigDecimal clientRechargeByWeek = statsClientMapper.getClientRechargeByDate(weekDay);
        BigDecimal clientRechargeByMonth = statsClientMapper.getClientRechargeByDate(monthDay);
        resp.addData(Field.ALL_CLIENT_COUNT, allClientCount);
        resp.addData(Field.CLIENT_COUNT_BY_DATE, clientCountByDate);
        resp.addData(Field.CLIENT_RECHARGE_BY_WEEK, NumberUtils.formatAmount(clientRechargeByWeek));
        resp.addData(Field.CLIENT_RECHARGE_BY_MONTH, NumberUtils.formatAmount(clientRechargeByMonth));

        return resp;
    }

    @Override
    public BLResp getClientIndexStats()
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        Date weekDay = DateCalculateUtils.getBeforeDayDate(currentDay, 6, true);//近7天00:00:00
        Date monthFrist = DateCalculateUtils.getCurrentMonthFirst(currentDay, true);//当前月第一天00:00:00
        Long allClientCount = statsClientMapper.getAllClientCount();
        Long clientCountByWeek = statsClientMapper.getClientCountByDate(weekDay);
        Long clientCountByMonthFrist = statsClientMapper.getClientCountByDate(monthFrist);
        resp.addData(Field.ALL_CLIENT_COUNT, allClientCount);
        resp.addData(Field.CLIENT_COUNT_BY_WEEK, clientCountByWeek);
        resp.addData(Field.CLIENT_COUNT_BY_MONTH_FRIST, clientCountByMonthFrist);
        return resp;
    }

    @Override
    public BLResp getClientList(ScopeType scopeTypeEnum, Page page)
    {
        BLResp resp = BLResp.build();
        Date currentDay = new Date();
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(ScopeType.MONTH == scopeTypeEnum)
        {

            Date date = DateCalculateUtils.getBeforeDayDate(currentDay, 29, true);
            Long total = statsClientMapper.getClientCountByDate(date);
            int pages = page.getTotalPage(total.intValue());
            resp.addData(Field.TOTAL, total);
            resp.addData(Field.PAGES, pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                getClientInfoList(page, date, resp);
            }
        }
        else if(ScopeType.QUARTER == scopeTypeEnum)
        {
            Date date = DateCalculateUtils.getCurrentQuarterFirstDate(currentDay, true);
            Long total = statsClientMapper.getClientCountByDate(date);
            int pages = page.getTotalPage(total.intValue());
            resp.addData(Field.TOTAL, total);
            resp.addData(Field.PAGES, pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                getClientInfoList(page, date, resp);
            }
        }
        else if(ScopeType.TYPE == scopeTypeEnum)
        {
            Date date = DateCalculateUtils.getBeforeDayDate(currentDay, 364, true);
            Long total = statsClientMapper.getClientCountByDate(date);
            int pages = page.getTotalPage(total.intValue());
            resp.addData(Field.TOTAL, total);
            resp.addData(Field.PAGES, pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                getClientInfoList(page, date, resp);
            }
        }
        else
        {
            return resp.result(RestResult.PARAMETER_ERROR);
        }
        return resp;
    }

    private void getClientInfoList(Page page, Date date, BLResp resp)
    {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
        List<ClientInfo> clientInfoListByDate = clientInfoMapper.getClientInfoListByDate(date);
        List<Map<String, Object>> dataList = getClientInfoDateList(clientInfoListByDate);
        resp.addData(Field.LIST, dataList);
    }

    private List<Map<String, Object>> getClientInfoDateList(List<ClientInfo> clientInfoListByDate)
    {
        List<Map<String, Object>> dataList = new ArrayList<>(clientInfoListByDate.size());
        Map<String, Object> map;
        for(ClientInfo item : clientInfoListByDate)
        {
            map = new HashMap<>();
            map.put(Field.REGISTER_DATE, DateUtils.format(item.getRegisterTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
            map.put(Field.CORP_NAME, item.getCorpName());
            map.put(Field.SHORT_NAME, item.getShortName());
            map.put(Field.USERNAME, item.getUsername());
            map.put(Field.NAME, item.getName());
            map.put(Field.PHONE, item.getPhone());
            map.put(Field.EMAIL, item.getEmail());
            map.put(Field.MANAGER_NAME, item.getManagerName());
            dataList.add(map);
        }
        return dataList;
    }

}
