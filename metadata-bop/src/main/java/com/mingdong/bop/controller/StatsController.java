package com.mingdong.bop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.StatsService;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(value = "index.html")
    public ModelAndView gotoStatsIndex()
    {
        ModelAndView view = new ModelAndView("data-analysis/data-index");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "customer.html")
    public ModelAndView gotoStatsCustomer()
    {
        ModelAndView view = new ModelAndView("data-analysis/customer-data");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getClientIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "/client/clientList", method = RequestMethod.GET)
    @ResponseBody
    public BLResp getClientList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        BLResp resp = statsService.getClientList(scopeTypeEnum, new Page(pageNum, pageSize));
        return resp;
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

    @RequestMapping(value = "recharge.html")
    public ModelAndView gotoStatsRecharge()
    {
        ModelAndView view = new ModelAndView("data-analysis/recharge-data");
        view.addAllObjects(RequestThread.getMap());
        BLResp resp = statsService.getRechargeIndexStats();
        view.addAllObjects(resp.getDataMap());
        return view;
    }

    @RequestMapping(value = "/client/rechargeList", method = RequestMethod.GET)
    @ResponseBody
    public BLResp getRechargeList(@RequestParam(value = Field.SCOPE_TYPE, required = false) String scopeType,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        ScopeType scopeTypeEnum = ScopeType.getScopeType(scopeType);
        BLResp resp = statsService.getRechargeList(scopeTypeEnum, new Page(pageNum, pageSize));
        return resp;
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

    @RequestMapping(value = "revenue.html")
    public ModelAndView gotoStatsRevenue()
    {
        ModelAndView view = new ModelAndView("data-analysis/revenue-data");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

    @RequestMapping(value = "request.html")
    public ModelAndView gotoStatsRequest()
    {
        ModelAndView view = new ModelAndView("data-analysis/product-data-request");
        view.addAllObjects(RequestThread.getMap());
        return view;
    }

}
