package com.mingdong.bop.controller;

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
        return view;
    }

    @RequestMapping(value = "recharge.html")
    public ModelAndView gotoRechargeListPage()
    {
        ModelAndView view = new ModelAndView("finance/recharge-list");
        return view;
    }
}
