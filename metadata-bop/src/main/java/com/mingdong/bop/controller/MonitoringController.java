package com.mingdong.bop.controller;

import com.mingdong.bop.service.ClientService;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MonitoringController
{
    @Resource
    private ClientService clientService;

    @LoginRequired
    @GetMapping(value = "/monitoring/customer/allCustomer")
    public RestListResp getRechargeTypeList()
    {
        RestListResp res = new RestListResp();
        clientService.getAllClient(res);
        return res;
    }
}
