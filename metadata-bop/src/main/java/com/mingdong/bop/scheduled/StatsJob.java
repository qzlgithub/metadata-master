package com.mingdong.bop.scheduled;

import com.mingdong.bop.configurer.ApplicationContextProvider;
import com.mingdong.bop.service.QuartzService;
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
            QuartzService quartzService = ApplicationContextProvider.getBean(QuartzService.class);
            Date date = new Date();
            quartzService.statsByData(date);
            quartzService.statsRechargeByData(date);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
