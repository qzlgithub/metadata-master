package com.mingdong.backend.service.impl;

import com.mingdong.backend.service.ScheduledService;
import com.mingdong.core.service.ClientRpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ScheduledServiceImpl implements ScheduledService
{
    @Resource
    private ClientRpcService clientRpcService;

    @Override
    public void statsByData(Date date)
    {
        clientRpcService.statsByDate(date);
    }

    @Override
    public void statsRechargeByData(Date date)
    {
        clientRpcService.statsRechargeByDate(date);
    }

    @Override
    public void quartzClientRemind(Date date)
    {
        clientRpcService.quartzClientRemind(date);
    }

    @Override
    public void statsRequest(Date date)
    {
        clientRpcService.statsRequestByDate(date);
    }

}
