package com.mingdong.csp.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;

public interface ProductService
{
    void getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp);

    XSSFWorkbook createProductRechargeXlsx(Long clientId, Long productId, Date fromDate, Date endDate);

    void getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp);

    XSSFWorkbook createProductRequestXlsx(Long clientId, Long productId, Date fromDate, Date endDate);
}
