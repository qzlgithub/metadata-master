package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "config")
public class ConfigController
{
    @Resource
    private SystemService systemService;

    @RequestMapping(value = "recharge.html")
    public ModelAndView configRecharge()
    {
        ModelAndView view = new ModelAndView("system-manage/recharge-manage");
        List<Map<String, Object>> list = systemService.getRechargeTypeList(null, TrueOrFalse.FALSE);
        view.addObject(Field.LIST, list);
        view.addObject(Field.TOTAL, list.size());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "industry.html")
    public ModelAndView configIndustry()
    {
        ModelAndView view = new ModelAndView("system-manage/industry-manage");
        view.addObject(Field.LIST, systemService.getHierarchyIndustry());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @GetMapping(value = "setting.html")
    public ModelAndView otherSettingPage()
    {
        ModelAndView view = new ModelAndView("system-manage/other-setting");
        view.addObject(Field.LIST, systemService.getHierarchyIndustry());
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "industry", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> configIndustry(
            @RequestParam(value = Field.PARENT_ID, defaultValue = "0") Long parentId)
    {
        return systemService.getIndustryMap(parentId, TrueOrFalse.TRUE);
    }

    @RequestMapping(value = "industryInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getIndustryInfo(@RequestParam(value = Field.ID) Long id)
    {
        return systemService.getIndustryInfo(id);
    }

    @RequestMapping(value = "columnInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getColumnInfo(@RequestParam(value = Field.ID) Long id)
    {
        return systemService.getPrivilegeInfo(id);
    }

    @RequestMapping(value = "column/modification", method = RequestMethod.POST)
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
        return resp;
    }

    @RequestMapping(value = "industry/checkCode", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Integer> configIndustry(@RequestParam(value = Field.CODE) String code)
    {
        Map<String, Integer> map = new HashMap<>();
        boolean exist = systemService.checkIndustryCodeExist(code);
        map.put(Field.EXIST, exist ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        return map;
    }

    @RequestMapping(value = "industry/addition", method = RequestMethod.POST)
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

    @RequestMapping(value = "industry/modification", method = RequestMethod.POST)
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

    @RequestMapping(value = "recharge/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getRechargeList()
    {
        BLResp resp = BLResp.build();
        systemService.getRechargeTypeList(null, resp);
        return resp.getDataMap();
    }

    @RequestMapping(value = "recharge/addition", method = RequestMethod.POST)
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

    @RequestMapping(value = "recharge/deletion", method = RequestMethod.GET)
    @ResponseBody
    public BLResp dropRechargeType(@RequestParam(value = Field.RECHARGE_TYPE_ID) Long rechargeTypeId)
    {
        BLResp resp = BLResp.build();
        systemService.dropRechargeType(rechargeTypeId, resp);
        return resp;
    }

    @RequestMapping(value = "recharge/info", method = RequestMethod.GET)
    @ResponseBody
    public BLResp getRechargeTypeInfo(@RequestParam(value = Field.RECHARGE_TYPE_ID) Long rechargeTypeId)
    {
        BLResp resp = BLResp.build();
        systemService.getRechargeTypeInfo(rechargeTypeId, resp);
        return resp;
    }

    @RequestMapping(value = "recharge/modification", method = RequestMethod.POST)
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

    @RequestMapping(value = "productTypeList", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> configTypeList()
    {
        return systemService.getProductListMap();
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
        systemService.setGlobalSetting(subUserQty, serviceQQ, resp);
        return resp;
    }
}