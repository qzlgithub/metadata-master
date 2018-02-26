package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.constant.ProdType;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

@Controller
public class SettingPageController
{
    @Resource
    private SystemService systemService;
    @Resource
    private ManagerService managerService;
    @Resource
    private ProductService productService;

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

    @GetMapping(value = "/setting/role.html")
    public ModelAndView gotoRoleIndex()
    {
        ModelAndView view = new ModelAndView("role/list");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/role/add.html")
    public ModelAndView gotoRoleAddition()
    {
        ModelAndView view = new ModelAndView("role/add");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/role/edit.html")
    public ModelAndView gotoRoleEdit(@RequestParam(value = Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("role/edit");
        view.addAllObjects(managerService.getRolePrivilegeDetail(id));
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/user.html")
    public ModelAndView gotoManagerManagement()
    {
        ModelAndView view = new ModelAndView("user/list");
        view.addObject(Field.ROLE_LIST, systemService.getValidRole());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/user/add.html")
    public ModelAndView gotoManagerAdditionPage()
    {
        ModelAndView view = new ModelAndView("user/add");
        view.addObject(Field.ROLE_LIST, systemService.getValidRole());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/user/edit.html")
    public ModelAndView gotoManagerEditPage(@RequestParam(value = Field.ID) Long managerId)
    {
        BLResp resp = BLResp.build();
        managerService.getManagerInfo(managerId, resp);
        ModelAndView view = new ModelAndView("user/edit");
        view.addAllObjects(resp.getDataMap());
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

    @GetMapping(value = "/setting/product/edit.html")
    public ModelAndView productEdit(@RequestParam(Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("product/edit");
        Map<String, Object> map = productService.getProductInfo(id);
        view.addAllObjects(map);
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "/setting/product/category.html")
    public ModelAndView productCategoryIndex()
    {
        ModelAndView view = new ModelAndView("product/category");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }
}
