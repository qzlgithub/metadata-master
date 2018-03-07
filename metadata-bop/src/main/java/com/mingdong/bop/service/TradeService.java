package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestListResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

public interface TradeService
{
    /**
     * 根据条件获取充值记录列表
     */
    void getProductRechargeInfoList(String keyword, Long productId, Long managerId, Long rechargeType, Date fromDate,
            Date toDate, Page page, RestListResp res);

    /**
     * 根据条件导出充值记录列表
     */
    XSSFWorkbook createProductRechargeInfoListXlsx(String keyword, Long productId, Long managerId, Long rechargeType,
            Date fromDate, Date toDate, Page page);

    void getClientBillList(String keyword, Long productId, Integer billPlan, Date fromDate, Date toDate, Long managerId,
            Page page, RestListResp res);

    XSSFWorkbook createClientBillListXlsx(String keyword, Long productId, Integer billPlan, Date fromDate, Date toDate,
            Long manager, Page page);
}
