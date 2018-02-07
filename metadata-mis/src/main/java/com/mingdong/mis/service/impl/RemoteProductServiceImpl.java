package com.mingdong.mis.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.Custom;
import com.mingdong.core.constant.ProdType;
import com.mingdong.core.constant.ProductStatus;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.ApiReqInfoDTO;
import com.mingdong.core.model.dto.ApiReqInfoListDTO;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.NewProductDTO;
import com.mingdong.core.model.dto.ProductClientDetailDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductDictDTO;
import com.mingdong.core.model.dto.ProductInfoDTO;
import com.mingdong.core.model.dto.ProductInfoListDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.model.dto.ProductTxtDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.domain.entity.ApiReqInfo;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.DictProductType;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.entity.ProductClientInfo;
import com.mingdong.mis.domain.entity.ProductInfo;
import com.mingdong.mis.domain.entity.ProductRecharge;
import com.mingdong.mis.domain.entity.ProductRechargeInfo;
import com.mingdong.mis.domain.entity.ProductTxt;
import com.mingdong.mis.domain.entity.UserProduct;
import com.mingdong.mis.domain.mapper.ApiReqInfoMapper;
import com.mingdong.mis.domain.mapper.ApiReqMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.DictProductTypeMapper;
import com.mingdong.mis.domain.mapper.ProductClientInfoMapper;
import com.mingdong.mis.domain.mapper.ProductInfoMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeMapper;
import com.mingdong.mis.domain.mapper.ProductTxtMapper;
import com.mingdong.mis.domain.mapper.UserProductMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteProductServiceImpl implements RemoteProductService
{
    @Resource
    private ProductRechargeMapper productRechargeMapper;
    @Resource
    private ProductRechargeInfoMapper productRechargeInfoMapper;
    @Resource
    private ProductClientInfoMapper productClientInfoMapper;
    @Resource
    private ApiReqMapper apiReqMapper;
    @Resource
    private ApiReqInfoMapper apiReqInfoMapper;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private DictProductTypeMapper dictProductTypeMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductTxtMapper productTxtMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private UserProductMapper userProductMapper;
    @Resource
    private RedisDao redisDao;

    @Override
    public ProductRechargeInfoListDTO getProductRechargeRecord(Long clientId, Long productId, Date fromDate,
            Date endDate, Page page)
    {
        ProductRechargeInfoListDTO productRecListDTO = new ProductRechargeInfoListDTO();
        List<ProductRechargeInfoDTO> dataDtoList = new ArrayList<>();
        productRecListDTO.setDataList(dataDtoList);
        if(page == null)
        {
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, fromDate,
                    endDate);
            if(CollectionUtils.isNotEmpty(dataList))
            {
                ProductRechargeInfoDTO productRechargeDTO;
                for(ProductRechargeInfo item : dataList)
                {
                    productRechargeDTO = new ProductRechargeInfoDTO();
                    dataDtoList.add(productRechargeDTO);
                    EntityUtils.copyProperties(item, productRechargeDTO);
                }
            }
        }
        else
        {
            int total = productRechargeMapper.countBy(clientId, productId, fromDate, endDate);
            int pages = page.getTotalPage(total);
            productRecListDTO.setTotal(total);
            productRecListDTO.setPages(pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, fromDate,
                        endDate);
                ProductRechargeInfoDTO productRechargeDTO;
                for(ProductRechargeInfo item : dataList)
                {
                    productRechargeDTO = new ProductRechargeInfoDTO();
                    dataDtoList.add(productRechargeDTO);
                    EntityUtils.copyProperties(item, productRechargeDTO);
                }
            }
        }
        return productRecListDTO;
    }

    @Override
    public ApiReqInfoListDTO getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate,
            Page page)
    {
        ApiReqInfoListDTO apiReqInfoListDTO = new ApiReqInfoListDTO();
        List<ApiReqInfoDTO> apiReqInfoDTOList = new ArrayList<>();
        apiReqInfoListDTO.setDataList(apiReqInfoDTOList);
        ApiReqInfoDTO apiReqInfoDTO;
        if(page == null)
        {
            List<ApiReqInfo> apiReqInfoList = apiReqInfoMapper.getListBy(clientId, productId, fromDate, endDate);
            if(CollectionUtils.isNotEmpty(apiReqInfoList))
            {
                for(ApiReqInfo item : apiReqInfoList)
                {
                    apiReqInfoDTO = new ApiReqInfoDTO();
                    EntityUtils.copyProperties(item, apiReqInfoDTO);
                    apiReqInfoDTOList.add(apiReqInfoDTO);
                }
            }
        }
        else
        {
            int total = apiReqMapper.countBy(clientId, productId, fromDate, endDate);
            int pages = page.getTotalPage(total);
            apiReqInfoListDTO.setPages(pages);
            apiReqInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ApiReqInfo> apiReqInfoList = apiReqInfoMapper.getListBy(clientId, productId, fromDate, endDate);
                if(CollectionUtils.isNotEmpty(apiReqInfoList))
                {
                    for(ApiReqInfo item : apiReqInfoList)
                    {
                        apiReqInfoDTO = new ApiReqInfoDTO();
                        apiReqInfoDTOList.add(apiReqInfoDTO);
                        EntityUtils.copyProperties(item, apiReqInfoDTO);
                    }
                }
            }
        }
        return apiReqInfoListDTO;

    }

    @Override
    public ProductListDTO getIndexProductList(Long clientId, Integer isOpen, Integer[] selectedType, Page page)
    {
        ProductListDTO dto = new ProductListDTO();
        List<ProductClientInfo> dataList;
        if(page == null)
        {
            dataList = productClientInfoMapper.getInfoListBy(clientId, isOpen, selectedType);
        }
        else
        {
            int total = productClientInfoMapper.countInfoListBy(clientId, isOpen, selectedType);
            int pages = page.getTotalPage(total);
            dto.setTotal(total);
            dto.setPages(pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                dataList = productClientInfoMapper.getInfoListBy(clientId, isOpen, selectedType);
            }
            else
            {
                dataList = new ArrayList<>();
            }
        }
        List<ProductDTO> opened = new ArrayList<>();
        List<ProductDTO> toOpen = new ArrayList<>();
        for(ProductClientInfo info : dataList)
        {
            if(info.getClientProductId() != null && TrueOrFalse.TRUE.equals(info.getIsOpened()))
            {
                if(opened.size() < Constant.HOME_PRODUCT_QTY)
                {
                    ProductDTO d = new ProductDTO();
                    d.setId(info.getProductId());
                    d.setName(info.getProductName());
                    d.setCode(info.getCode());
                    d.setType(info.getType());
                    d.setTypeName(info.getTypeName());
                    d.setBillPlan(info.getBillPlan());
                    if(BillPlan.BY_TIME.getId().equals(info.getBillPlan()))
                    {
                        d.setStatus(ProductStatus.getStatusByDate(info.getStartDate(), info.getEndDate()));
                        d.setFromDate(info.getStartDate());
                        d.setToDate(info.getEndDate());
                    }
                    else
                    {
                        d.setStatus(ProductStatus.getStatusByBalance(info.getUnitAmt(), info.getBalance()));
                        d.setCostAmt(info.getUnitAmt());
                        d.setBalance(info.getBalance());
                        d.setArrearTime(info.getArrearTime());
                    }
                    opened.add(d);
                }
            }
            else
            {
                if(toOpen.size() < Constant.HOME_PRODUCT_QTY)
                {
                    ProductDTO d = new ProductDTO();
                    d.setId(info.getProductId());
                    d.setName(info.getProductName());
                    d.setRemark(info.getRemark());
                    d.setCode(info.getCode());
                    d.setTypeName(info.getTypeName());
                    d.setType(info.getType());
                    toOpen.add(d);
                }
            }
        }
        dto.setOpened(opened);
        dto.setToOpen(toOpen);
        return dto;
    }

    @Override
    public ProductDTO getClientProductDetail(Long clientId, Long productId)
    {
        ProductDTO dto = new ProductDTO();
        ClientProduct clientProduct = clientProductMapper.findByClientAndProduct(clientId, productId);
        if(clientProduct == null)
        {
            dto.setErrCode(RestResult.PRODUCT_NOT_OPEN.getCode());
            return dto;
        }
        ProductClientInfo info = productClientInfoMapper.getClientProductInfo(clientProduct.getId());
        dto.setId(info.getProductId());
        dto.setName(info.getProductName());
        dto.setContent(info.getContent());
        dto.setBillPlan(info.getBillPlan());
        if(BillPlan.BY_TIME.getId().equals(info.getBillPlan()))
        {
            dto.setFromDate(info.getStartDate());
            dto.setToDate(info.getEndDate());
        }
        else
        {
            dto.setCostAmt(info.getUnitAmt());
            dto.setBalance(info.getBalance());
        }
        return dto;
    }

    @Override
    public ProductDictDTO getClientProductDictDTO(Long clientId)
    {
        ProductDictDTO dto = new ProductDictDTO();
        List<ProductClientInfo> dataList = productClientInfoMapper.getClientDictList(clientId);
        List<DictDTO> dictList = new ArrayList<>();
        for(ProductClientInfo o : dataList)
        {
            dictList.add(new DictDTO(o.getProductId() + "", o.getProductName()));
        }
        dto.setProductDictList(dictList);
        return dto;
    }

    /*@Override
    public DictProductTypeListDTO getDictProductTypeList(Integer enabled, Page page)
    {

        DictProductTypeListDTO dictProductTypeListDTO = new DictProductTypeListDTO();
        List<DictProductTypeDTO> dataDtoList = new ArrayList<>();
        dictProductTypeListDTO.setDataList(dataDtoList);
        DictProductTypeDTO dictProductTypeDTO;
        if(page == null)
        {
            List<DictProductType> dataList = dictProductTypeMapper.getListByStatus(enabled);
            if(CollectionUtils.isNotEmpty(dataList))
            {
                for(DictProductType item : dataList)
                {
                    dictProductTypeDTO = new DictProductTypeDTO();
                    dataDtoList.add(dictProductTypeDTO);
                    EntityUtils.copyProperties(item, dictProductTypeDTO);
                }
            }
        }
        else
        {
            int total = dictProductTypeMapper.countListByStatus(enabled);
            int pages = page.getTotalPage(total);
            dictProductTypeListDTO.setTotal(total);
            dictProductTypeListDTO.setPages(pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<DictProductType> dataList = dictProductTypeMapper.getListByStatus(enabled);
                if(CollectionUtils.isNotEmpty(dataList))
                {
                    for(DictProductType item : dataList)
                    {
                        dictProductTypeDTO = new DictProductTypeDTO();
                        EntityUtils.copyProperties(item, dictProductTypeDTO);
                        dataDtoList.add(dictProductTypeDTO);
                    }
                }
            }
        }
        return dictProductTypeListDTO;
    }*/

    @Override
    public ProductRechargeDTO getProductRechargeByContractNo(String contractNo)
    {
        ProductRechargeDTO productRechargeDTO = new ProductRechargeDTO();
        ProductRecharge pro = productRechargeMapper.findByContractNo(contractNo);
        if(pro == null)
        {
            return null;
        }
        EntityUtils.copyProperties(pro, productRechargeDTO);
        return productRechargeDTO;
    }

    @Override
    public ProductRechargeDTO getProductRechargeById(Long id)
    {
        ProductRechargeDTO productRechargeDTO = new ProductRechargeDTO();
        ProductRecharge pr = productRechargeMapper.findById(id);
        if(pr == null)
        {
            return null;
        }
        EntityUtils.copyProperties(pr, productRechargeDTO);
        return productRechargeDTO;
    }

    @Override
    public BigDecimal sumAmountByClientProduct(Long clientProductId)
    {
        return productRechargeMapper.sumAmountByClientProduct(clientProductId);
    }

    @Override
    public ProductRechargeInfoListDTO getProductRechargeInfoList(Long clientId, Long productId, Date startTime,
            Date endTime, Page page)
    {
        ProductRechargeInfoListDTO productRechargeInfoListDTO = new ProductRechargeInfoListDTO();
        List<ProductRechargeInfoDTO> dataList = new ArrayList<>();
        productRechargeInfoListDTO.setDataList(dataList);
        ProductRechargeInfoDTO clientOperateInfoDTO;
        if(page == null)
        {
            List<ProductRechargeInfo> productRechargeInfos = productRechargeInfoMapper.getListBy(clientId, productId,
                    startTime, endTime);
            if(CollectionUtils.isNotEmpty(productRechargeInfos))
            {
                for(ProductRechargeInfo item : productRechargeInfos)
                {
                    clientOperateInfoDTO = new ProductRechargeInfoDTO();
                    EntityUtils.copyProperties(item, clientOperateInfoDTO);
                    dataList.add(clientOperateInfoDTO);
                }
            }
        }
        else
        {
            int total = productRechargeInfoMapper.countBy(clientId, productId, startTime, endTime);
            int pages = page.getTotalPage(total);
            productRechargeInfoListDTO.setPages(pages);
            productRechargeInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ProductRechargeInfo> productRechargeInfos = productRechargeInfoMapper.getListBy(clientId,
                        productId, startTime, endTime);
                if(CollectionUtils.isNotEmpty(productRechargeInfos))
                {
                    for(ProductRechargeInfo item : productRechargeInfos)
                    {
                        clientOperateInfoDTO = new ProductRechargeInfoDTO();
                        dataList.add(clientOperateInfoDTO);
                        EntityUtils.copyProperties(item, clientOperateInfoDTO);
                    }
                }
            }
        }
        return productRechargeInfoListDTO;
    }

    /*@Override
    public DictProductTypeDTO getDictProductTypeById(Long id)
    {
        DictProductTypeDTO dictProductTypeDTO = new DictProductTypeDTO();
        DictProductType type = dictProductTypeMapper.findById(id);
        if(type == null)
        {
            return null;
        }
        EntityUtils.copyProperties(type, dictProductTypeDTO);
        return dictProductTypeDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateDictProductTypeSkipNull(DictProductTypeDTO dictProductTypeDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictProductType type = dictProductTypeMapper.findById(dictProductTypeDTO.getId());
        if(type == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        dictProductTypeDTO.setEnabled(type.getEnabled());
        type = dictProductTypeMapper.findByCode(dictProductTypeDTO.getCode());
        if(type != null && !dictProductTypeDTO.getId().equals(type.getId()))
        {
            resultDTO.setResult(RestResult.CATEGORY_CODE_EXIST);
            return resultDTO;
        }
        type = new DictProductType();
        EntityUtils.copyProperties(dictProductTypeDTO, type);
        dictProductTypeMapper.updateSkipNull(type);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }*/

    @Override
    public ProductDTO getProductById(Long productId)
    {
        ProductDTO productDTO = new ProductDTO();
        Product product = productMapper.findById(productId);
        if(product == null)
        {
            return null;
        }
        EntityUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    @Override
    public ProductTxtDTO getProductTxtById(Long productId)
    {
        ProductTxtDTO productTxtDTO = new ProductTxtDTO();
        ProductTxt productTxt = productTxtMapper.findById(productId);
        if(productTxt == null)
        {
            return null;
        }
        EntityUtils.copyProperties(productTxt, productTxtDTO);
        return productTxtDTO;
    }

    @Override
    public ProductListDTO getProductListByStatus(Integer enabled)
    {
        ProductListDTO productListDTO = new ProductListDTO();
        List<ProductDTO> dataList = new ArrayList<>();
        productListDTO.setDataList(dataList);
        ProductDTO productDTO;
        List<Product> productList = productMapper.getListByStatus(enabled);
        if(CollectionUtils.isNotEmpty(productList))
        {
            for(Product item : productList)
            {
                productDTO = new ProductDTO();
                EntityUtils.copyProperties(item, productDTO);
                dataList.add(productDTO);
            }
        }
        return productListDTO;
    }

    @Override
    public ProductInfoListDTO getProductInfoList(Integer enabled, Page page)
    {
        ProductInfoListDTO productInfoListDTO = new ProductInfoListDTO();
        List<ProductInfoDTO> dataList = new ArrayList<>();
        productInfoListDTO.setDataList(dataList);
        ProductInfoDTO productInfoDTO;
        if(page == null)
        {
            List<ProductInfo> productInfoList = productInfoMapper.getListByEnabled(enabled);
            if(CollectionUtils.isNotEmpty(productInfoList))
            {
                for(ProductInfo item : productInfoList)
                {
                    productInfoDTO = new ProductInfoDTO();
                    EntityUtils.copyProperties(item, productInfoDTO);
                    productInfoDTO.setCustomName(Custom.getById(productInfoDTO.getCustom()).getName());
                    productInfoDTO.setTypeName(ProdType.getById(productInfoDTO.getType()).getName());
                    dataList.add(productInfoDTO);
                }
            }
        }
        else
        {
            int total = productInfoMapper.countByEnabled(enabled);
            int pages = page.getTotalPage(total);
            productInfoListDTO.setPages(pages);
            productInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ProductInfo> productInfoList = productInfoMapper.getListByEnabled(enabled);
                if(CollectionUtils.isNotEmpty(productInfoList))
                {
                    for(ProductInfo item : productInfoList)
                    {
                        productInfoDTO = new ProductInfoDTO();
                        dataList.add(productInfoDTO);
                        EntityUtils.copyProperties(item, productInfoDTO);
                        productInfoDTO.setCustomName(Custom.getById(productInfoDTO.getCustom()).getName());
                        productInfoDTO.setTypeName(ProdType.getById(productInfoDTO.getType()).getName());
                    }
                }
            }
        }
        return productInfoListDTO;
    }

    @Override
    public ProductRechargeInfoListDTO getProductRechargeInfoListBy(String shortName, Long typeId, Long productId,
            Long managerId, Date startDate, Date endDate, Page page)
    {
        ProductRechargeInfoListDTO productRechargeInfoListDTO = new ProductRechargeInfoListDTO();
        List<ProductRechargeInfoDTO> dataList = new ArrayList<>();
        productRechargeInfoListDTO.setDataList(dataList);
        ProductRechargeInfoDTO productRechargeInfoDTO;
        if(page == null)
        {
            List<ProductRechargeInfo> productRechargeInfoList = productRechargeInfoMapper.getProductRechargeInfoListBy(
                    shortName, typeId, productId, managerId, startDate, endDate);
            for(ProductRechargeInfo item : productRechargeInfoList)
            {
                productRechargeInfoDTO = new ProductRechargeInfoDTO();
                EntityUtils.copyProperties(item, productRechargeInfoDTO);
                dataList.add(productRechargeInfoDTO);
            }
        }
        else
        {
            int total = productRechargeInfoMapper.countProductRechargeInfoBy(shortName, typeId, productId, managerId,
                    startDate, endDate);
            int pages = page.getTotalPage(total);
            productRechargeInfoListDTO.setPages(pages);
            productRechargeInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ProductRechargeInfo> productRechargeInfoList =
                        productRechargeInfoMapper.getProductRechargeInfoListBy(shortName, typeId, productId, managerId,
                                startDate, endDate);
                for(ProductRechargeInfo item : productRechargeInfoList)
                {
                    productRechargeInfoDTO = new ProductRechargeInfoDTO();
                    EntityUtils.copyProperties(item, productRechargeInfoDTO);
                    dataList.add(productRechargeInfoDTO);
                }
            }
        }
        return productRechargeInfoListDTO;
    }

    @Override
    public BigDecimal getProductRechargeInfoSumBy(String shortName, Long typeId, Long productId, Long managerId,
            Date startDate, Date endDate)
    {
        return productRechargeInfoMapper.getProductRechargeInfoSumBy(shortName, typeId, productId, managerId, startDate,
                endDate);
    }

    @Override
    public List<ProductClientDetailDTO> getProductInfoList(Long clientId)
    {
        List<ProductClientDetailDTO> list = new ArrayList<>();
        List<ProductClientInfo> dataList = productClientInfoMapper.getListByClient(clientId);
        for(ProductClientInfo d : dataList)
        {
            ProductClientDetailDTO o = new ProductClientDetailDTO();
            o.setProductId(d.getProductId());
            o.setName(d.getProductName());
            o.setCustom(d.getCustom());
            o.setProductType(d.getType());
            o.setRemark(d.getRemark());
            o.setCode(d.getCode());
            if(d.getClientProductId() != null)
            {
                o.setIsOpened(d.getIsOpened());
                o.setClientProductId(d.getClientProductId());
                o.setAppId(d.getAppId());
                o.setBillPlan(d.getBillPlan());
                if(BillPlan.BY_TIME.getId().equals(d.getBillPlan()))
                {
                    o.setFromDate(d.getStartDate());
                    o.setToDate(d.getEndDate());
                    o.setAmount(d.getAmount());
                }
                else
                {
                    o.setUnitAmt(d.getUnitAmt());
                    o.setBalance(d.getBalance());
                }
            }
            list.add(o);
        }
        return list;
    }

    /*@Override
    @Transactional
    public ResultDTO addProductType(DictProductTypeDTO dictProductTypeDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictProductType type = dictProductTypeMapper.findByCode(dictProductTypeDTO.getCode());
        if(type != null)
        {
            resultDTO.setResult(RestResult.CATEGORY_CODE_EXIST);
            return resultDTO;
        }
        if(dictProductTypeDTO.getCode() == null)
        {
            resultDTO.setResult(RestResult.INVALID_PRODUCT_TYPE);
            return resultDTO;
        }
        if(dictProductTypeDTO.getName() == null)
        {
            resultDTO.setResult(RestResult.INVALID_PRODUCT_NAME);
            return resultDTO;
        }
        type = new DictProductType();
        EntityUtils.copyProperties(dictProductTypeDTO, type);
        dictProductTypeMapper.add(type);
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }*/

    @Override
    @Transactional
    public ResultDTO addProduct(NewProductDTO newProductDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        // 1. 校验产品类型是否有效
        DictProductType type = dictProductTypeMapper.findById(newProductDTO.getProductDTO().getType().longValue());
        if(type == null)
        {
            resultDTO.setResult(RestResult.INVALID_PRODUCT_TYPE);
            return resultDTO;
        }
        // 2. 校验产品编码是否重复
        Product product = productMapper.findByCode(newProductDTO.getProductDTO().getCode());
        if(product != null)
        {
            resultDTO.setResult(RestResult.DUPLICATE_PRODUCT_CODE);
            return resultDTO;
        }
        // 3. 校验产品名是否重复
        product = productMapper.findByName(newProductDTO.getProductDTO().getName());
        if(product != null)
        {
            resultDTO.setResult(RestResult.PRODUCT_NAME_EXIST);
            return resultDTO;
        }
        product = new Product();
        EntityUtils.copyProperties(newProductDTO.getProductDTO(), product);
        productMapper.add(product);
        if(newProductDTO.getProductTxtDTO() != null)
        {
            ProductTxt productTxt = new ProductTxt();
            EntityUtils.copyProperties(newProductDTO.getProductTxtDTO(), productTxt);
            productTxtMapper.add(productTxt);
        }
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    @Override
    @Transactional
    public ResultDTO updateProductSkipNull(NewProductDTO newProductDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Product product = productMapper.findByCode(newProductDTO.getProductDTO().getCode());
        if(product != null && !newProductDTO.getProductDTO().getId().equals(product.getId()))
        {
            resultDTO.setResult(RestResult.DUPLICATE_PRODUCT_CODE);
            return resultDTO;
        }
        product = productMapper.findById(newProductDTO.getProductDTO().getId());
        if(product == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        Product updateProduct = new Product();
        EntityUtils.copyProperties(newProductDTO.getProductDTO(), updateProduct);
        productMapper.updateSkipNull(updateProduct);
        ProductTxt productTxt = productTxtMapper.findById(newProductDTO.getProductDTO().getId());
        if(productTxt == null)
        {
            productTxt = new ProductTxt();
            EntityUtils.copyProperties(newProductDTO.getProductTxtDTO(), productTxt);
            productTxtMapper.add(productTxt);
        }
        else
        {
            productTxt.setUpdateTime(new Date());
            productTxt.setContent(newProductDTO.getProductTxtDTO().getContent());
            productTxtMapper.updateById(productTxt);
        }
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

    /*@Override
    @Transactional
    public ResultDTO updateDictProductTypeStatusById(Long id, Integer enabled)
    {
        ResultDTO resultDTO = new ResultDTO();
        DictProductType type = dictProductTypeMapper.findById(id);
        if(type == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        if(enabled.equals(type.getEnabled()))
        {
            type.setEnabled(TrueOrFalse.TRUE.equals(enabled) ? TrueOrFalse.FALSE : TrueOrFalse.TRUE);
            type.setUpdateTime(new Date());
            dictProductTypeMapper.updateById(type);
        }
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }*/

    @Override
    @Transactional
    public ResultDTO updateProductStatusById(Long id, Integer enabled)
    {
        ResultDTO resultDTO = new ResultDTO();
        Product product = productMapper.findById(id);
        if(product == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        if(enabled.equals(product.getEnabled()))
        {
            product.setEnabled(TrueOrFalse.TRUE.equals(enabled) ? TrueOrFalse.FALSE : TrueOrFalse.TRUE);
            product.setUpdateTime(new Date());
            productMapper.updateById(product);
        }
        List<UserProduct> userProductList = userProductMapper.findListBy(null, id);
        if(CollectionUtils.isNotEmpty(userProductList)){
            List<Long> ids  = new ArrayList<>();
            List<String> removeToken = new ArrayList<>();
            for(UserProduct item : userProductList){
                if(StringUtils.isNotEmpty(item.getAccessToken())){
                    removeToken.add(item.getAccessToken());
                    ids.add(item.getId());
                }
            }
            if(CollectionUtils.isNotEmpty(ids)){
                userProductMapper.updateToken(new Date(),null,ids);
                for(String item : removeToken){
                    redisDao.dropUserAuth(item);
                }
            }
        }
        resultDTO.setResult(RestResult.SUCCESS);
        return resultDTO;
    }

}

