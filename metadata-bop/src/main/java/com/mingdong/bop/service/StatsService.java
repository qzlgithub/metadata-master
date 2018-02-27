package com.mingdong.bop.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
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
    void getClientList(ScopeType scopeTypeEnum, Page page, RestListResp res);

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
    void getRechargeList(ScopeType scopeTypeEnum, Page page, RestListResp res);

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
    void getRequestList(ScopeType scopeTypeEnum, Page page, String name, Long productId, RestListResp res);

    /**
     * 产品请求数据导出
     */
    XSSFWorkbook createRequestListXlsx(ScopeType scopeTypeEnum, Page page, String name, Long productId);

    /**
     * 产品请求数据json
     */
    JSONArray getRequestListJson(ScopeType scopeTypeEnum, String name, Long productId);

    /**
     * 产品请求数据index页面的数据
     */
    RestResp getRequestIndexStats();

    /**
     * 营收数据
     */
    void getRevenueList(ScopeType scopeTypeEnum, Page page, RestListResp res);

    /**
     * 营收数据导出
     */
    XSSFWorkbook createRevenueListXlsx(ScopeType scopeTypeEnum, Page page);

    RestResp getRevenueIndexStats();

    JSONArray getRevenueListJson(ScopeType scopeTypeEnum);

}
