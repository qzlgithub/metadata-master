package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.TrueOrFalse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
        view.addAllObjects(RequestThread.getMap());
        List<Map<String, Object>> productInfoList = productService.getProductInfoListMap(TrueOrFalse.TRUE);
        view.addObject(Field.PRODUCT_DICT, productInfoList);
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/finance/recharge.html")
    public ModelAndView gotoRechargeListPage()
    {
        ModelAndView view = new ModelAndView("finance/recharge");
        view.addAllObjects(RequestThread.getMap());
        List<Map<String, Object>> rechargeTypeList = systemService.getRechargeTypeList(TrueOrFalse.TRUE,
                TrueOrFalse.FALSE);
        List<Map<String, Object>> productInfoList = productService.getProductInfoListMap(TrueOrFalse.TRUE);
        List<Map<String, Object>> managerList = managerService.getManagerListMap(TrueOrFalse.TRUE);

        view.addObject(Field.RECHARGE_TYPE_LIST, rechargeTypeList);
        view.addObject(Field.PRODUCT_INFO_LIST, productInfoList);
        view.addObject(Field.MANAGER_LIST, managerList);
        view.addObject(Field.IS_MANAGER, RequestThread.isManager());
        return view;
    }
}
