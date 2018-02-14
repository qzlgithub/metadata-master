package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.bop.service.StatsService;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.ListRes;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping(value = "stats")
public class StatsController
{
    @Resource
    private StatsService statsService;

    @GetMapping(value = "/client/clientList")
    @ResponseBody
    public ListRes getClientList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        statsService.getClientList(scopeTypeEnum, new Page(pageNum, pageSize), res);
        return res;
    }

    @GetMapping(value = "client/clientList/export")
    public void exportClientList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            HttpServletResponse response) throws IOException
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        XSSFWorkbook wb = statsService.createClientListXlsx(scopeTypeEnum, new Page(1, 1000));
        String filename = new String("客户数据".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @GetMapping(value = "client/clientListJson")
    @ResponseBody
    public String getClientListJson(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType)
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        JSONArray jsonArray = statsService.getClientListJson(scopeTypeEnum);
        return jsonArray.toJSONString();
    }

    @GetMapping(value = "/client/rechargeList")
    @ResponseBody
    public ListRes getRechargeList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ListRes res = new ListRes();
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        statsService.getRechargeList(scopeTypeEnum, new Page(pageNum, pageSize), res);
        return res;
    }

    @GetMapping(value = "client/rechargeList/export")
    public void exportRechargeList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            HttpServletResponse response) throws IOException
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        XSSFWorkbook wb = statsService.createRechargeListXlsx(scopeTypeEnum, new Page(1, 1000));
        String filename = new String("充值数据".getBytes(), "ISO8859-1");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    @GetMapping(value = "client/rechargeListJson")
    @ResponseBody
    public String getRechargeListJson(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType)
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        JSONObject jsonObject = statsService.getRechargeListJson(scopeTypeEnum);
        return jsonObject.toJSONString();
    }

}
