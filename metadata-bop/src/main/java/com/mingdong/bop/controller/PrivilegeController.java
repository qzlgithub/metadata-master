package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "privilege")
public class PrivilegeController
{
    @Resource
    private SystemService systemService;

    @RequestMapping(value = "index.html")
    public ModelAndView configRecharge()
    {
        ModelAndView view = new ModelAndView("system-manage/column-manage");
        view.addObject(Field.LIST, systemService.getHierarchyPrivilege());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

}
