package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.BLResp;
import com.mingdong.bop.model.ManagerVO;
import com.mingdong.bop.model.NewManagerVO;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RequestThread;
import com.movek.model.Page;
import com.movek.util.CollectionUtils;
import com.movek.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping(value = "manager")
public class ManagerController
{
    @Resource
    private ManagerService managerService;
    @Resource
    private SystemService systemService;

    @RequestMapping(value = "index.html")
    public ModelAndView gotoManagerManagement()
    {
        ModelAndView view = new ModelAndView("system-manage/account-management");
        view.addObject(Field.ROLE_LIST, systemService.getValidRole());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /*@RequestMapping(value = "role/management")
    public ModelAndView gotoManagerRoleManagement()
    {
        ModelAndView view = new ModelAndView("system-manage/group-manage");
        return view;
    }

    @RequestMapping(value = "role/addition")
    public ModelAndView gotoManagerRoleAddition()
    {
        ModelAndView view = new ModelAndView("system-manage/group-add");
        return view;
    }*/

    @RequestMapping(value = "addition.html")
    public ModelAndView gotoManagerAdditionPage()
    {
        ModelAndView view = new ModelAndView("system-manage/account-add");
        view.addObject(Field.ROLE_LIST, systemService.getValidRole());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "edit.html")
    public ModelAndView gotoManagerEditPage(@RequestParam(value = Field.ID) Long managerId)
    {
        BLResp resp = BLResp.build();
        managerService.getManagerInfo(managerId, resp);
        ModelAndView view = new ModelAndView("system-manage/account-edit");
        view.addAllObjects(resp.getDataMap());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    /**
     * 添加新账号
     */
    @RequestMapping(value = "addition", method = RequestMethod.POST)
    @ResponseBody
    public BLResp addNewManager(@RequestBody NewManagerVO vo)
    {
        BLResp resp = BLResp.build();
        // 校验各个字段值
        if(StringUtils.isNullBlank(vo.getUsername()) || StringUtils.isNullBlank(vo.getPassword()) ||
                StringUtils.isNullBlank(vo.getName()) || StringUtils.isNullBlank(vo.getPhone()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(vo.getRoleId() == null || vo.getRoleId() <= 0)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(!TrueOrFalse.TRUE.equals(vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(CollectionUtils.isEmpty(vo.getPrivilege()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        // 保存管理账号
        managerService.addManager(vo.getUsername(), vo.getPassword(), vo.getName(), vo.getPhone(), vo.getRoleId(),
                vo.getEnabled(), vo.getPrivilege(), resp);
        return resp;
    }

    /**
     * 获取管理账号列表
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getManagerList(@RequestParam(value = Field.ROLE_ID, required = false) Long roleId,
            @RequestParam(value = Field.ENABLED, required = false) Integer enabled,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        // 校验各个字段值
        if(roleId != null && roleId <= 0)
        {
            roleId = null;
        }
        if(!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled))
        {
            enabled = null;
        }
        Page page = new Page(pageNum, pageSize);
        managerService.getUserList(roleId, enabled, page, resp);
        return resp.getDataMap();
    }

    /**
     * 编辑账号信息
     */
    @RequestMapping(value = "modification", method = RequestMethod.POST)
    @ResponseBody
    public BLResp editManager(@RequestBody ManagerVO vo)
    {
        BLResp resp = BLResp.build();
        if(vo.getRoleId() == null || vo.getRoleId() <= 0)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(!TrueOrFalse.TRUE.equals(vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        if(CollectionUtils.isEmpty(vo.getPrivilege()))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.editManager(vo.getManagerId(), vo.getRoleId(), vo.getName(), vo.getPhone(), vo.getEnabled(),
                vo.getPrivilege(), resp);
        return resp;
    }

    @RequestMapping(value = "changeStatus", method = RequestMethod.POST)
    @ResponseBody
    public BLResp changeStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long managerId = jsonReq.getLong(Field.ID);
        if(managerId == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.changeManagerStatus(managerId, resp);
        return resp;
    }
}
