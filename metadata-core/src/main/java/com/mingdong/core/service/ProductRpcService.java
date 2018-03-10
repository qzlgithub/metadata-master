package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.ProductReqDTO;
import com.mingdong.core.model.dto.response.ProductDetailResDTO;
import com.mingdong.core.model.dto.response.ProductRechargeResDTO;
import com.mingdong.core.model.dto.response.ProductResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;

import java.util.Date;
import java.util.List;

public interface ProductRpcService
{
    /**
     * 根据条件获取客户充值记录
     */
    ListDTO<ProductRechargeResDTO> getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate,
            Page page);

    /**
     * 获取指定客户已开通的产品服务信息列表
     */
    ListDTO<ProductResDTO> getOpenedProductList(Long clientId);

    /**
     * 获取指定客户未开通的产品服务信息列表
     */
    ListDTO<ProductResDTO> getUnopenedProductList(Long clientId);

    /**
     * 根据条件获取产品信息
     */
    ProductResDTO getClientProductInfo(Long clientId, Long productId);

    /**
     * 根据合同号获取充值记录
     */
    Integer checkIfContractExist(String contractNo);

    /**
     * 查询产品的详细信息
     */
    ProductResDTO getProductInfoData(Long productId);

    /**
     * 根据客户id获取客户项目详细
     */
    List<ProductDetailResDTO> getProductInfoList(Long clientId);

    /**
     * 产品信息编辑
     */
    ResponseDTO editProduct(ProductReqDTO reqDTO);

    /**
     * 根据产品id修改产品状态
     */
    ResponseDTO changeProductStatus(Long productId, Integer enabled);

    ListDTO<ProductResDTO> getProductList(String keyword, Integer type, Integer custom, Integer status, Page page);

    ListDTO<ProductRechargeResDTO> getRechargeInfoList(String keyword, Long productId, Long managerId,
            Long rechargeType, Date fromDate, Date toDate, Page page);

    ListDTO<ProductResDTO> getProductList(Long clientId, List<Integer> typeList, Integer incOpened, Page page);
}
