package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ApiReqInfoListDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.NewProductDTO;
import com.mingdong.core.model.dto.ProductClientDetailDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductDictDTO;
import com.mingdong.core.model.dto.ProductInfoListDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.model.dto.ProductTxtDTO;
import com.mingdong.core.model.dto.ResultDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RemoteProductService
{
    /**
     * 根据条件获取客户充值记录
     */
    ProductRechargeInfoListDTO getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate,
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
    ProductDTO getClientProductDetail(Long clientId, Long productId);

    /**
     * 根据客户id获取客户产品
     */
    ProductDictDTO getClientProductDictDTO(Long clientId);

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
     * 根据条件获取产品充值记录列表
     */
    ProductRechargeInfoListDTO getProductRechargeInfoList(Long clientId, Long productId, Date startTime, Date endTime,
            Page page);

    /**
     * 根据产品id获取产品信息
     */
    ProductDTO getProductById(Long productId);

    /**
     * 根据产品id获取产品txt
     */
    ProductTxtDTO getProductTxtById(Long productId);

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
     * 修改产品，校验
     */
    ResultDTO updateProductSkipNull(NewProductDTO newProductDTO);

    /**
     * 根据产品id修改产品状态
     */
    ResultDTO changeProductStatus(Long productId, Integer enabled);

    ListDTO<ProductDTO> getProductList(String keyword, Integer type, Integer custom, Integer status, Page page);

    ListDTO<ProductRechargeInfoDTO> getRechargeInfoList(String keyword, Long productId, Long managerId,
            Long rechargeType, Date fromDate, Date toDate, Page page);

    ListDTO<ProductDTO> getProductList(Long clientId, List<Integer> typeList, Integer incOpened, Page page);
}
