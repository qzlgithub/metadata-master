package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.domain.mapper.StatsClientMapper;
import com.mingdong.bop.service.StatsService;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Service
public class StatsServiceImpl implements StatsService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private StatsClientMapper statsClientMapper;

    @Override
    public BLResp getIndexClientStats()
    {
        BLResp resp = BLResp.build();
        Calendar calendar = Calendar.getInstance();
        //处理成00:00:00
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        Date nowDay = calendar.getTime();//今日00:00:00
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date previousDay = calendar.getTime();//昨天00:00:00
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        Date weekDay = calendar.getTime();//近7天00:00:00
        calendar.add(Calendar.DAY_OF_MONTH, -23);
        Date monthDay = calendar.getTime();//近30天00:00:00
        Long allClientCount = statsClientMapper.getAllClientCount();
        Long clientCountByDate = statsClientMapper.getClientCountByDate(monthDay);
        BigDecimal clientRechargeByWeek = statsClientMapper.getClientRechargeByDate(weekDay);
        BigDecimal clientRechargeByMonth = statsClientMapper.getClientRechargeByDate(monthDay);
        resp.addData("allClientCount",allClientCount);
        resp.addData("clientCountByDate",clientCountByDate);
        resp.addData("clientRechargeByWeek",clientRechargeByWeek.toString());
        resp.addData("clientRechargeByMonth",clientRechargeByMonth.toString());
        return resp;
    }

}
