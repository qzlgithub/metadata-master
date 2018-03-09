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
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.dto.request.SysConfigReqDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    public RestListResp getRechargeTypeList()
    {
        RestListResp res = new RestListResp();
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
        RestResp resp = new RestResp();
        String name = jsonReq.getString(Field.NAME);
        if(StringUtils.isNullBlank(name))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        String remark = jsonReq.getString(Field.REMARK);
        systemService.addRechargeType(name, remark, resp);
        return resp;
    }

    /**
     * 充值类型 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/setting/rechargeType")
    public RestResp editRechargeType(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        Integer id = jsonReq.getInteger(Field.ID);
        String name = jsonReq.getString(Field.NAME);
        String remark = jsonReq.getString(Field.REMARK);
        if(id == null || StringUtils.isNullBlank(name))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
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
        RestResp resp = new RestResp();
        Integer id = jsonReq.getInteger(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(id == null || (!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled)))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        systemService.enableRechargeType(id, enabled, resp);
        return resp;
    }

    /**
     * 行业字典 - 验证行业编码是否存在
     */
    @LoginRequired
    @GetMapping(value = "/setting/industry/verification")
    public RestResp configIndustry(@RequestParam(value = Field.CODE) String code)
    {
        RestResp resp = new RestResp();
        systemService.checkIfIndustryExist(code, resp);
        return resp;
    }

    /**
     * 行业字典 - 新增
     */
    @LoginRequired
    @PutMapping(value = "/setting/industry/addition")
    public RestResp addIndustryType(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        Long id = jsonReq.getLong(Field.ID);
        String code = jsonReq.getString(Field.CODE);
        String name = jsonReq.getString(Field.NAME);
        if(StringUtils.isNullBlank(code) || StringUtils.isNullBlank(name))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
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
        RestResp resp = new RestResp();
        Long id = jsonReq.getLong(Field.ID);
        String code = jsonReq.getString(Field.CODE);
        String name = jsonReq.getString(Field.NAME);
        if(StringUtils.isNullBlank(code) || StringUtils.isNullBlank(name))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
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
        RestResp resp = new RestResp();
        Long id = jsonReq.getLong(Field.ID);
        Integer enabled = jsonReq.getInteger(Field.ENABLED);
        if(id == null || (!TrueOrFalse.TRUE.equals(enabled) && !TrueOrFalse.FALSE.equals(enabled)))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
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
        RestResp resp = new RestResp();
        Long privilegeId = jsonReq.getLong(Field.ID);
        String name = jsonReq.getString(Field.NAME);
        if(privilegeId == null || StringUtils.isNullBlank(name))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        systemService.editPrivilegeInfo(privilegeId, name, resp);
        if(RestResult.SUCCESS.getCode().equals(resp.getCode()))
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
        RestResp resp = new RestResp();
        JSONArray moduleArray = jsonReq.getJSONArray(Field.MODULE);
        List<Long> moduleIdList = moduleArray.toJavaList(Long.class);
        Integer status = jsonReq.getInteger(Field.STATUS);
        if(CollectionUtils.isEmpty(moduleIdList) || (!TrueOrFalse.TRUE.equals(status) && !TrueOrFalse.FALSE.equals(
                status)))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        systemService.setModuleStatus(moduleIdList, status);
        return resp;
    }

    /**
     * 其它设置 - 编辑
     */
    @LoginRequired
    @PostMapping(value = "/setting/configuration")
    public RestResp setGlobalSetting(@RequestBody JSONObject jsonReq)
    {
        RestResp resp = new RestResp();
        Integer subUserQty = jsonReq.getInteger(Field.SUB_USER_QTY);
        String serviceQQ = jsonReq.getString(Field.SERVICE_QQ);
        if(subUserQty == null || StringUtils.isNullBlank(serviceQQ))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        List<SysConfigReqDTO> sysConfigReqDTOList = new ArrayList<>();
        SysConfigReqDTO sysConfigReqDTO = new SysConfigReqDTO();
        sysConfigReqDTO.setName(SysParam.CLIENT_SUB_USER_QTY);
        sysConfigReqDTO.setValue(subUserQty + "");
        sysConfigReqDTOList.add(sysConfigReqDTO);
        sysConfigReqDTO = new SysConfigReqDTO();
        sysConfigReqDTO.setName(SysParam.SERVICE_QQ);
        sysConfigReqDTO.setValue(serviceQQ + "");
        sysConfigReqDTOList.add(sysConfigReqDTO);
        systemService.setGlobalSetting(sysConfigReqDTOList, resp);
        return resp;
    }
}
