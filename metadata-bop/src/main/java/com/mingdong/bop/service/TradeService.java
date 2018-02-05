package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

public interface TradeService
{
    /**
     * 测试数据
     */
    //    BLResp testList2(Long productId, Long clientId, Date time, Page page);
    //
    //    BLResp testList3(Long clientId, Date time, Page page);
    //
    //    BLResp testList4(Long clientId, Date time, Page page);

    /**
     * 根据条件获取充值记录列表
     *
     * @param clientId
     * @param productId
     * @param startTime
     * @param endTime
     * @param page
     * @param resp
     */
    void getProductRechargeList(Long clientId, Long productId, Date startTime, Date endTime, Page page, BLResp resp);

    /**
     * 根据条件获取充值记录列表
     *
     * @param shortName
     * @param typeId
     * @param productId
     * @param managerId
     * @param startDate
     * @param endDate
     * @param page
     * @param resp
     */
    void getProductRechargeInfoList(String shortName, Long typeId, Long productId, Long managerId, Date startDate,
            Date endDate, Page page, BLResp resp);

    /**
     * 根据条件导出充值记录列表
     *
     * @param shortName
     * @param typeId
     * @param productId
     * @param managerId
     * @param startDate
     * @param endDate
     * @param page
     * @return
     */
    XSSFWorkbook createProductRechargeInfoListXlsx(String shortName, Long typeId, Long productId, Long managerId,
            Date startDate, Date endDate, Page page);

    /**
     * 根据条件获取客户消费记录列表
     *
     * @param shortName
     * @param typeId
     * @param productId
     * @param startDate
     * @param endDate
     * @param page
     * @param resp
     */
    void getClientBillList(String shortName, Long typeId, Long productId, Date startDate, Date endDate, Page page,
            BLResp resp);

    /**
     * 根据条件导出客户消费记录列表
     *
     * @param shortName
     * @param typeId
     * @param productId
     * @param startDate
     * @param endDate
     * @param page
     * @return
     */
    XSSFWorkbook createClientBillListXlsx(String shortName, Long typeId, Long productId, Date startDate, Date endDate,
            Page page);
}
