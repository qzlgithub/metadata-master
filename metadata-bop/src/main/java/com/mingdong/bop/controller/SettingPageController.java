package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.constant.ProdType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class SettingPageController
{
    @Resource
    private SystemService systemService;

    @GetMapping(value = "/setting/menu.html")
    public ModelAndView rechargeIndex()
    {
        ModelAndView view = new ModelAndView("setting/menu");
        view.addObject(Field.LIST, systemService.getHierarchyPrivilege());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/industry.html")
    public ModelAndView configIndustry()
    {
        ModelAndView view = new ModelAndView("setting/industry");
        view.addObject(Field.LIST, systemService.getHierarchyIndustry());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/recharge.html")
    public ModelAndView configRecharge()
    {
        ModelAndView view = new ModelAndView("setting/recharge");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/other.html")
    public ModelAndView otherSettingPage()
    {
        ModelAndView view = new ModelAndView("setting/other");
        view.addAllObjects(systemService.getSettings());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/product.html")
    public ModelAndView productIndex()
    {
        ModelAndView view = new ModelAndView("product/list");
        view.addObject(Field.PRODUCT_TYPE_DICT, ProdType.getProdTypeDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }
}
