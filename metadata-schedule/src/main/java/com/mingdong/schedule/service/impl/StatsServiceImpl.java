package com.mingdong.schedule.service.impl;

import com.mingdong.core.service.RemoteStatsService;
import com.mingdong.schedule.service.StatsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class StatsServiceImpl implements StatsService
{
    @Resource
    private RemoteStatsService statsApi;

    @Override
    public void statsDataForHour(Date date)
    {
        statsApi.statsDataForHour(date);
    }
}
