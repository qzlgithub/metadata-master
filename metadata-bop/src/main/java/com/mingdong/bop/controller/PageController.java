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
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ImageCode;
import com.mingdong.core.util.CaptchaUtils;
import com.mingdong.core.util.DateCalculateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        ModelAndView view = new ModelAndView("finance/bill-list");
        view.addAllObjects(RequestThread.getMap());
        List<Map<String, Object>> productInfoList = productService.getProductInfoListMap(TrueOrFalse.TRUE);

        view.addObject(Field.PRODUCT_INFO_LIST, productInfoList);
        Date date = new Date();
        Date beforeDayDate = DateCalculateUtils.getBeforeDayDate(date, 30, true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        view.addObject(Field.START_DATE, sdf.format(beforeDayDate));
        view.addObject(Field.END_DATE, sdf.format(date));
        return view;
    }

    @RequestMapping(value = "/finance/recharge.html")
    public ModelAndView gotoRechargeListPage()
    {
        ModelAndView view = new ModelAndView("finance/recharge-list");
        view.addAllObjects(RequestThread.getMap());
        List<Map<String, Object>> rechargeTypeList = systemService.getRechargeTypeList(TrueOrFalse.TRUE,
                TrueOrFalse.FALSE);
        List<Map<String, Object>> productInfoList = productService.getProductInfoListMap(TrueOrFalse.TRUE);
        List<Map<String, Object>> managerList = managerService.getManagerListMap(TrueOrFalse.TRUE);

        view.addObject(Field.RECHARGE_TYPE_LIST, rechargeTypeList);
        view.addObject(Field.PRODUCT_INFO_LIST, productInfoList);
        view.addObject(Field.MANAGER_LIST, managerList);
        Date date = new Date();
        Date beforeDayDate = DateCalculateUtils.getBeforeDayDate(date, 30, true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        view.addObject(Field.START_DATE, sdf.format(beforeDayDate));
        view.addObject(Field.END_DATE, sdf.format(date));
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
        ModelAndView view = new ModelAndView("customer-manage/customer-index");
        List<Map<String, Object>> industryList = systemService.getIndustryList(0L, TrueOrFalse.TRUE);
        view.addObject(Field.INDUSTRY_LIST, industryList);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户添加页面
     */
    @RequestMapping(value = "/client/addition.html")
    public ModelAndView gotoClientAddition()
    {
        ModelAndView view = new ModelAndView("customer-manage/customer-add");
        view.addAllObjects(systemService.getInitIndustryMap());
        view.addObject(Field.DEFAULT_PASSWORD, Constant.DEFAULT_PASSWORD);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户编辑页面
     */
    @RequestMapping(value = "/client/edit.html")
    public ModelAndView gotoClientEdit(@RequestParam(value = Field.CLIENT_ID) Long clientId)
    {
        BLResp resp = BLResp.build();
        clientService.getClientInfoForEdit(clientId, resp);
        //Map<String, Object> clientMap = clientService.findClientInfo(clientId);
        ModelAndView view = new ModelAndView("customer-manage/customer-edit");
        view.addAllObjects(resp.getDataMap());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 页面：客户详情页面
     */
    @RequestMapping(value = "/client/detail.html")
    public ModelAndView gotoClientDetail(@RequestParam(value = Field.CLIENT_ID) Long clientId)
    {
        BLResp resp = BLResp.build();
        //Map<String, Object> clientMap = clientService.findClientDetail(clientId);
        clientService.findClientDetail(clientId, resp);
        ModelAndView view = new ModelAndView("customer-manage/customer-detail");
        view.addAllObjects(resp.getDataMap());
        view.addObject(Field.RECHARGE_DICT, systemService.getRechargeDict());
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

    /**
     * 账户充值
     */
    @RequestMapping(value = "/client/account/recharge.html")
    public ModelAndView gotoAccountRecharge(@RequestParam(value = Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("customer-manage/account-recharge");
        view.addObject(Field.CLIENT_ID, id);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 账户消费
     */
    @RequestMapping(value = "/client/account/consume.html")
    public ModelAndView gotoAccountConsumption(@RequestParam(value = Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("customer-manage/account-consume");
        view.addObject(Field.CLIENT_ID, id);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/config/recharge.html")
    public ModelAndView configRecharge()
    {
        ModelAndView view = new ModelAndView("system-manage/recharge-manage");
        List<Map<String, Object>> list = systemService.getRechargeTypeList(null, TrueOrFalse.FALSE);
        view.addObject(Field.LIST, list);
        view.addObject(Field.TOTAL, list.size());
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
        ModelAndView view = new ModelAndView("system-manage/account-management");
        view.addObject(Field.ROLE_LIST, systemService.getValidRole());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/manager/addition.html")
    public ModelAndView gotoManagerAdditionPage()
    {
        ModelAndView view = new ModelAndView("system-manage/account-add");
        view.addObject(Field.ROLE_LIST, systemService.getValidRole());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/manager/edit.html")
    public ModelAndView gotoManagerEditPage(@RequestParam(value = Field.ID) Long managerId)
    {
        BLResp resp = BLResp.build();
        managerService.getManagerInfo(managerId, resp);
        ModelAndView view = new ModelAndView("system-manage/account-edit");
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
        ModelAndView view = new ModelAndView("product-manage/product-index");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/product/addition.html")
    public ModelAndView productAddition()
    {
        ModelAndView view = new ModelAndView("product-manage/product-add");
        view.addObject(Field.PRODUCT_TYPE_DICT, productService.getProductTypeDict(TrueOrFalse.TRUE));
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/product/edit.html")
    public ModelAndView productEdit(@RequestParam(Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("product-manage/product-edit");
        Map<String, Object> map = productService.getProductInfo(id);
        view.addAllObjects(map);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/product/category/index.html")
    public ModelAndView productCategoryIndex()
    {
        ModelAndView view = new ModelAndView("product-manage/product-category");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/role/index.html")
    public ModelAndView gotoRoleIndex()
    {
        ModelAndView view = new ModelAndView("system-manage/group-manage");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/role/addition.html")
    public ModelAndView gotoRoleAddition()
    {
        ModelAndView view = new ModelAndView("system-manage/group-add");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "/role/edit.html")
    public ModelAndView gotoRoleEdit(@RequestParam(value = Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("system-manage/group-edit");
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
