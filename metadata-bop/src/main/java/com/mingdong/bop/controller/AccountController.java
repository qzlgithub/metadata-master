package com.mingdong.bop.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ManagerVO;
import com.mingdong.bop.model.NewManagerVO;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.ListRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class AccountController
{
    @Resource
    private ManagerService managerService;

    /**
     * 账号角色 - 权限列表
     */
    @LoginRequired
    @GetMapping(value = "/account/role/privilege")
    public List<String> getRolePrivilege(@RequestParam(value = Field.ROLE_ID) Long roleId)
    {
        return managerService.getRolePrivilege(roleId);
    }

    /**
     * 账号角色 - 新增
     */
    @LoginRequired
    @PostMapping(value = "/account/role/addition")
    public RestResp addRole(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        String name = jsonReq.getString(Field.NAME);
        JSONArray privilegeArray = jsonReq.getJSONArray(Field.PRIVILEGE);
        if(StringUtils.isNullBlank(name) || privilegeArray == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        List<Long> privilege = privilegeArray.toJavaList(Long.class);
        if(com.mingdong.common.util.CollectionUtils.isEmpty(privilege))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.addRole(name, privilege, resp);
        return resp;
    }

    /**
     * 账号角色 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/account/role")
    public RestResp editRole(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        Long roleId = jsonReq.getLong(Field.ID);
        String name = jsonReq.getString(Field.NAME);
        JSONArray privilegeArray = jsonReq.getJSONArray(Field.PRIVILEGE);
        if(roleId == null || StringUtils.isNullBlank(name) || privilegeArray == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        List<Long> privilege = privilegeArray.toJavaList(Long.class);
        if(com.mingdong.common.util.CollectionUtils.isEmpty(privilege))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.editRole(roleId, name, privilege, resp);
        return resp;
    }

    /**
     * 账号角色 - 变更状态
     */
    @LoginRequired
    @PostMapping(value = "/account/role/status")
    public RestResp changeRoleStatus(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        Long roleId = jsonReq.getLong(Field.ID);
        if(roleId == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.changeStatus(roleId, resp);
        return resp;
    }

    /**
     * 账号角色 - 列表
     */
    @LoginRequired
    @GetMapping(value = "/account/role/list")
    public ListRes getManagerRoleList(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        Page page = new Page(pageNum, pageSize);
        managerService.getRoleList(page, res);
        return res;
    }

    /**
     * 账号角色 - 验证是否存在
     */
    @LoginRequired
    @GetMapping(value = "/account/role/verification")
    public Map<String, Object> getList(@RequestParam(value = Field.NAME) String name)
    {
        RestResp resp = RestResp.build();
        managerService.checkIfRoleNameExist(name, resp);
        return resp.getDataMap();
    }

    /**
     * 管理账号 - 新增
     */
    @LoginRequired
    @PostMapping(value = "/account/addition")
    public RestResp addNewManager(@RequestBody NewManagerVO vo)
    {
        RestResp resp = RestResp.build();
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
        managerService.addManager(vo.getUsername(), vo.getPassword(), vo.getName(), vo.getPhone(), vo.getQq(),
                vo.getRoleId(), vo.getEnabled(), vo.getPrivilege(), resp);
        return resp;
    }

    /**
     * 管理账号 - 列表
     */
    @LoginRequired
    @GetMapping(value = "/account/list")
    public ListRes getManagerList(@RequestParam(value = Field.ROLE_ID, required = false) Long roleId,
            @RequestParam(value = Field.ENABLED, required = false) Integer enabled,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
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
        managerService.getManagerList(roleId, enabled, page, res);
        return res;
    }

    /**
     * 管理账号 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/account")
    public RestResp editManager(@RequestBody ManagerVO vo)
    {
        RestResp resp = RestResp.build();
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
        managerService.editManager(vo.getManagerId(), vo.getRoleId(), vo.getName(), vo.getPhone(), vo.getQq(),
                vo.getEnabled(), vo.getPrivilege(), resp);
        return resp;
    }

    /**
     * 管理账号 - 变更状态
     */
    @LoginRequired
    @PostMapping(value = "/account/status")
    public RestResp changeStatus(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        Long managerId = jsonReq.getLong(Field.ID);
        if(managerId == null)
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        managerService.changeManagerStatus(managerId, resp);
        return resp;
    }
}