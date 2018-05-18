package com.mingdong.bop.controller;

import com.mingdong.bop.service.ClientService;
import com.mingdong.common.model.Page;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HomeController
{
    @Resource
    private ClientService clientService;

    @LoginRequired
    @GetMapping(value = "/home/list/date")
    public RestListResp getListDate()
    {
        RestListResp resp = new RestListResp();
        clientService.getClientRemindForDate(null, null, null, new Page(1, 5), resp);
        return resp;
    }

    @LoginRequired
    @GetMapping(value = "/home/list/time")
    public RestListResp getListTime()
    {
        RestListResp resp = new RestListResp();
        clientService.getClientRemindForTimes(null, null, null, new Page(1, 5), resp);
        return resp;
    }

}
