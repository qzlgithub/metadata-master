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
    BLResp testList2(Long productId, Long clientId, Date time, Page page);

    BLResp testList3(Long clientId, Date time, Page page);

    BLResp testList4(Long clientId, Date time, Page page);

    void getProductRechargeList(Long clientId, Long productId, Date startTime, Date endTime, Page page, BLResp resp);

    void getProductRechargeInfoList(String shortName, Long typeId, Long productId, Long managerId, Date startDate,
            Date endDate, Page page, BLResp resp);

    XSSFWorkbook createProductRechargeInfoListXlsx(String shortName, Long typeId, Long productId, Long managerId,
            Date startDate, Date endDate, Page page);

    void getClientBillList(String shortName, Long typeId, Long productId, Date startDate, Date endDate, Page page,
            BLResp resp);

    XSSFWorkbook createClientBillListXlsx(String shortName, Long typeId, Long productId, Date startDate, Date endDate,
            Page page);
}
