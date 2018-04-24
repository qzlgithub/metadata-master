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
    @RequestMapping(value = "/stats/request/product.html")
    public ModelAndView statsRequestProduct()
    {
        ModelAndView view = new ModelAndView("monitor/analysis/request-product");
        Map<String, Object> summaryMap = statisticsManage.getRequestProductStatisticsIndex();
        view.addAllObjects(summaryMap);
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

}
