package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "role")
public class RoleController
{
    @Resource
    private ManagerService managerService;

    @GetMapping(value = "privilege")
    @ResponseBody
    public List<String> getRolePrivilege(@RequestParam(value = Field.ROLE_ID) Long roleId)
    {
        return managerService.getRolePrivilege(roleId);
    }

    @PostMapping(value = "addition")
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

    @PostMapping(value = "modification")
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

    @PostMapping(value = "changeStatus")
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
    @GetMapping(value = "list")
    @ResponseBody
    public ListRes getManagerRoleList(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        Page page = new Page(pageNum, pageSize);
        managerService.getRoleList(page, res);
        return res;
    }

    @GetMapping(value = "check")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(value = Field.NAME) String name)
    {
        BLResp resp = BLResp.build();
        managerService.checkIfRoleNameExist(name, resp);
        return resp.getDataMap();
    }
}
