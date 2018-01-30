package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.DictProductTypeDTO;
import com.mingdong.core.model.dto.DictProductTypeListDTO;
import com.mingdong.core.model.dto.ProductClientDetailDTO;
import com.mingdong.core.model.dto.ProductClientInfoListDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductDictDTO;
import com.mingdong.core.model.dto.ProductInfoListDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.model.dto.ProductReqInfoListDTO;
import com.mingdong.core.model.dto.ProductTxtDTO;
import com.mingdong.core.model.dto.ResultDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RemoteProductService
{
    /**
     * 获取客户项目充值记录
     */
    ProductRechargeInfoListDTO getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate,
            Page page);

    /**
     * 获取客户请求记录
     */
    ProductReqInfoListDTO getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate,
            Page page);

    ProductListDTO getIndexProductList(Long clientId, Integer isOpen, Integer[] selectedType, Page page);

    ProductDTO getClientProductDetail(Long clientId, Long productId);

    ProductDictDTO getClientProductDictDTO(Long clientId);

    DictProductTypeListDTO getDictProductTypeList(Integer enabled, Page page);

    ProductClientInfoListDTO getProductClientInfoListByClientId(Long clientId);

    ProductRechargeDTO getProductRechargeByContractNo(String contractNo);

    ResultDTO saveProductRecharge(ProductRechargeDTO productRechargeDTO);

    ProductRechargeDTO getProductRechargeById(Long id);

    BigDecimal sumAmountByClientProduct(Long clientProductId);

    ProductRechargeInfoListDTO getProductRechargeInfoList(Long clientId, Long productId, Date startTime, Date endTime,
            Page page);

    DictProductTypeDTO getDictProductTypeByCode(String code);

    ResultDTO saveDictProductType(DictProductTypeDTO type);

    DictProductTypeDTO getDictProductTypeById(Long id);

    ResultDTO updateDictProductTypeSkipNull(DictProductTypeDTO type);

    ProductDTO getProductById(Long productId);

    ProductTxtDTO getProductTxtById(Long productId);

    ProductDTO getProductByCode(String code);

    ProductDTO getProductByName(String name);

    ResultDTO saveProductTxt(ProductTxtDTO productTxt);

    ResultDTO saveProduct(ProductDTO product);

    ResultDTO updateProductById(ProductDTO product);

    ResultDTO updateProductTxtById(ProductTxtDTO productTxt);

    ResultDTO updateDictProductTypeById(DictProductTypeDTO dictProductTypeDTO);

    ProductListDTO getProductListByStatus(Integer enabled);

    ProductInfoListDTO getProductInfoList(Integer enabled, Page page);

    ProductRechargeInfoListDTO getProductRechargeInfoListBy(String shortName, Long typeId, Long productId,
            Long managerId, Date startDate, Date endDate, Page page);

    BigDecimal getProductRechargeInfoSumBy(String shortName, Long typeId, Long productId, Long managerId,
            Date startDate, Date endDate);

    List<ProductClientDetailDTO> getProductInfoList(Long clientId);
}
