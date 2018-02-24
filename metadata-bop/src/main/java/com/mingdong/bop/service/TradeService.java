package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.ListRes;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

public interface TradeService
{
    /**
     * 根据条件获取充值记录列表
     */
    void getProductRechargeList(Long clientId, Long productId, Date startTime, Date endTime, Page page, ListRes res);

    /**
     * 根据条件获取充值记录列表
     */
    void getProductRechargeInfoList(String keyword, Long productId, Long managerId, Long rechargeType, Date fromDate,
            Date toDate, Page page, ListRes res);

    /**
     * 根据条件导出充值记录列表
     */
    XSSFWorkbook createProductRechargeInfoListXlsx(String keyword, Long productId, Long managerId, Long rechargeType,
            Date fromDate, Date toDate, Page page);

    /**
     * 根据条件获取客户消费记录列表
     */
    void getClientBillList(String shortName, Long typeId, Long clientId, Long userId, Long productId, Date startDate,
            Date endDate, Page page, ListRes res);

    /**
     * 根据条件导出客户消费记录列表
     */
    XSSFWorkbook createClientBillListXlsx(String shortName, Long typeId, Long clientId, Long userId, Long productId,
            Date startDate, Date endDate, Page page);

    void getClientBillList(String keyword, Long productId, Integer billPlan, Date fromDate, Date toDate, Page page,
            ListRes res);

    XSSFWorkbook createClientBillListXlsx(String keyword, Long productId, Integer billPlan, Date fromDate, Date toDate,
            Page page);

    XSSFWorkbook createClientBillListXlsx(Long clientId, Long userId, Long productId, Date fromDate, Date toDate, Page page);
}
