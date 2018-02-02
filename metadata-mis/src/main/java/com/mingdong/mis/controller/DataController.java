package com.mingdong.mis.controller;

import com.mingdong.core.model.BLResp;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.service.DataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DataController
{
    @Resource
    private DataService dataService;

    @RequestMapping(value = "gateway", headers = {"accept-version=1.0"})
    public BLResp getProductionData(@RequestParam(value = Field.PHONE) String phone)
    {
        BLResp resp = BLResp.build();
        dataService.getProductionData(RequestThread.getIp(), RequestThread.getProductId(), RequestThread.getClientId(),
                RequestThread.getUserId(), phone, resp);
        return resp;
    }

    @PostMapping(value = "dsdata/test", produces = "text/plain;charset=UTF-8")
    public String getDSReport(@RequestBody String str)
    {
        System.out.println(">>>: " + str);
        return "Hello, " + str;
    }
}
