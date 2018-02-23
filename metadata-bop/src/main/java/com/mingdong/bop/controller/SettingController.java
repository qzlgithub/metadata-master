package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.ListRes;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SettingController
{
    @Resource
    private SystemService systemService;

    @LoginRequired
    @GetMapping(value = "dict/recharge/list")
    public ListRes getRechargeTypeList()
    {
        ListRes res = new ListRes();
        systemService.getRechargeTypeList(res);
        return res;
    }

    @LoginRequired
    @PutMapping(value = "dict/recharge")
    public BLResp addRechargeType(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        String name = jsonReq.getString(Field.NAME);
        if(StringUtils.isNullBlank(name))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        String remark = jsonReq.getString(Field.REMARK);
        systemService.addRechargeType(name, remark, resp);
        return resp;
    }

    @LoginRequired
    @DeleteMapping(value = "dict/recharge")
    public BLResp dropRechargeType(@RequestBody JSONObject jsonReq)
    {
        Integer id = jsonReq.getInteger(Field.ID);
        if(id != null)
        {
            systemService.dropRechargeType(id);
        }
        return BLResp.build();
    }

    @LoginRequired
    @PostMapping(value = "dict/recharge")
    public BLResp editRechargeType(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Integer id = jsonReq.getInteger(Field.ID);
        String name = jsonReq.getString(Field.NAME);
        String remark = jsonReq.getString(Field.REMARK);
        if(id == null || StringUtils.isNullBlank(name))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.editRechargeType(id, name, remark, resp);
        return resp;

    }

    @LoginRequired
    @PostMapping(value = "dict/recharge/status")
    public BLResp enableRechargeType(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Integer id = jsonReq.getInteger(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(id == null || (!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled)))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.enableRechargeType(id, enabled, resp);
        return resp;
    }
}
