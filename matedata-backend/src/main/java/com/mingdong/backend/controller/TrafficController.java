package com.mingdong.backend.controller;

import com.mingdong.backend.component.RedisDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TrafficController
{
    @Resource
    private RedisDao redisDao;

    @GetMapping(value = "/traffic")
    public String clientRequest()
    {
        redisDao.realTimeTraffic(System.currentTimeMillis() / 1000, 1999L, 2999L);
        return "ok";
    }
}
