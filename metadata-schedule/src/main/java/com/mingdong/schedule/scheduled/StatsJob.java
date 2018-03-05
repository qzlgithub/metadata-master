package com.mingdong.schedule.scheduled;

import com.mingdong.schedule.configurer.ApplicationContextProvider;
import com.mingdong.schedule.service.StatsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

public class StatsJob implements Job
{

    @Override
    public void execute(JobExecutionContext context)
    {
        try
        {
            StatsService statsService = ApplicationContextProvider.getBean(StatsService.class);
            statsService.statsDataForHour(new Date());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
