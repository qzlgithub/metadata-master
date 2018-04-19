package com.mingdong.mis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.util.MapUtils;
import com.mingdong.core.exception.MetadataCoreException;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.service.ClientService;
import com.mingdong.mis.util.SignUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

@RestController
public class SecurityController
{
    @Resource
    private ClientService clientService;

    @GetMapping(value = "/security/access-token", headers = {"accept-version=1.0"})
    public MDResp getAccessToken(@RequestParam(value = Field.APP_ID) String appId,
            @RequestParam(value = Field.TIMESTAMP) String timestamp,
            @RequestParam(value = Field.ACCESS_KEY) String accessKey,
            @RequestParam(value = Field.USERNAME, required = false) String username,
            @RequestParam(value = Field.REFRESH, required = false) Integer refresh)
    {
        MDResp res = MDResp.create();
        clientService.getClientAccessToken(appId, timestamp, accessKey, username, refresh, res);
        return res;
    }

    @PostMapping(value = "/security/sign")
    public String sign(@RequestBody JSONObject jsonReq) throws MetadataCoreException
    {
        Map<String, Object> map = new HashMap<>();
        Set<String> strings = jsonReq.keySet();
        String key = "";
        for(String str : strings)
        {
            if("appKey".equals(str))
            {
                key = jsonReq.getString("appKey");
                continue;
            }
            map.put(str, jsonReq.get(str));
        }
        SortedMap sm = MapUtils.sortKey(map);
        String str = JSON.toJSONString(sm);
        System.out.println("str:" + str);
        return SignUtils.sign(str, key);
    }
}
