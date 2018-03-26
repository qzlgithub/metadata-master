package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class WarningPageController
{
    @Resource
    private SystemService systemService;

    @LoginRequired
    @RequestMapping(value = "/warning/setting/list.html")
    public ModelAndView list()
    {
        ModelAndView view = new ModelAndView("warning/list");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @GetMapping(value = "/warning/setting/edit.html")
    public ModelAndView edit(@RequestParam(Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("articles/edit");
        view.addAllObjects(RequestThread.getMap());
        view.addAllObjects(systemService.getWarningSetting(id));
        return view;
    }
}
