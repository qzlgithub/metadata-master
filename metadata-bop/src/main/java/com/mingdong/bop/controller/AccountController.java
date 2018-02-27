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
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
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
        RestResp resp = new RestResp();
        String name = jsonReq.getString(Field.NAME);
        JSONArray privilegeArray = jsonReq.getJSONArray(Field.PRIVILEGE);
        if(StringUtils.isNullBlank(name) || privilegeArray == null)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        List<Long> privilege = privilegeArray.toJavaList(Long.class);
        if(com.mingdong.common.util.CollectionUtils.isEmpty(privilege))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
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
        RestResp resp = new RestResp();
        Long roleId = jsonReq.getLong(Field.ID);
        String name = jsonReq.getString(Field.NAME);
        JSONArray privilegeArray = jsonReq.getJSONArray(Field.PRIVILEGE);
        if(roleId == null || StringUtils.isNullBlank(name) || privilegeArray == null)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        List<Long> privilege = privilegeArray.toJavaList(Long.class);
        if(com.mingdong.common.util.CollectionUtils.isEmpty(privilege))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
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
        RestResp resp = new RestResp();
        Long roleId = jsonReq.getLong(Field.ID);
        if(roleId == null)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        managerService.changeStatus(roleId, resp);
        return resp;
    }

    /**
     * 账号角色 - 列表
     */
    @LoginRequired
    @GetMapping(value = "/account/role/list")
    public RestListResp getManagerRoleList(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
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
        RestResp resp = new RestResp();
        managerService.checkIfRoleNameExist(name, resp);
        return resp.getData();
    }

    /**
     * 管理账号 - 新增
     */
    @LoginRequired
    @PostMapping(value = "/account/addition")
    public RestResp addNewManager(@RequestBody NewManagerVO vo)
    {
        RestResp resp = new RestResp();
        // 校验各个字段值
        if(StringUtils.isNullBlank(vo.getUsername()) || StringUtils.isNullBlank(vo.getPassword()) ||
                StringUtils.isNullBlank(vo.getName()) || StringUtils.isNullBlank(vo.getPhone()))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(vo.getRoleId() == null || vo.getRoleId() <= 0)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(!TrueOrFalse.TRUE.equals(vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled()))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(CollectionUtils.isEmpty(vo.getPrivilege()))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
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
    public RestListResp getManagerList(@RequestParam(value = Field.ROLE_ID, required = false) Long roleId,
            @RequestParam(value = Field.ENABLED, required = false) Integer enabled,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
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
        RestResp resp = new RestResp();
        if(vo.getRoleId() == null || vo.getRoleId() <= 0)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(!TrueOrFalse.TRUE.equals(vo.getEnabled()) && !TrueOrFalse.FALSE.equals(vo.getEnabled()))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(CollectionUtils.isEmpty(vo.getPrivilege()))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
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
        RestResp resp = new RestResp();
        Long managerId = jsonReq.getLong(Field.ID);
        if(managerId == null)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        managerService.changeManagerStatus(managerId, resp);
        return resp;
    }
}