package com.mingdong.csp.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.ListRes;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
     * 根据客户id获取客户产品详细信息
     *
     * @param clientId
     * @param productId
     * @param resp
     */
    void getClientProductDetail(Long clientId, Long productId, RestResp resp);

    /**
     * 根据客户id获取客户产品列表
     *
     * @param clientId
     * @return
     */
    List<Map<String, Object>> getClientProductList(Long clientId);

    /**
     * 根据客户id获取客户产品详细信息列表
     *
     * @param clientId
     * @param resp
     */
    void getClientProductDetailList(Long clientId, RestResp resp);

    /**
     * 根据状态获取产品类型列表
     *
     * @param enabled
     * @return
     */
    //    List<Map<String, Object>> getDictProductTypeList(Integer enabled);

    /**
     * 根据条件获取产品信息列表
     *
     * @param clientId
     * @param isOpen
     * @param selectedType
     * @param page
     * @param resp
     */
    void getProductListBy(Long clientId, Integer isOpen, Integer[] selectedType, Page page, RestResp resp);

    void getProductListBy(Long clientId, List<Integer> productTypeList, Integer incOpened, Page page, ListRes res);
}
