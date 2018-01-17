package com.mingdong.csp.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProductService
{
    /**
     * 获取客户项目充值记录
     */
    void getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp);

    /**
     * 导出客户项目充值记录
     */
    XSSFWorkbook createProductRechargeXlsx(Long clientId, Long productId, Date fromDate, Date endDate);

    /**
     * 客户请求记录
     */
    void getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp);

    /**
     * 导出客户请求记录
     */
    XSSFWorkbook createProductRequestXlsx(Long clientId, Long productId, Date fromDate, Date endDate);

    void getClientProductDetail(Long clientId, Long productId, BLResp resp);

    List<Map<String, Object>> getClientProductList(Long clientId);
}
