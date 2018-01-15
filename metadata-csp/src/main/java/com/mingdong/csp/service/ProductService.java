package com.mingdong.csp.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

public interface ProductService
{
    /**
     * 获取客户项目充值记录
     *
     * @param clientId
     * @param productId
     * @param fromDate
     * @param endDate
     * @param page
     * @param resp
     */
    void getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp);

    /**
     * 导出客户项目充值记录
     *
     * @param clientId
     * @param productId
     * @param fromDate
     * @param endDate
     * @return
     */
    XSSFWorkbook createProductRechargeXlsx(Long clientId, Long productId, Date fromDate, Date endDate);

    /**
     * 客户请求记录
     *
     * @param clientId
     * @param productId
     * @param fromDate
     * @param endDate
     * @param page
     * @param resp
     */
    void getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp);

    /**
     * 导出客户请求记录
     *
     * @param clientId
     * @param productId
     * @param fromDate
     * @param endDate
     * @return
     */
    XSSFWorkbook createProductRequestXlsx(Long clientId, Long productId, Date fromDate, Date endDate);
}
