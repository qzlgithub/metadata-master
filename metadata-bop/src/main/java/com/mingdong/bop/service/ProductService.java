package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService
{
    /**
     * 获取产品类型信息列表
     *
     * @param page
     * @return
     */
    BLResp getProdCategory(Page page);

    /**
     * 新增产品类型信息
     *
     * @param code
     * @param name
     * @param remark
     * @param resp
     */
    void addProdCategory(String code, String name, String remark, BLResp resp);

    /**
     * 修改产品类型信息
     *
     * @param productId
     * @param code
     * @param name
     * @param remark
     * @return
     */
    BLResp editProdCategory(Long productId, String code, String name, String remark);

    /**
     * 根据产品id获取产品信息
     *
     * @param productId
     * @return
     */
    Map<String, Object> getProductInfo(Long productId);

    /**
     * 新增产品
     *  @param productType
     * @param code
     * @param name
     * @param costAmt
     * @param enabled
     * @param custom
     * @param remark
     * @param content
     * @param resp
     */
    void addProduct(Integer productType, String code, String name, BigDecimal costAmt, Integer enabled, Integer custom,
            String remark, String content, BLResp resp);

    /**
     * 修改产品
     *  @param id
     * @param productType
     * @param code
     * @param name
     * @param costAmt
     * @param enabled
     * @param custom
     * @param remark
     * @param content
     * @param resp
     */
    void editProduct(Long id, Integer productType, String code, String name, BigDecimal costAmt, Integer enabled,
            Integer custom, String remark, String content, BLResp resp);

    /**
     * 根据产品类型id获取产品类型信息
     *
     * @param id
     * @return
     */
    BLResp getProductCategoryInfo(Long id);

    /**
     * 更改产品类型信息
     *
     * @param id
     * @param enabled
     * @param resp
     */
    void updateCateStatus(Long id, Integer enabled, BLResp resp);

    /**
     * 更改产品信息
     *
     * @param id
     * @param enabled
     * @param resp
     */
    void updateProdStatus(Long id, Integer enabled, BLResp resp);

    /**
     * 获取产品信息列表
     *
     * @return
     */
    List<Map<String, Object>> getProductDict();

    /**
     * 获取产品类型信息列表
     *
     * @param enabled
     * @return
     */
    List<Map<String, Object>> getProductTypeDict(Integer enabled);

    /**
     * 获取产品信息列表
     *
     * @param page
     * @return
     */
    BLResp getProductInfoList(Page page);

    /**
     * 根据状态获取产品信息列表
     *
     * @param enabled
     * @return
     */
    List<Map<String, Object>> getProductInfoListMap(Integer enabled);
}