package com.mingdong.schedule.scheduled;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerManage
{
    private static SchedulerFactory schedulerfactory = new StdSchedulerFactory();
    private static Scheduler scheduler = null;

    public static void addJob(JobDetail job, Trigger trigger)
    {
        try
        {
            if(scheduler == null)
            {
                scheduler = schedulerfactory.getScheduler();
                scheduler.start();
            }
            scheduler.scheduleJob(job, trigger);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
