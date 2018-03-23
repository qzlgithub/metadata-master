package com.mingdong.bop.scheduled;

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
     * 定时任务-统计任务
     */
    @PostConstruct
    private void statsJob()
    {
        JobDetail jobDetail = JobBuilder.newJob(StatsJob.class).withIdentity("statsJob", "jobGroup").build();
        //        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");//每十秒
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 3 0-23 * * ?");//每小时的第3分钟开始执行
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("statsJob", "jobGroup").withSchedule(
                scheduleBuilder).build();
        SchedulerManage.addJob(jobDetail, cronTrigger);
    }

    /**
     * 定时任务-客户服务提醒
     */
    @PostConstruct
    private void clientRemindJob()
    {
        JobDetail jobDetail = JobBuilder.newJob(ClientRemindJob.class)
                .withIdentity("clientRemindJob", "jobGroup")
                .build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 1 * * ?");//每天凌晨1点执行执行
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("clientRemindJob", "jobGroup").withSchedule(
                scheduleBuilder).build();
        SchedulerManage.addJob(jobDetail, cronTrigger);
    }

}
