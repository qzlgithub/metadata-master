package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ClientService;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class MonitoringController
{
    @Resource
    private ClientService clientService;

    @LoginRequired
    @GetMapping(value = "/monitoring/customer/allCustomer")
    public RestListResp allCustomer()
    {
        RestListResp res = new RestListResp();
        clientService.getAllClient(res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/monitoring/customer/request")
    public RestResp request(@RequestParam(value = Field.CLIENT_ID, required = false) String clientId)
    {
        RestResp res = new RestResp();
        List<Long> clientIdList = null;
        if(!StringUtils.isNullBlank(clientId))
        {
            List<String> strings = Arrays.asList(clientId.split(","));
            clientIdList = new ArrayList<>();
            for(String item : strings){
                clientIdList.add(Long.valueOf(item));
            }
        }
        clientService.getStatsClientRequestCache(clientIdList, res);
        return res;
    }
}
