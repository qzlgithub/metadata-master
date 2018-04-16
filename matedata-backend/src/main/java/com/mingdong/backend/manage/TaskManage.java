package com.mingdong.backend.manage;

import com.mingdong.backend.service.ScheduledService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class TaskManage
{
    @Resource
    private ScheduledService scheduledService;

    @Scheduled(cron = "0 3 * * * *")
    public void startStatsJob()
    {
        Date date = new Date();
        scheduledService.statsByData(date);
        scheduledService.statsRechargeByData(date);
        scheduledService.statsRequest(date);
    }

    @Scheduled(cron = "0 3 * * * ?")
    public void startCleanTrafficJob()
    {
        scheduledService.cleanTraffic(new Date());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void startClientRemindJob()
    {
        scheduledService.quartzClientRemind(new Date());
    }

}
