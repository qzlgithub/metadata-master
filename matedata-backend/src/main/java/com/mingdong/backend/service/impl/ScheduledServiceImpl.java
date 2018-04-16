package com.mingdong.backend.service.impl;

import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.domain.entity.Job;
import com.mingdong.backend.domain.entity.JobLog;
import com.mingdong.backend.domain.mapper.JobLogMapper;
import com.mingdong.backend.domain.mapper.JobMapper;
import com.mingdong.backend.service.ScheduledService;
import com.mingdong.core.constant.JobType;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.service.ClientRpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ScheduledServiceImpl implements ScheduledService
{
    private SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private RedisDao redisDao;
    @Resource
    private ClientRpcService clientRpcService;
    @Resource
    private JobLogMapper jobLogMapper;
    @Resource
    private JobMapper jobMapper;

    @Override
    public void statsByData(Date date)
    {
        clientRpcService.statsByDate(date);
    }

    @Override
    public void statsRechargeByData(Date date)
    {
        clientRpcService.statsRechargeByDate(date);
    }

    @Override
    public void quartzClientRemind(Date date)
    {
        clientRpcService.quartzClientRemind(date);
    }

    @Override
    public void cleanTraffic(Date date)
    {
        try
        {
            redisDao.cleanUpTraffic(date.getTime() / 1000);
            saveJobLog(JobType.CLEAN_TRAFFIC, TrueOrFalse.TRUE, null);
        }
        catch(Exception e)
        {
            saveJobLog(JobType.CLEAN_TRAFFIC, TrueOrFalse.FALSE,
                    JobType.CLEAN_TRAFFIC.getName() + ":" + longSdf.format(date));
        }
    }

    @Override
    public void statsRequest(Date date)
    {
        clientRpcService.statsRequestByDate(date);
    }

    private void saveJobLog(JobType jobType, Integer success, String remark)
    {
        try
        {
            JobLog addJobLog = new JobLog();
            Date date = new Date();
            addJobLog.setCreateTime(date);
            addJobLog.setJobCode(jobType.getCode());
            addJobLog.setSuccess(success);
            addJobLog.setRemark(remark);
            jobLogMapper.add(addJobLog);
            if(TrueOrFalse.TRUE.equals(success))
            {
                Job job = new Job();
                job.setCode(jobType.getCode());
                job.setLastSucTime(date);
                jobMapper.updateSkipNull(job);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}