package com.mingdong.bop.controller;

import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.StatsService;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
        BLResp resp = BLResp.build();
        statsService.getIndexStats(resp);
        view.addAllObjects(resp.getDataMap());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "customer.html")
    public ModelAndView gotoStatsCustomer()
    {
        ModelAndView view = new ModelAndView("data-analysis/customer-data");
        view.addAllObjects(RequestThread.getMap());
        return view;
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
