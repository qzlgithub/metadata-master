package com.mingdong.bop.scheduled;

import com.mingdong.bop.configurer.ApplicationContextProvider;
import com.mingdong.bop.service.QuartzService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

public class CleanTrafficJob implements Job
{
    @Override
    public void execute(JobExecutionContext jobExecutionContext)
    {
        try
        {
            QuartzService quartzService = ApplicationContextProvider.getBean(QuartzService.class);
            quartzService.cleanTraffic(new Date());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
