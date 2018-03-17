package com.mingdong.schedule.service.impl;

import com.mingdong.core.service.StatsRpcService;
import com.mingdong.schedule.service.StatsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class StatsServiceImpl implements StatsService
{
    @Resource
    private StatsRpcService statsRpcService;

    @Override
    public void statsByData(Date date)
    {
        statsRpcService.statsByData(date);
    }

    @Override
    public void statsRechargeByData(Date date)
    {
        statsRpcService.statsRechargeByData(date);
    }
}
