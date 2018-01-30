package com.mingdong.bop.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface StatsService
{
    /**
     * 获取数据统计index页面的数据
     *
     * @return
     */
    BLResp getIndexStats();

    /**
     * 获取客户统计index页面的数据
     *
     * @return
     */
    BLResp getClientIndexStats();

    /**
     * 获取客户列表
     *
     * @param scopeTypeEnum
     * @param page
     * @return
     */
    BLResp getClientList(ScopeType scopeTypeEnum, Page page);

    /**
     * 导出客户列表
     *
     * @param scopeTypeEnum
     * @param page
     * @return
     */
    XSSFWorkbook createClientListXlsx(ScopeType scopeTypeEnum, Page page);

    /**
     * 客户统计echarts所需要的json数据
     *
     * @param scopeTypeEnum
     * @return
     */
    JSONArray getClientListJson(ScopeType scopeTypeEnum);

    /**
     * 获取充值统计index页面的数据
     *
     * @return
     */
    BLResp getRechargeIndexStats();

    /**
     * 获取充值列表
     *
     * @param scopeTypeEnum
     * @param page
     * @return
     */
    BLResp getRechargeList(ScopeType scopeTypeEnum, Page page);

    /**
     * 导出充值列表
     *
     * @param scopeTypeEnum
     * @param page
     * @return
     */
    XSSFWorkbook createRechargeListXlsx(ScopeType scopeTypeEnum, Page page);

    /**
     * 充值统计echarts所需要的json数据
     *
     * @param scopeTypeEnum
     * @return
     */
    JSONObject getRechargeListJson(ScopeType scopeTypeEnum);

}
