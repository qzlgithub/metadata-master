package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.StatsService;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "stats")
public class StatsController
{
    @Resource
    private StatsService statsService;

    @RequestMapping(value = "index.html")
    public ModelAndView gotoStatsIndex()
    {
        ModelAndView view = new ModelAndView("data-analysis/data-index");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "customer.html")
    public ModelAndView gotoStatsCustomer()
    {
        ModelAndView view = new ModelAndView("data-analysis/customer-data");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getClientIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "/client/clientList", method = RequestMethod.GET)
    @ResponseBody
    public BLResp getClentList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        BLResp resp = statsService.getClientList(scopeTypeEnum, new Page(pageNum, pageSize));
        return resp;
    }

    @RequestMapping(value = "recharge.html")
    public ModelAndView gotoStatsRecharge()
    {
        ModelAndView view = new ModelAndView("data-analysis/recharge-data");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "revenue.html")
    public ModelAndView gotoStatsRevenue()
    {
        ModelAndView view = new ModelAndView("data-analysis/revenue-data");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "request.html")
    public ModelAndView gotoStatsRequest()
    {
        ModelAndView view = new ModelAndView("data-analysis/product-data-request");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

}
