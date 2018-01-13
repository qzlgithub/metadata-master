package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductRequestDTO;

import java.util.Date;
import java.util.List;

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
     * @param resp
     */
    List<ProductRechargeDTO> getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp);

    /**
     * 获取客户请求记录
     *
     * @param clientId
     * @param productId
     * @param fromDate
     * @param endDate
     * @param page
     * @param resp
     */
    List<ProductRequestDTO> getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page, BLResp resp);

}
