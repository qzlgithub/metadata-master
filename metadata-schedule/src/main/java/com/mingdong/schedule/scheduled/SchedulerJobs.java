package com.mingdong.schedule.scheduled;

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
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 3 0-23 * * ?");//每小时的第3分钟开始执行
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("statsJob", "jobGroup").withSchedule(
                scheduleBuilder).build();
        SchedulerManage.addJob(jobDetail, cronTrigger);
    }

}
