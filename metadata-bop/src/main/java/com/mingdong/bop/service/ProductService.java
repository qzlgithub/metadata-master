package com.mingdong.bop.service;

import com.mingdong.bop.model.BLResp;
import com.mingdong.common.model.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService
{
    BLResp getProdCategory(Page page);

    void addProdCategory(String code, String name, String remark, BLResp resp);

    BLResp editProdCategory(Long productId, String code, String name, String remark);

    //BLResp getProductList(Page page);

    Map getProductInfo(Long productId);

    BLResp getProductList(Page page);

    BLResp addProduct(Long typeId, String code, String name, BigDecimal costAmt, Integer enabled, String content,
            String remark);

    BLResp editProduct(Long productId, Long typeId, String code, String name, BigDecimal costAmt, String content,
            String remark, Integer enabled);

    BLResp disableProduct(Long productId, Integer enabled);

    BLResp getProductCategoryInfo(Long id);

    void updateCateStatus(Long id, Integer enabled, BLResp resp);

    void updateProdStatus(Long id, Integer enabled, BLResp resp);

    BLResp getProdCategory();

    List<Map<String, Object>> getProductDict();

    BLResp getProductInfoList(Page page);
}

