package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class ClientPageController
{
    @Resource
    private SystemService systemService;
    @Resource
    private ClientService clientService;
    @Resource
    private ProductService productService;
    @Resource
    private ManagerService managerService;

    /**
     * 页面：客户列表
     */
    @LoginRequired
    @RequestMapping(value = "/client/index.html")
    public ModelAndView clientIndexPage()
    {
        ModelAndView view = new ModelAndView("client/list");
        List<Map<String, Object>> industryList = systemService.getIndustryList(0L, TrueOrFalse.TRUE);
        view.addObject(Field.INDUSTRY_LIST, industryList);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户添加页面
     */
    @LoginRequired
    @RequestMapping(value = "/client/add.html")
    public ModelAndView gotoClientAddition()
    {
        ModelAndView view = new ModelAndView("client/add");
        view.addAllObjects(systemService.getInitIndustryMap());
        view.addObject(Field.DEFAULT_PASSWORD, Constant.DEFAULT_PASSWORD);
        view.addObject(Field.MANAGER_ID, RequestThread.getOperatorId());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户编辑页面
     */
    @LoginRequired
    @RequestMapping(value = "/client/edit.html")
    public ModelAndView gotoClientEdit(@RequestParam(value = Field.ID) Long clientId)
    {
        RestResp resp = new RestResp();
        clientService.getClientInfoForEdit(clientId, resp);
        ModelAndView view = new ModelAndView("client/edit");
        List<Dict> adminUserDict = managerService.getAdminUserDict();
        view.addObject(Field.ADMIN_USER_DICT, adminUserDict);
        view.addAllObjects(resp.getData());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户详情页面
     */
    @LoginRequired
    @RequestMapping(value = "/client/detail.html")
    public ModelAndView gotoClientDetail(@RequestParam(value = Field.ID) Long clientId)
    {
        ModelAndView view = new ModelAndView("client/detail");
        view.addAllObjects(clientService.getClientDetailData(clientId));
        view.addObject(Field.RECHARGE_TYPE_DICT, systemService.getRechargeDict());
        view.addObject(Field.BILL_PLAN_DICT, BillPlan.getBillPlanDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 产品充值记录
     */
    @LoginRequired
    @RequestMapping(value = "/client/recharge.html")
    public ModelAndView gotoClientRecharge(@RequestParam(value = Field.C) Long clientId,
            @RequestParam(value = Field.P, required = false) Long productId)
    {
        ModelAndView view = new ModelAndView("client/recharge");
        view.addObject(Field.CLIENT_ID, clientId + "");
        view.addObject(Field.PRODUCT_ID, productId + "");
        // 查询客户企业名称
        view.addObject(Field.CORP_NAME, clientService.getClientCorpName(clientId));
        // 产品字典数据列表
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 接口请求记录
     */
    @LoginRequired
    @RequestMapping(value = "/client/consumption.html")
    public ModelAndView gotoClientRequest(@RequestParam(value = Field.C) Long clientId,
            @RequestParam(value = Field.P, required = false) Long productId,
            @RequestParam(value = Field.U, required = false) Long userId)
    {
        ModelAndView view = new ModelAndView("client/consumption");
        view.addObject(Field.CLIENT_ID, clientId + "");
        view.addObject(Field.PRODUCT_ID, productId + "");
        view.addObject(Field.USER_ID, userId + "");
        // 查询客户企业名称及账号字典列表，包括主账号和子账号
        view.addAllObjects(clientService.getClientAccountDict(clientId));
        // 产品字典数据列表
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }
}
