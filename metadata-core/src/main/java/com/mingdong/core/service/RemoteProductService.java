package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ApiReqInfoListDTO;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductClientDetailDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductInfoListDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ResultDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RemoteProductService
{
    /**
     * 根据条件获取客户充值记录
     */
    ListDTO<ProductRechargeInfoDTO> getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate,
            Page page);

    /**
     * 根据条件获取获取客户消费记录
     */
    ApiReqInfoListDTO getProductRequestRecord(Long clientId, Long userId, Long productId, Date fromDate, Date endDate,
            Page page);

    /**
     * 根据条件获取产品信息列表
     */
    ProductListDTO getIndexProductList(Long clientId, Integer isOpen, Integer[] selectedType, Page page);

    /**
     * 根据条件获取产品信息
     */
    ProductDTO getClientProductInfo(Long clientId, Long productId);

    ListDTO<DictDTO> getClientProductDict(Long clientId);

    /**
     * 根据合同号获取充值记录
     */
    ProductRechargeDTO getProductRechargeByContractNo(String contractNo);

    /**
     * 根据充值记录id获取充值记录
     */
    ProductRechargeDTO getProductRechargeById(Long id);

    /**
     * 根据客户产品id获取总的充值金额
     */
    BigDecimal sumAmountByClientProduct(Long clientProductId);

    /**
     * 查询产品的详细信息
     */
    ProductDTO getProductInfoData(Long productId);

    /**
     * 根据状态获取产品信息列表
     */
    ProductListDTO getProductListByStatus(Integer enabled);

    /**
     * 根据状态获取项目信息列表
     */
    ProductInfoListDTO getProductInfoList(Integer enabled, Page page);

    /**
     * 根据客户id获取客户项目详细
     */
    List<ProductClientDetailDTO> getProductInfoList(Long clientId);

    /**
     * 产品信息编辑
     */
    ResultDTO editProduct(ProductDTO productDTO);

    /**
     * 根据产品id修改产品状态
     */
    ResultDTO changeProductStatus(Long productId, Integer enabled);

    ListDTO<ProductDTO> getProductList(String keyword, Integer type, Integer custom, Integer status, Page page);

    ListDTO<ProductRechargeInfoDTO> getRechargeInfoList(String keyword, Long productId, Long managerId,
            Long rechargeType, Date fromDate, Date toDate, Page page);

    ListDTO<ProductDTO> getProductList(Long clientId, List<Integer> typeList, Integer incOpened, Page page);
}
