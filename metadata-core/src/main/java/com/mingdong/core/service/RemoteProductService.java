package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductDictDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductRecListDTO;
import com.mingdong.core.model.dto.ProductReqListDTO;

import java.util.Date;

public interface RemoteProductService
{
    /**
     * 获取客户项目充值记录
     */
    ProductRecListDTO getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page);

    /**
     * 获取客户请求记录
     */
    ProductReqListDTO getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page);

    ProductListDTO getIndexProductList(Long clientId);

    ProductDTO getClientProductDetail(Long clientId, Long productId);

    ProductDictDTO getClientProductDictDTO(Long clientId);
}
