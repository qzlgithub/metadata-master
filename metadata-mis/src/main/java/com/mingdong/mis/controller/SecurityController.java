package com.mingdong.mis.controller;

import com.mingdong.mis.constant.Field;
import com.mingdong.mis.model.MetadataRes;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "security")
public class SecurityController
{
    @Resource
    private ClientService clientService;

    @GetMapping(value = "access-token", headers = {"accept-version=1.0"})
    public MetadataRes getAccessToken(@RequestParam(value = Field.APP_ID) String appId,
            @RequestParam(value = Field.TIMESTAMP) String timestamp,
            @RequestParam(value = Field.ACCESS_KEY) String accessKey,
            @RequestParam(value = Field.USERNAME, required = false) String username,
            @RequestParam(value = Field.REFRESH, required = false) Integer refresh)
    {
        MetadataRes res = RequestThread.getResult();
        clientService.getClientAccessToken(appId, timestamp, accessKey, username, refresh, res);
        return res;
    }
}
