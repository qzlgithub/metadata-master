package com.mingdong.bop.controller;

import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class HomePageController
{
    @Resource
    private SystemService systemService;

    @LoginRequired
    @RequestMapping(value = "/enter.html")
    public ModelAndView enterPage()
    {
        if(RequestThread.isSalesman())
        {
            //业务员
            return new ModelAndView("redirect:/home.html");
        }
        else if(RequestThread.isOperation())
        {
            //运营
            return new ModelAndView("redirect:/stats.html");
        }
        //管理员
        ModelAndView view = new ModelAndView("enter");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/home.html")
    public ModelAndView homePage()
    {
        ModelAndView view = new ModelAndView("home");
        view.addAllObjects(RequestThread.getMap());
        view.addAllObjects(systemService.getHomeData());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/stats.html")
    public ModelAndView statsPage()
    {
        ModelAndView view = new ModelAndView("monitor/index");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

}
