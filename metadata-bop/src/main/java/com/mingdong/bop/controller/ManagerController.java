package com.mingdong.bop.controller;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ManagerVO;
import com.mingdong.bop.model.NewManagerVO;
import com.mingdong.bop.service.ManagerService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "manager")
public class ManagerController
{
    @Resource
    private ManagerService managerService;

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
        managerService.addManager(vo.getUsername(), vo.getPassword(), vo.getName(), vo.getPhone(), vo.getQq(),
                vo.getRoleId(), vo.getEnabled(), vo.getPrivilege(), resp);
        return resp;
    }

    /**
     * 获取管理账号列表
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
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
        managerService.editManager(vo.getManagerId(), vo.getRoleId(), vo.getName(), vo.getPhone(), vo.getQq(),
                vo.getEnabled(), vo.getPrivilege(), resp);
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
