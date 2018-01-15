package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ProductRecListDTO;
import com.mingdong.core.model.dto.ProductReqListDTO;

import java.util.Date;

public interface RemoteProductService
{
    /**
     * 获取客户项目充值记录
     *
     * @param clientId
     * @param productId
     * @param fromDate
     * @param endDate
     * @param page
     */
    ProductRecListDTO getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page);

    /**
     * 获取客户请求记录
     *
     * @param clientId
     * @param productId
     * @param fromDate
     * @param endDate
     * @param page
     */
    ProductReqListDTO getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page);

}
