package com.mingdong.bop.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.RequestThread;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "role")
public class RoleController
{
    @Resource
    private ManagerService managerService;

    @RequestMapping(value = "index.html")
    public ModelAndView gotoRoleIndex()
    {
        ModelAndView view = new ModelAndView("system-manage/group-manage");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "addition.html")
    public ModelAndView gotoRoleAddition()
    {
        ModelAndView view = new ModelAndView("system-manage/group-add");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "edit.html")
    public ModelAndView gotoRoleEdit(@RequestParam(value = Field.ID) Long id)
    {
        ModelAndView view = new ModelAndView("system-manage/group-edit");
        view.addAllObjects(managerService.getRolePrivilegeDetail(id));
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "privilege", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getRolePrivilege(@RequestParam(value = Field.ROLE_ID) Long roleId)
    {
        return managerService.getRolePrivilege(roleId);
    }

    @RequestMapping(value = "addition", method = RequestMethod.POST)
    @ResponseBody
    public BLResp addRole(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        String name = jsonReq.getString(Field.NAME);
        JSONArray privilegeArray = jsonReq.getJSONArray(Field.PRIVILEGE);
        if(StringUtils.isNullBlank(name) || privilegeArray == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        List<Long> privilege = privilegeArray.toJavaList(Long.class);
        if(CollectionUtils.isEmpty(privilege))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.addRole(name, privilege, resp);
        return resp;
    }

    @RequestMapping(value = "modification", method = RequestMethod.POST)
    @ResponseBody
    public BLResp editRole(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long roleId = jsonReq.getLong(Field.ID);
        String name = jsonReq.getString(Field.NAME);
        JSONArray privilegeArray = jsonReq.getJSONArray(Field.PRIVILEGE);
        if(roleId == null || StringUtils.isNullBlank(name) || privilegeArray == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        List<Long> privilege = privilegeArray.toJavaList(Long.class);
        if(CollectionUtils.isEmpty(privilege))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.editRole(roleId, name, privilege, resp);
        return resp;
    }

    @RequestMapping(value = "changeStatus", method = RequestMethod.POST)
    @ResponseBody
    public BLResp changeStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long roleId = jsonReq.getLong(Field.ID);
        if(roleId == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.changeStatus(roleId, resp);
        return resp;
    }

    /**
     * 获取管理账号角色列表
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getManagerRoleList(
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        BLResp resp = BLResp.build();
        Page page = new Page(pageNum, pageSize);
        managerService.getRoleList(page, resp);
        return resp.getDataMap();
    }
}
