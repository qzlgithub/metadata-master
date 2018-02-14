package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.SysParam;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.SysConfigDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "config")
public class ConfigController
{
    @Resource
    private SystemService systemService;

    @GetMapping(value = "industry")
    @ResponseBody
    public List<Map<String, Object>> configIndustry(
            @RequestParam(value = Field.PARENT_ID, defaultValue = "0") Long parentId)
    {
        return systemService.getIndustryMap(parentId, TrueOrFalse.TRUE);
    }

    @GetMapping(value = "industryInfo")
    @ResponseBody
    public Map<String, String> getIndustryInfo(@RequestParam(value = Field.ID) Long id)
    {
        return systemService.getIndustryInfo(id);
    }

    @GetMapping(value = "columnInfo")
    @ResponseBody
    public Map<String, Object> getColumnInfo(@RequestParam(value = Field.ID) Long id)
    {
        return systemService.getPrivilegeInfo(id);
    }

    @PostMapping(value = "column/modification")
    @ResponseBody
    public BLResp editColumnInfo(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long privilegeId = jsonReq.getLong(Field.ID);
        String name = jsonReq.getString(Field.NAME);
        if(privilegeId == null || StringUtils.isNullBlank(name))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.editPrivilegeInfo(privilegeId, name, resp);
        if(RestResult.SUCCESS.getCode().equals(resp.getErrCode()))
        {
            systemService.cacheSystemModule();
        }
        return resp;
    }

    @GetMapping(value = "industry/checkCode")
    @ResponseBody
    public Map<String, Integer> configIndustry(@RequestParam(value = Field.CODE) String code)
    {
        Map<String, Integer> map = new HashMap<>();
        boolean exist = systemService.checkIndustryCodeExist(code);
        map.put(Field.EXIST, exist ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        return map;
    }

    @PostMapping(value = "industry/addition")
    @ResponseBody
    public BLResp addIndustryType(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long id = jsonReq.getLong(Field.ID);
        String code = jsonReq.getString(Field.CODE);
        String name = jsonReq.getString(Field.NAME);
        if(StringUtils.isNullBlank(code) || StringUtils.isNullBlank(name))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.addIndustryType(id, code, name, resp);
        return resp;
    }

    @PostMapping(value = "industry/modification")
    @ResponseBody
    public BLResp editIndustryInfo(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long id = jsonReq.getLong(Field.ID);
        String code = jsonReq.getString(Field.CODE);
        String name = jsonReq.getString(Field.NAME);
        if(StringUtils.isNullBlank(code) || StringUtils.isNullBlank(name))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.editIndustryInfo(id, code, name, resp);
        return resp;
    }

    @PostMapping(value = "industry/status")
    @ResponseBody
    public BLResp changeIndustryStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long id = jsonReq.getLong(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(id == null || (!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled)))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.changeIndustryStatus(id, enabled, resp);
        return resp;
    }

    @PostMapping(value = "recharge/addition")
    @ResponseBody
    public BLResp addNewRechargeType(@RequestBody JSONObject jsonReq)
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

    @GetMapping(value = "recharge/deletion")
    @ResponseBody
    public BLResp dropRechargeType(@RequestParam(value = Field.RECHARGE_TYPE_ID) Long rechargeTypeId)
    {
        BLResp resp = BLResp.build();
        systemService.dropRechargeType(rechargeTypeId, resp);
        return resp;
    }

    @GetMapping(value = "recharge/info")
    @ResponseBody
    public BLResp getRechargeTypeInfo(@RequestParam(value = Field.RECHARGE_TYPE_ID) Long rechargeTypeId)
    {
        BLResp resp = BLResp.build();
        systemService.getRechargeTypeInfo(rechargeTypeId, resp);
        return resp;
    }

    @PostMapping(value = "recharge/modification")
    @ResponseBody
    public BLResp updateRechargeType(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long id = jsonReq.getLong(Field.ID);
        String name = jsonReq.getString(Field.NAME);
        String remark = jsonReq.getString(Field.REMARK);
        if(id == null || StringUtils.isNullBlank(name))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.updateRechargeType(id, name, remark, resp);
        return resp;

    }

    @PostMapping(value = "recharge/status")
    @ResponseBody
    public BLResp changeRechargeStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Long id = jsonReq.getLong(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(id == null || (!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled)))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.changeRechargeStatus(id, enabled, resp);
        return resp;
    }

    @PostMapping(value = "global/setting")
    @ResponseBody
    public BLResp setGlobalSetting(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
        Integer subUserQty = jsonReq.getInteger(Field.SUB_USER_QTY);
        String serviceQQ = jsonReq.getString(Field.SERVICE_QQ);
        if(subUserQty == null || StringUtils.isNullBlank(serviceQQ))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        List<SysConfigDTO> sysConfigDTOList = new ArrayList<>();
        SysConfigDTO sysConfigDTO = new SysConfigDTO();
        sysConfigDTO.setName(SysParam.CLIENT_SUB_USER_QTY);
        sysConfigDTO.setValue(subUserQty + "");
        sysConfigDTOList.add(sysConfigDTO);
        sysConfigDTO = new SysConfigDTO();
        sysConfigDTO.setName(SysParam.SERVICE_QQ);
        sysConfigDTO.setValue(serviceQQ + "");
        sysConfigDTOList.add(sysConfigDTO);
        systemService.setGlobalSetting(sysConfigDTOList, resp);
        return resp;
    }
}