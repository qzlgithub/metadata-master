package com.mingdong.csp.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;
import java.util.List;

public interface ProductService
{
    /**
     * 获取客户产品充值记录
     */
    void getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date toDate, Page page, RestResp resp);

    /**
     * 导出客户产品充值记录
     */
    XSSFWorkbook createProductRechargeXlsx(Long clientId, Long productId, Date fromDate, Date toDate);

    /**
     * 客户请求记录
     */
    void getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date toDate, Page page, RestResp resp);

    /**
     * 导出客户请求记录
     */
    XSSFWorkbook createProductRequestXlsx(Long clientId, Long productId, Date fromDate, Date toDate);

    /**
     * 查询客户产品的详请页信息
     */
    void getClientProductInfoData(Long clientId, Long productId, RestResp resp);

    List<Dict> getClientProductDict(Long clientId);

    /**
     * 根据客户id获取客户产品详细信息列表
     */
    void getClientProductDetailList(Long clientId, RestResp resp);

    void getProductListBy(Long clientId, List<Integer> productTypeList, Integer incOpened, Page page, RestListResp res);
}
