package com.mingdong.bop.controller;

import com.mingdong.bop.manage.StatisticsManage;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.core.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class StatsPageController
{
    @Resource
    private StatisticsManage statisticsManage;

    @LoginRequired
    @RequestMapping(value = "/stats/client.html")
    public ModelAndView statsClient()
    {
        ModelAndView view = new ModelAndView("monitor/analysis/client");
        Map<String, Object> summaryMap = statisticsManage.getClientStatisticsIndex();
        view.addAllObjects(summaryMap);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats/recharge.html")
    public ModelAndView statsRecharge()
    {
        ModelAndView view = new ModelAndView("monitor/analysis/recharge");
        Map<String, Object> summaryMap = statisticsManage.getRechargeStatisticsIndex();
        view.addAllObjects(summaryMap);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats/request/client.html")
    public ModelAndView statsRequestClient()
    {
        ModelAndView view = new ModelAndView("monitor/analysis/request-client");

        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats/request/product.html")
    public ModelAndView statsRequestProduct()
    {
        ModelAndView view = new ModelAndView("monitor/analysis/request-product");

        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats/request/third.html")
    public ModelAndView statsRequestThird()
    {
        ModelAndView view = new ModelAndView("monitor/analysis/request-third");

        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats/revenue.html")
    public ModelAndView statsRevenue()
    {
        ModelAndView view = new ModelAndView("monitor/analysis/revenue");

        view.addAllObjects(RequestThread.getMap());
        return view;
    }









    /*@LoginRequired
    @RequestMapping(value = "/stats/index.html")
    public ModelAndView gotoStatsIndex()
    {
        ModelAndView view = new ModelAndView("stats/index");
        view.addAllObjects(RequestThread.getMap());
        RestResp resp = statsService.getIndexStats();
        view.addAllObjects(resp.getData());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats/client.html")
    public ModelAndView gotoStatsCustomer()
    {
        ModelAndView view = new ModelAndView("stats/client");
        RestResp resp = statsService.getClientQuantityStats();
        view.addAllObjects(resp.getData());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats/revenue.html")
    public ModelAndView gotoStatsRevenue()
    {
        ModelAndView view = new ModelAndView("stats/revenue");
        view.addAllObjects(RequestThread.getMap());
        RestResp resp = statsService.getRevenueIndexStats();
        view.addAllObjects(resp.getData());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats/consumption.html")
    public ModelAndView gotoStatsRequest()
    {
        ModelAndView view = new ModelAndView("stats/consumption");
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addAllObjects(RequestThread.getMap());
        RestResp resp = statsService.getRequestIndexStats();
        view.addAllObjects(resp.getData());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats/recharge.html")
    public ModelAndView gotoStatsRecharge()
    {
        ModelAndView view = new ModelAndView("stats/recharge");
        view.addAllObjects(RequestThread.getMap());
        RestResp resp = statsService.getRechargeIndexStats();
        view.addAllObjects(resp.getData());
        return view;
    }*/
}
