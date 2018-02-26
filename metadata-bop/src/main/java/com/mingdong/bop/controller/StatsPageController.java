package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.service.StatsService;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class StatsPageController
{
    @Resource
    private ProductService productService;
    @Resource
    private StatsService statsService;

    @RequestMapping(value = "/stats/index.html")
    public ModelAndView gotoStatsIndex()
    {
        ModelAndView view = new ModelAndView("stats/index");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "/stats/customer.html")
    public ModelAndView gotoStatsCustomer()
    {
        ModelAndView view = new ModelAndView("stats/client");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getClientIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "/stats/revenue.html")
    public ModelAndView gotoStatsRevenue()
    {
        ModelAndView view = new ModelAndView("stats/revenue");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/stats/request.html")
    public ModelAndView gotoStatsRequest()
    {
        ModelAndView view = new ModelAndView("stats/request");
        List<Map<String, Object>> productInfoList = productService.getProductInfoListMap(TrueOrFalse.TRUE);
        view.addObject(Field.PRODUCT_INFO_LIST, productInfoList);
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getRequestIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "/stats/recharge.html")
    public ModelAndView gotoStatsRecharge()
    {
        ModelAndView view = new ModelAndView("stats/recharge");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getRechargeIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }
}
