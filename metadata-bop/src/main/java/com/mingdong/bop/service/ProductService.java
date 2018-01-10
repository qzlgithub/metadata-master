package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService
{
    BLResp getProdCategory(Page page);

    void addProdCategory(String code, String name, String remark, BLResp resp);

    BLResp editProdCategory(Long productId, String code, String name, String remark);

    Map getProductInfo(Long productId);

    void addProduct(Long productType, String code, String name, BigDecimal costAmt, Integer enabled, String remark,
            String content, BLResp resp);

    void editProduct(Long id, Long productType, String code, String name, BigDecimal costAmt, Integer enabled,
            String remark, String content, BLResp resp);

    BLResp getProductCategoryInfo(Long id);

    void updateCateStatus(Long id, Integer enabled, BLResp resp);

    void updateProdStatus(Long id, Integer enabled, BLResp resp);

    List<Map<String, Object>> getProductDict();

    List<Map<String, Object>> getProductTypeDict(Integer enabled);

    BLResp getProductInfoList(Page page);
}