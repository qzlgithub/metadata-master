package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.model.ListRes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService
{
    /**
     * 根据产品id获取产品信息
     */
    Map<String, Object> getProductInfo(Long productId);

    /**
     * 修改产品
     */
    void editProduct(Long id, Integer productType, String code, String name, BigDecimal costAmt, Integer enabled,
            Integer custom, String remark, String content, RestResp resp);

    /**
     * 更改产品信息
     */
    void changeProductStatus(Long productId, Integer enabled, RestResp resp);

    /**
     * 获取产品信息列表
     */
    List<Map<String, Object>> getProductDict();

    /**
     * 根据状态获取产品信息列表
     */
    List<Map<String, Object>> getProductInfoListMap(Integer enabled);

    void getProductList(String keyword, Integer type, Integer custom, Integer status, Page page, ListRes res);
}