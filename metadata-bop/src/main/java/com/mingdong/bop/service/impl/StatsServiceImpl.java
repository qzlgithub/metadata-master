package com.mingdong.bop.service.impl;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.domain.mapper.StatsClientMapper;
import com.mingdong.bop.model.BLResp;
import com.mingdong.bop.service.StatsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        Date weekDay = calendar.getTime();

        return null;
    }

    @Override
    public void getIndexStats(BLResp resp)
    {

    }
}
