package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class ProcessPageController
{
    @Resource
    private SystemService systemService;

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
        List<Map<String, Object>> list = systemService.getWarningDisposeManagerList();
        view.addObject(Field.LIST, list);
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/alarm/wait/detail.html")
    public ModelAndView waitDetail(@RequestParam(value = Field.ID, required = false) Long id)
    {
        ModelAndView view = new ModelAndView("monitor/alarm/detail");
        view.addAllObjects(RequestThread.getMap());
        view.addAllObjects(systemService.getWarningManageById(id));
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/alarm/already/detail.html")
    public ModelAndView alreadyDetail(@RequestParam(value = Field.ID, required = false) Long id)
    {
        ModelAndView view = new ModelAndView("monitor/alarm/detail");
        view.addAllObjects(RequestThread.getMap());
        view.addAllObjects(systemService.getWarningManageById(id));
        return view;
    }
}
