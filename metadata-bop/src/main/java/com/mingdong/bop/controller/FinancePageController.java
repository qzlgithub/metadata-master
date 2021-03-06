package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

@Controller
public class FinancePageController
{
    @Resource
    private ManagerService managerService;
    @Resource
    private ProductService productService;
    @Resource
    private SystemService systemService;

    @LoginRequired
    @RequestMapping(value = "/finance/consumption.html")
    public ModelAndView gotoBillListPage()
    {
        ModelAndView view = new ModelAndView("finance/consumption");
        Calendar calendar = Calendar.getInstance();
        Date endDay = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDay = calendar.getTime();
        view.addObject(Field.START_DATE, DateUtils.format(startDay, DateFormat.YYYY_MM_DD_2));
        view.addObject(Field.END_DATE, DateUtils.format(endDay, DateFormat.YYYY_MM_DD_2));
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/finance/recharge.html")
    public ModelAndView gotoRechargeListPage()
    {
        ModelAndView view = new ModelAndView("finance/recharge");
        Calendar calendar = Calendar.getInstance();
        Date endDay = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDay = calendar.getTime();
        view.addObject(Field.START_DATE, DateUtils.format(startDay, DateFormat.YYYY_MM_DD_2));
        view.addObject(Field.END_DATE, DateUtils.format(endDay, DateFormat.YYYY_MM_DD_2));
        view.addObject(Field.RECHARGE_TYPE_DICT, systemService.getRechargeDict());
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addObject(Field.ADMIN_USER_DICT, managerService.getAdminUserDict());
        view.addObject(Field.IS_MANAGER, RequestThread.isManager());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }
}
