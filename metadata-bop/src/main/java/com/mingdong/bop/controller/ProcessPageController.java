package com.mingdong.bop.controller;

import com.mingdong.bop.model.RequestThread;
import com.mingdong.core.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProcessPageController
{
    @LoginRequired
    @RequestMapping(value = "/alarm/wait.html")
    public ModelAndView alarmWait()
    {
        ModelAndView view = new ModelAndView("monitor/alarm/wait");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/alarm/already.html")
    public ModelAndView alarmAlready()
    {
        ModelAndView view = new ModelAndView("monitor/alarm/already");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/alarm/detail.html")
    public ModelAndView detail()
    {
        ModelAndView view = new ModelAndView("monitor/alarm/detail");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }
}
