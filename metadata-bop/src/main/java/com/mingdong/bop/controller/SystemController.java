package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.SystemService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "system")
public class SystemController
{
    @Resource
    private SystemService systemService;

    @RequestMapping(value = "column/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getIndustryList(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        // TODO FIX...
        /*Page page = new Page(pageNum, pageSize);
        BLResp resp = systemService.getColumnList(page);
        return resp.getDataMap();*/
        return null;
    }

    @RequestMapping(value = "industry/childList", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getSubIndustryList(@RequestParam(value = Field.INDUSTRY_ID) Long industryId)
    {
        return systemService.getIndustryList(industryId, TrueOrFalse.TRUE);
    }

    @RequestMapping(value = "industry/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getIndustryLis(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        Page page = new Page(pageNum, pageSize);
        BLResp resp = systemService.getIndustryList(page);
        return resp.getDataMap();
    }

    @PostMapping(value = "module/status")
    @ResponseBody
    public BLResp setModuleStatus(@RequestBody JSONObject jsonReq)
    {
        BLResp resp = BLResp.build();
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
}
