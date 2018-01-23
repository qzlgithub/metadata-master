package com.mingdong.bop.controller;

import com.mingdong.bop.model.RequestThread;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "finance")
public class PageController
{
    @RequestMapping(value = "bill.html")
    public ModelAndView gotoBillListPage()
    {
        ModelAndView view = new ModelAndView("finance/bill-list");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "recharge.html")
    public ModelAndView gotoRechargeListPage()
    {
        ModelAndView view = new ModelAndView("finance/recharge-list");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }
}
