package com.mingdong.bop.scheduled;

import com.mingdong.bop.configurer.ApplicationContextProvider;
import com.mingdong.bop.service.QuartzService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

public class ClientRemindJob implements Job
{
    @Override
    public void execute(JobExecutionContext context)
    {
        try
        {
            QuartzService quartzService = ApplicationContextProvider.getBean(QuartzService.class);
            quartzService.quartzClientRemind(new Date());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
