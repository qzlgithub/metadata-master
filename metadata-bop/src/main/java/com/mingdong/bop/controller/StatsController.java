package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.bop.manage.StatisticsManage;
import com.mingdong.bop.service.StatsService;
import com.mingdong.common.model.Page;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class StatsController
{
    @Resource
    private StatsService statsService;
    @Resource
    private StatisticsManage statisticsManage;

    /**
     * TODO 首页第三方接口请求统计图
     */
    @LoginRequired
    @GetMapping(value = "/stats/request/third")
    public RestResp getThirdRequestStats()
    {
        RestResp resp = new RestResp();
        statisticsManage.getThirdRequestDiagramIn24h(resp);
        return resp;
    }

    /**
     * TODO 首页接口请求统计图
     */
    @LoginRequired
    @GetMapping(value = "/stats/request/product")
    public RestResp getProductRequestStats()
    {
        RestResp resp = new RestResp();
        String s =
                "[{\"productType\":\"互联网金融\",\"product\":[{\"request\":\"3202340\",\"ratio\":\"10\",\"client\":\"12\",\"name\":\"小微贷\"},{\"request\":\"237000\",\"ratio\":\"18\",\"client\":\"18\",\"name\":\"小张贷\"},{\"request\":\"434300\",\"ratio\":\"12\",\"client\":\"102\",\"name\":\"小黄贷\"}]},{\"productType\":\"风控名单\",\"product\":[{\"request\":\"4347388\",\"ratio\":\"11\",\"client\":\"500\",\"name\":\"黑名单\"},{\"request\":\"989329\",\"ratio\":\"15\",\"client\":\"76\",\"name\":\"白名单\"},{\"request\":\"347438\",\"ratio\":\"15\",\"client\":\"88\",\"name\":\"红名单\"},{\"request\":\"1423423\",\"ratio\":\"19\",\"client\":\"301\",\"name\":\"绿名单\"}]}]";
        JSONArray arr = JSON.parseArray(s);
        resp.addData(Field.DATA, arr);
        return resp;
    }

    @LoginRequired
    @GetMapping(value = "/stats/client/list")
    public RestListResp getClientList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp resp = new RestListResp();
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        statsService.getClientList(scopeTypeEnum, new Page(pageNum, pageSize), resp);
        return resp;
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
    public RestListResp getRechargeList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
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
    public RestListResp getRequestList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.NAME, required = false) String name,
            @RequestParam(value = Field.PRODUCT_ID, required = false) Long productId,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
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

    /**
     * TODO TBD：营收数据列表
     */
    @LoginRequired
    @GetMapping(value = "/stats/client/revenueList")
    public RestListResp getRevenueList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        statsService.getRevenueList(scopeTypeEnum, new Page(pageNum, pageSize), res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/stats/client/revenueListJson")
    public String getRevenueListJson(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType)
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        JSONArray jsonArray = statsService.getRevenueListJson(scopeTypeEnum);
        return jsonArray.toJSONString();
    }
}
