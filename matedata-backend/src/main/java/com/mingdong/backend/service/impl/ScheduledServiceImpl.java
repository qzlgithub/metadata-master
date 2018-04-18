package com.mingdong.backend.service.impl;

import com.mingdong.backend.service.ScheduledService;
import com.mingdong.core.service.ClientRpcService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ScheduledServiceImpl implements ScheduledService
{
    @Resource
    private ClientRpcService clientRpcService;

    @Override
    public void statsByData(Date date)
    {
        clientRpcService.statsByDate(date);
    }

    @Override
    public void statsRechargeByData(Date date)
    {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date parse = new Date();
//        try
//        {
//            parse = sdf.parse("2018-01-01");
//        }
//        catch(ParseException e)
//        {
//            e.printStackTrace();
//        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(parse);
//        while(calendar.getTime().before(date)){
//            clientRpcService.statsRechargeByDate(calendar.getTime());
//            calendar.add(Calendar.DAY_OF_MONTH,1);
//        }

        clientRpcService.statsRechargeByDate(date);
    }

    @Override
    public void quartzClientRemind(Date date)
    {
        clientRpcService.quartzClientRemind(date);
    }

    @Override
    public void statsRequest(Date date)
    {
        clientRpcService.statsRequestByDate(date);
    }

}
