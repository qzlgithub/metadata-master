package com.mingdong.bop.controller;

import com.mingdong.bop.component.RedisDao;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ManagerSession;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.service.StatsService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.ProdType;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ImageCode;
import com.mingdong.core.util.CaptchaUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class PageController
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private ManagerService managerService;
    @Resource
    private ClientService clientService;
    @Resource
    private ProductService productService;
    @Resource
    private SystemService systemService;
    @Resource
    private StatsService statsService;

    @RequestMapping(value = {"/", "index.html", "login.html"})
    public ModelAndView index(HttpServletRequest request) throws IOException
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        if(!StringUtils.isNullBlank(sessionId))
        {
            ManagerSession ms = redisDao.getManagerSession(sessionId);
            if(ms != null)
            {
                return new ModelAndView("redirect:/stats/index.html");
            }
        }
        ImageCode imageCode = CaptchaUtils.buildImageCode(); // 获取图片验证码
        session.setAttribute(Field.IMAGE_CAPTCHA, imageCode.getCode());
        ModelAndView view = new ModelAndView("login");
        view.addObject(Field.IMAGE, "data:image/png;base64," + imageCode.getBase64Code());
        return view;
    }

    @RequestMapping(value = "/finance/bill.html")
    public ModelAndView gotoBillListPage()
    {
        ModelAndView view = new ModelAndView("finance/bill");
        view.addAllObjects(RequestThread.getMap());
        List<Map<String, Object>> productInfoList = productService.getProductInfoListMap(TrueOrFalse.TRUE);
        view.addObject(Field.PRODUCT_DICT, productInfoList);
        return view;
    }

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
        return view;
    }

    @RequestMapping(value = "logout")
    public ModelAndView managerLogout(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        managerService.userLogout(sessionId);
        return new ModelAndView("redirect:/");
    }

    /**
     * 页面：客户列表
     */
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
     * 页面：客户帐号消费列表
     */
    @RequestMapping(value = "/client/user/consume.html")
    public ModelAndView clientUserConsumeIndexPage(@RequestParam(value = Field.USER_ID) Long userId)
    {
        ModelAndView view = new ModelAndView("customer-manage/product-user-consume");
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addObject(Field.USER_ID, userId);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户添加页面
     */
    @RequestMapping(value = "/client/addition.html")
    public ModelAndView gotoClientAddition()
    {
        ModelAndView view = new ModelAndView("client/add");
        view.addAllObjects(systemService.getInitIndustryMap());
        view.addObject(Field.DEFAULT_PASSWORD, Constant.DEFAULT_PASSWORD);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户编辑页面
     */
    @RequestMapping(value = "/client/edit.html")
    public ModelAndView gotoClientEdit(@RequestParam(value = Field.ID) Long clientId)
    {
        BLResp resp = BLResp.build();
        clientService.getClientInfoForEdit(clientId, resp);
        ModelAndView view = new ModelAndView("client/edit");
        view.addAllObjects(resp.getDataMap());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户详情页面
     */
    @RequestMapping(value = "/client/detail.html")
    public ModelAndView gotoClientDetail(@RequestParam(value = Field.ID) Long clientId)
    {
        BLResp resp = BLResp.build();
        clientService.findClientDetail(clientId, resp);
        ModelAndView view = new ModelAndView("client/detail");
        view.addAllObjects(resp.getDataMap());
        view.addObject(Field.RECHARGE_DICT, systemService.getRechargeDict());
        view.addObject(Field.BILL_PLAN_LIST, BillPlan.getAllList());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 充值记录
     */
    @RequestMapping(value = "/client/product/recharge.html")
    public ModelAndView gotoRechargeRecord(@RequestParam(value = Field.ID) Long clientProductId)
    {
        ModelAndView view = new ModelAndView("customer-manage/product-recharge");
        view.addAllObjects(clientService.getClientProductInfo(clientProductId));
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 消费明细
     */
    @RequestMapping(value = "/client/product/consume.html")
    public ModelAndView gotoConsumptionDetail(@RequestParam(value = Field.ID) Long clientProductId)
    {
        ModelAndView view = new ModelAndView("customer-manage/product-consume");
        view.addAllObjects(clientService.getClientProductInfo(clientProductId));
        view.addObject(Field.PRODUCT_DICT, productService.getProductDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/config/recharge.html")
    public ModelAndView configRecharge()
    {
        ModelAndView view = new ModelAndView("dict/recharge");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/config/industry.html")
    public ModelAndView configIndustry()
    {
        ModelAndView view = new ModelAndView("system-manage/industry-manage");
        view.addObject(Field.LIST, systemService.getHierarchyIndustry());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/config/setting.html")
    public ModelAndView otherSettingPage()
    {
        ModelAndView view = new ModelAndView("system-manage/other-setting");
        view.addAllObjects(systemService.getSettings());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/manager/index.html")
    public ModelAndView gotoManagerManagement()
    {
        ModelAndView view = new ModelAndView("account/list");
        view.addObject(Field.ROLE_LIST, systemService.getValidRole());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/manager/addition.html")
    public ModelAndView gotoManagerAdditionPage()
    {
        ModelAndView view = new ModelAndView("account/add");
        view.addObject(Field.ROLE_LIST, systemService.getValidRole());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/manager/edit.html")
    public ModelAndView gotoManagerEditPage(@RequestParam(value = Field.ID) Long managerId)
    {
        BLResp resp = BLResp.build();
        managerService.getManagerInfo(managerId, resp);
        ModelAndView view = new ModelAndView("account/edit");
        view.addAllObjects(resp.getDataMap());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/privilege/index.html")
    public ModelAndView rechargeIndex()
    {
        ModelAndView view = new ModelAndView("system-manage/column-manage");
        view.addObject(Field.LIST, systemService.getHierarchyPrivilege());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/product/index.html")
    public ModelAndView productIndex()
    {
        ModelAndView view = new ModelAndView("product/list");
        view.addObject(Field.PRODUCT_TYPE_DICT, ProdType.getProdTypeDict());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/product/edit.html")
    public ModelAndView productEdit(@RequestParam(Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("product/edit");
        Map<String, Object> map = productService.getProductInfo(id);
        view.addAllObjects(map);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/product/category/index.html")
    public ModelAndView productCategoryIndex()
    {
        ModelAndView view = new ModelAndView("product/category");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/role/index.html")
    public ModelAndView gotoRoleIndex()
    {
        ModelAndView view = new ModelAndView("role/list");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/role/addition.html")
    public ModelAndView gotoRoleAddition()
    {
        ModelAndView view = new ModelAndView("role/add");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/role/edit.html")
    public ModelAndView gotoRoleEdit(@RequestParam(value = Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("role/edit");
        view.addAllObjects(managerService.getRolePrivilegeDetail(id));
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/stats/index.html")
    public ModelAndView gotoStatsIndex()
    {
        ModelAndView view = new ModelAndView("data-analysis/data-index");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "/stats/customer.html")
    public ModelAndView gotoStatsCustomer()
    {
        ModelAndView view = new ModelAndView("data-analysis/customer-data");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getClientIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "/stats/revenue.html")
    public ModelAndView gotoStatsRevenue()
    {
        ModelAndView view = new ModelAndView("data-analysis/revenue-data");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/stats/request.html")
    public ModelAndView gotoStatsRequest()
    {
        ModelAndView view = new ModelAndView("data-analysis/product-data-request");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/stats/recharge.html")
    public ModelAndView gotoStatsRecharge()
    {
        ModelAndView view = new ModelAndView("data-analysis/recharge-data");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getRechargeIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

}
