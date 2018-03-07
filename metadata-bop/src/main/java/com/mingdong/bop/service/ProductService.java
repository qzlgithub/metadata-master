package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.Dict;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService
{
    /**
     * 根据产品id获取产品信息
     */
    Map<String, Object> getProductInfoData(Long productId);

    /**
     * 修改产品
     */
    void editProduct(Long productId, String name, BigDecimal costAmt, String remark, String content, Integer enabled,
            RestResp resp);

    /**
     * 更改产品信息
     */
    void changeProductStatus(Long productId, Integer enabled, RestResp resp);

    /**
     * 获取产品信息列表
     */
    List<Dict> getProductDict();

    /**
     * 根据状态获取产品信息列表
     */
    List<Map<String, Object>> getProductInfoListMap(Integer enabled);

    void getProductList(String keyword, Integer type, Integer custom, Integer status, Page page, RestListResp res);
}