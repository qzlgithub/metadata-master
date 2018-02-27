package com.mingdong.bop.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.ListRes;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface StatsService
{
    /**
     * 获取数据统计index页面的数据
     */
    RestResp getIndexStats();

    /**
     * 获取客户统计index页面的数据
     */
    RestResp getClientIndexStats();

    /**
     * 获取客户列表
     */
    void getClientList(ScopeType scopeTypeEnum, Page page, ListRes res);

    /**
     * 导出客户列表
     */
    XSSFWorkbook createClientListXlsx(ScopeType scopeTypeEnum, Page page);

    /**
     * 客户统计echarts所需要的json数据
     */
    JSONArray getClientListJson(ScopeType scopeTypeEnum);

    /**
     * 获取充值统计index页面的数据
     */
    RestResp getRechargeIndexStats();

    /**
     * 获取充值列表
     */
    void getRechargeList(ScopeType scopeTypeEnum, Page page, ListRes res);

    /**
     * 导出充值列表
     */
    XSSFWorkbook createRechargeListXlsx(ScopeType scopeTypeEnum, Page page);

    /**
     * 充值统计echarts所需要的json数据
     */
    JSONObject getRechargeListJson(ScopeType scopeTypeEnum);

    /**
     * 获取产品请求数据
     */
    void getRequestList(ScopeType scopeTypeEnum, Page page, String name, Long productId, ListRes res);

    XSSFWorkbook createRequestListXlsx(ScopeType scopeTypeEnum, Page page, String name, Long productId);

    JSONArray getRequestListJson(ScopeType scopeTypeEnum, String name, Long productId);

    RestResp getRequestIndexStats();
}
