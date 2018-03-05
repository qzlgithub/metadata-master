package com.mingdong.schedule.scheduled;

import com.mingdong.schedule.configurer.SchedulerConfiguration;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SchedulerJobs
{

    /**
     * 统计任务
     */
    @PostConstruct
    private void statsJob()
    {
        JobDetail jobDetail = JobBuilder.newJob(StatsJob.class).withIdentity("statsJob", "jobGroup").build();
        //        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");//每十秒
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 0/1 * * ?");//每整点
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("statsJob", "jobGroup").withSchedule(
                scheduleBuilder).build();
        SchedulerConfiguration.addJob(jobDetail, cronTrigger);
    }

}
