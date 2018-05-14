package com.mingdong.backend.controller;

import com.mingdong.backend.service.ScheduledService;
import com.mingdong.common.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class TrafficController
{
    private static String KEY = "yILpIoVB5AEzB8MW";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    @Resource
    private ScheduledService scheduledService;

    @GetMapping(value = "/job/statsByData")
    public String statsByData(String dateStr, String key)
    {
        if(!KEY.equals(key) || StringUtils.isNullBlank(dateStr))
        {
            return "null";
        }
        Date parse;
        try
        {
            parse = sdf.parse(dateStr);

        }
        catch(Exception e)
        {
            return "false";
        }
        scheduledService.statsByData(parse);
        return "true";
    }

    @GetMapping(value = "/job/statsRechargeByData")
    public String statsRechargeByData(String dateStr, String key)
    {
        if(!KEY.equals(key) || StringUtils.isNullBlank(dateStr))
        {
            return "null";
        }
        Date parse;
        try
        {
            parse = sdf.parse(dateStr);

        }
        catch(Exception e)
        {
            return "false";
        }
        scheduledService.statsRechargeByData(parse);
        return "true";
    }

    @GetMapping(value = "/job/quartzClientRemind")
    public String quartzClientRemind(String dateStr, String key)
    {
        if(!KEY.equals(key) || StringUtils.isNullBlank(dateStr))
        {
            return "null";
        }
        Date parse;
        try
        {
            parse = sdf.parse(dateStr);

        }
        catch(Exception e)
        {
            return "false";
        }
        scheduledService.quartzClientRemind(parse);
        return "true";
    }

    @GetMapping(value = "/job/statsRequest")
    public String statsRequest(String dateStr, String key)
    {
        if(!KEY.equals(key) || StringUtils.isNullBlank(dateStr))
        {
            return "null";
        }
        Date parse;
        try
        {
            parse = sdf.parse(dateStr);

        }
        catch(Exception e)
        {
            return "false";
        }
        scheduledService.statsRequest(parse);
        return "true";
    }

}
