package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.bop.service.StatsService;
import com.mingdong.common.model.Page;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.ListRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class StatsController
{
    @Resource
    private StatsService statsService;

    @LoginRequired
    @GetMapping(value = "/stats/client/clientList")
    public ListRes getClientList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        statsService.getClientList(scopeTypeEnum, new Page(pageNum, pageSize), res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/stats/client/clientListJson")
    public String getClientListJson(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType)
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        JSONArray jsonArray = statsService.getClientListJson(scopeTypeEnum);
        return jsonArray.toJSONString();
    }

    @LoginRequired
    @GetMapping(value = "/stats/client/rechargeList")
    public ListRes getRechargeList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        statsService.getRechargeList(scopeTypeEnum, new Page(pageNum, pageSize), res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/stats/client/rechargeListJson")
    public String getRechargeListJson(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType)
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        JSONObject jsonObject = statsService.getRechargeListJson(scopeTypeEnum);
        return jsonObject.toJSONString();
    }

    @LoginRequired
    @GetMapping(value = "/stats/client/requestList")
    public ListRes getRequestList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.NAME, required = false) String name,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        statsService.getRequestList(scopeTypeEnum, new Page(pageNum, pageSize), name, productId, res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/stats/client/requestListJson")
    public String getRequestListJson(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.NAME, required = false) String name,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId)
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        JSONArray jsonArray = statsService.getRequestListJson(scopeTypeEnum, name, productId);
        return jsonArray.toJSONString();
    }

}
