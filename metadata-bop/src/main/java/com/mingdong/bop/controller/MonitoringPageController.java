package com.mingdong.bop.controller;

import com.mingdong.bop.model.RequestThread;
import com.mingdong.core.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MonitoringPageController
{

    @LoginRequired
    @RequestMapping(value = "/monitoring/product.html")
    public ModelAndView product()
    {
        ModelAndView view = new ModelAndView("monitor/monitoring/product");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/monitoring/client.html")
    public ModelAndView customer()
    {
        ModelAndView view = new ModelAndView("monitor/monitoring/client");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/monitoring/third.html")
    public ModelAndView other()
    {
        ModelAndView view = new ModelAndView("monitor/monitoring/third");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }
}

