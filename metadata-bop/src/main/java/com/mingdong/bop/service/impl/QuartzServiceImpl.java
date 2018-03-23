package com.mingdong.bop.service.impl;

import com.mingdong.bop.service.QuartzService;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.StatsRpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class QuartzServiceImpl implements QuartzService
{
    @Resource
    private StatsRpcService statsRpcService;
    @Resource
    private ClientRpcService clientRpcService;

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

    @Override
    public void quartzClientRemind(Date date)
    {

    }
}
