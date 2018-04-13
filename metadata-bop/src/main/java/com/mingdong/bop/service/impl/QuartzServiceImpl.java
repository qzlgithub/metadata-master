package com.mingdong.bop.service.impl;

import com.mingdong.backend.service.TrafficService;
import com.mingdong.bop.service.QuartzService;
import com.mingdong.core.constant.JobType;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.request.JobLogReqDTO;
import com.mingdong.core.service.ClientRpcService;
import com.mingdong.core.service.SystemRpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class QuartzServiceImpl implements QuartzService
{
    private SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private ClientRpcService clientRpcService;
    @Resource
    private SystemRpcService systemRpcService;
    @Resource
    private TrafficService trafficService;

    @Override
    public void statsByData(Date date)
    {
        try
        {
            clientRpcService.statsByDate(date);
            saveJobLog(JobType.STATS_ALL, TrueOrFalse.TRUE, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            saveJobLog(JobType.STATS_ALL, TrueOrFalse.FALSE,
                    JobType.STATS_ALL.getName() + ":" + longSdf.format(date) + e.getMessage());
        }
    }

    @Override
    public void statsRechargeByData(Date date)
    {
        try
        {
            clientRpcService.statsRechargeByDate(date);
            saveJobLog(JobType.STATS_RECHARGE, TrueOrFalse.TRUE, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            saveJobLog(JobType.STATS_RECHARGE, TrueOrFalse.FALSE,
                    JobType.STATS_RECHARGE.getName() + ":" + longSdf.format(date) + e.getMessage());
        }
    }

    @Override
    public void quartzClientRemind(Date date)
    {
        try
        {
            clientRpcService.quartzClientRemind(date);
            saveJobLog(JobType.CLIENT_REMIND, TrueOrFalse.TRUE, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            saveJobLog(JobType.CLIENT_REMIND, TrueOrFalse.FALSE,
                    JobType.CLIENT_REMIND.getName() + ":" + longSdf.format(date) + e.getMessage());
        }
    }

    @Override
    public void cleanTraffic(Date date)
    {
        try
        {
            trafficService.cleanTraffic(date);
            saveJobLog(JobType.CLEAN_TRAFFIC, TrueOrFalse.TRUE, null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            saveJobLog(JobType.CLEAN_TRAFFIC, TrueOrFalse.FALSE,
                    JobType.CLEAN_TRAFFIC.getName() + ":" + longSdf.format(date) + e.getMessage());
        }
    }

    private void saveJobLog(JobType jobType, Integer success, String remark)
    {
        try
        {
            JobLogReqDTO jobLog = new JobLogReqDTO();
            jobLog.setJobCode(jobType.getCode());
            jobLog.setSuccess(success);
            jobLog.setRemark(remark);
            systemRpcService.addJobLog(jobLog);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
