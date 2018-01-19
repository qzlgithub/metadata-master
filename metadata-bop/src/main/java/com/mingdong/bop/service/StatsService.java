package com.mingdong.bop.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.bop.constant.ScopeType;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface StatsService
{
    BLResp getIndexStats();

    BLResp getClientIndexStats();

    BLResp getClientList(ScopeType scopeTypeEnum, Page page);

    XSSFWorkbook createClientListXlsx(ScopeType scopeTypeEnum, Page page);

    JSONArray getClientListJson(ScopeType scopeTypeEnum);

    BLResp getRechargeIndexStats();

    BLResp getRechargeList(ScopeType scopeTypeEnum, Page page);

    XSSFWorkbook createRechargeListXlsx(ScopeType scopeTypeEnum, Page page);

    JSONObject getRechargeListJson(ScopeType scopeTypeEnum);

}
