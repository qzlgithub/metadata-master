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
     *
     * @param clientId
     * @param isOpen
     * @param selectedType
     * @param page
     * @return
     */
    ProductListDTO getIndexProductList(Long clientId, Integer isOpen, Integer[] selectedType, Page page);

    /**
     * 根据条件获取产品信息
     *
     * @param clientId
     * @param productId
     * @return
     */
    ProductDTO getClientProductDetail(Long clientId, Long productId);

    /**
     * 根据客户id获取客户产品
     *
     * @param clientId
     * @return
     */
    ProductDictDTO getClientProductDictDTO(Long clientId);

    /**
     * 根据条件获取产品类别字典
     *
     * @param enabled
     * @param page
     * @return
     */
    //    DictProductTypeListDTO getDictProductTypeList(Integer enabled, Page page);

    /**
     * 根据合同号获取充值记录
     *
     * @param contractNo
     * @return
     */
    ProductRechargeDTO getProductRechargeByContractNo(String contractNo);

    /**
     * 根据充值记录id获取充值记录
     *
     * @param id
     * @return
     */
    ProductRechargeDTO getProductRechargeById(Long id);

    /**
     * 根据客户产品id获取总的充值金额
     *
     * @param clientProductId
     * @return
     */
    BigDecimal sumAmountByClientProduct(Long clientProductId);

    /**
     * 根据条件获取产品充值记录列表
     *
     * @param clientId
     * @param productId
     * @param startTime
     * @param endTime
     * @param page
     * @return
     */
    ProductRechargeInfoListDTO getProductRechargeInfoList(Long clientId, Long productId, Date startTime, Date endTime,
            Page page);

    /**
     * 根据产品类型id获取产品类型
     *
     * @param id
     * @return
     */
    //    DictProductTypeDTO getDictProductTypeById(Long id);

    /**
     * 更改产品类型字典，null值不修改
     *
     * @param type
     * @return
     */
    //    ResultDTO updateDictProductTypeSkipNull(DictProductTypeDTO type);

    /**
     * 根据产品id获取产品信息
     *
     * @param productId
     * @return
     */
    ProductDTO getProductById(Long productId);

    /**
     * 根据产品id获取产品txt
     *
     * @param productId
     * @return
     */
    ProductTxtDTO getProductTxtById(Long productId);

    /**
     * 根据状态获取产品信息列表
     *
     * @param enabled
     * @return
     */
    ProductListDTO getProductListByStatus(Integer enabled);

    /**
     * 根据状态获取项目信息列表
     *
     * @param enabled
     * @param page
     * @return
     */
    ProductInfoListDTO getProductInfoList(Integer enabled, Page page);

    /**
     * 根据条件获取产品充值记录列表
     *
     * @param shortName
     * @param typeId
     * @param productId
     * @param managerId
     * @param startDate
     * @param endDate
     * @param page
     * @return
     */
    ProductRechargeInfoListDTO getProductRechargeInfoListBy(String shortName, Long typeId, Long productId,
            Long managerId, Date startDate, Date endDate, Page page);

    /**
     * 根据条件获取产品充值总额
     *
     * @param shortName
     * @param typeId
     * @param productId
     * @param managerId
     * @param startDate
     * @param endDate
     * @return
     */
    BigDecimal getProductRechargeInfoSumBy(String shortName, Long typeId, Long productId, Long managerId,
            Date startDate, Date endDate);

    /**
     * 根据客户id获取客户项目详细
     *
     * @param clientId
     * @return
     */
    List<ProductClientDetailDTO> getProductInfoList(Long clientId);

    /**
     * 新增产品类型，校验
     *
     * @param type
     * @return
     */
    //    ResultDTO addProductType(DictProductTypeDTO type);

    /**
     * 新增产品，校验
     *
     * @param newProductDTO
     * @return
     */
    ResultDTO addProduct(NewProductDTO newProductDTO);

    /**
     * 修改产品，校验
     *
     * @param newProductDTO
     * @return
     */
    ResultDTO updateProductSkipNull(NewProductDTO newProductDTO);

    /**
     * 根据产品类型id修改产品类型状态
     *
     * @param id
     * @param enabled
     * @return
     */
    //    ResultDTO updateDictProductTypeStatusById(Long id, Integer enabled);

    /**
     * 根据产品id修改产品状态
     */
    ResultDTO changeProductStatus(Long productId, Integer enabled);

    ListDTO<ProductDTO> getProductList(String keyword, Integer type, Integer custom, Integer status, Page page);

    ListDTO<ProductRechargeInfoDTO> getRechargeInfoList(String keyword, Long productId, Long managerId,
            Long rechargeType, Date fromDate, Date toDate, Page page);
}
