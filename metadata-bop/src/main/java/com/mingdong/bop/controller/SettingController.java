package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.SysParam;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.ListRes;
import com.mingdong.core.model.dto.SysConfigDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SettingController
{
    @Resource
    private SystemService systemService;

    /**
     * 充值类型 - 列表
     */
    @LoginRequired
    @GetMapping(value = "/setting/rechargeType/list")
    public ListRes getRechargeTypeList()
    {
        ListRes res = new ListRes();
        systemService.getRechargeTypeList(res);
        return res;
    }

    /**
     * 充值类型 - 新增
     */
    @LoginRequired
    @PutMapping(value = "/setting/rechargeType/addition")
    public RestResp addRechargeType(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        String name = jsonReq.getString(Field.NAME);
        if(StringUtils.isNullBlank(name))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        String remark = jsonReq.getString(Field.REMARK);
        systemService.addRechargeType(name, remark, resp);
        return resp;
    }

    /**
     * 充值类型 - 删除
     */
    @LoginRequired
    @DeleteMapping(value = "/setting/rechargeType/deletion")
    public RestResp dropRechargeType(@RequestBody JSONObject jsonReq)
    {
        Integer id = jsonReq.getInteger(Field.ID);
        if(id != null)
        {
            systemService.dropRechargeType(id);
        }
        return RestResp.build();
    }

    /**
     * 充值类型 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/setting/rechargeType")
    public RestResp editRechargeType(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
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

    /**
     * 充值类型 - 变更状态
     */
    @LoginRequired
    @PostMapping(value = "/setting/rechargeType/status")
    public RestResp enableRechargeType(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        Integer id = jsonReq.getInteger(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(id == null || (!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled)))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.enableRechargeType(id, enabled, resp);
        return resp;
    }

    /**
     * 行业字典 - 验证行业编码是否存在
     */
    @LoginRequired
    @GetMapping(value = "/setting/industry/verification")
    public Map<String, Integer> configIndustry(@RequestParam(value = Field.CODE) String code)
    {
        Map<String, Integer> map = new HashMap<>();
        boolean exist = systemService.checkIndustryCodeExist(code);
        map.put(Field.EXIST, exist ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        return map;
    }

    /**
     * 行业字典 - 新增
     */
    @LoginRequired
    @PutMapping(value = "/setting/industry/addition")
    public RestResp addIndustryType(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
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

    /**
     * 行业字典 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/setting/industry")
    public RestResp editIndustryInfo(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
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

    /**
     * 行业字典 - 变更状态
     */
    @LoginRequired
    @PostMapping(value = "/setting/industry/status")
    public RestResp changeIndustryStatus(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        Long id = jsonReq.getLong(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(id == null || (!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled)))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.changeIndustryStatus(id, enabled, resp);
        return resp;
    }

    /**
     * 系统菜单 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/setting/menu")
    public RestResp editColumnInfo(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
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

    /**
     * 系统菜单 - 变更状态
     */
    @LoginRequired
    @PostMapping(value = "/setting/menu/status")
    public RestResp setModuleStatus(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
        JSONArray moduleArray = jsonReq.getJSONArray(Field.MODULE);
        List<Long> moduleIdList = moduleArray.toJavaList(Long.class);
        Integer status = jsonReq.getInteger(Field.STATUS);
        if(CollectionUtils.isEmpty(moduleIdList) || (!TrueOrFalse.TRUE.equals(status) && !TrueOrFalse.FALSE.equals(
                status)))
        {
            return resp.result(RestResult.KEY_FIELD_MISSING);
        }
        systemService.setModuleStatus(moduleIdList, status, resp);
        return resp;
    }

    /**
     * 其它设置 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/setting/configuration")
    public RestResp setGlobalSetting(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = RestResp.build();
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
