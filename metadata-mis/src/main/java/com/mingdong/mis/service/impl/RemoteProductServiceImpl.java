package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.NumberUtils;
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
import com.mingdong.core.model.dto.ListDTO;
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
import com.mingdong.mis.constant.Field;
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
            if(!CollectionUtils.isEmpty(dataList))
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
    public ApiReqInfoListDTO getProductRequestRecord(Long clientId, Long userId, Long productId, Date fromDate,
            Date endDate, Page page)
    {
        ApiReqInfoListDTO apiReqInfoListDTO = new ApiReqInfoListDTO();
        List<ApiReqInfoDTO> apiReqInfoDTOList = new ArrayList<>();
        apiReqInfoListDTO.setDataList(apiReqInfoDTOList);
        ApiReqInfoDTO apiReqInfoDTO;
        if(page == null)
        {
            List<ApiReqInfo> apiReqInfoList = apiReqInfoMapper.getListBy(clientId, userId, productId, fromDate,
                    endDate);
            if(!CollectionUtils.isEmpty(apiReqInfoList))
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
            int total = apiReqMapper.countBy(clientId, userId, productId, fromDate, endDate);
            int pages = page.getTotalPage(total);
            apiReqInfoListDTO.setPages(pages);
            apiReqInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ApiReqInfo> apiReqInfoList = apiReqInfoMapper.getListBy(clientId, userId, productId, fromDate,
                        endDate);
                if(!CollectionUtils.isEmpty(apiReqInfoList))
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
            if(info.getClientProductId() != null && TrueOrFalse.TRUE.equals(info.getOpened()))
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
            if(!CollectionUtils.isEmpty(productRechargeInfos))
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
                if(!CollectionUtils.isEmpty(productRechargeInfos))
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
        if(!CollectionUtils.isEmpty(productList))
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
            if(!CollectionUtils.isEmpty(productInfoList))
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
                if(!CollectionUtils.isEmpty(productInfoList))
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
                o.setIsOpened(d.getOpened());
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

    @Override
    @Transactional
    public ResultDTO changeProductStatus(Long productId, Integer enabled)
    {
        ResultDTO dto = new ResultDTO();
        Product product = productMapper.findById(productId);
        if(product == null)
        {
            dto.setResult(RestResult.OBJECT_NOT_FOUND);
            return dto;
        }
        if(enabled.equals(product.getEnabled()))
        {
            return dto;
        }
        // 更新产品状态
        product.setEnabled(enabled);
        product.setUpdateTime(new Date());
        productMapper.updateById(product);
        // 如果设置为禁用则需清空客户关于该产品的请求凭证
        if(!TrueOrFalse.TRUE.equals(enabled))
        {
            List<UserProduct> userProductList = userProductMapper.getListByProduct(productId);
            if(!CollectionUtils.isEmpty(userProductList))
            {
                List<Long> idList = new ArrayList<>();
                List<String> tokenList = new ArrayList<>();
                for(UserProduct o : userProductList)
                {
                    if(o.getAccessToken() != null)
                    {
                        idList.add(o.getId());
                        tokenList.add(o.getAccessToken());
                    }
                }
                if(idList.size() > 0)
                {
                    userProductMapper.clearAccessToken(new Date(), idList);
                    redisDao.dropUserAuth(tokenList.toArray(new String[tokenList.size()]));
                }
            }
        }
        return dto;
    }

    @Override
    public ListDTO<ProductDTO> getProductList(String keyword, Integer type, Integer custom, Integer status, Page page)
    {
        ListDTO<ProductDTO> dto = new ListDTO<>();
        int total = productMapper.countBy(keyword, type, custom, status);
        int pages = page.getTotalPage(total);
        dto.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<Product> dataList = productMapper.getListBy(keyword, type, custom, status);
            List<ProductDTO> list = new ArrayList<>(dataList.size());
            for(Product o : dataList)
            {
                ProductDTO pd = new ProductDTO();
                pd.setId(o.getId());
                pd.setCreateTime(o.getCreateTime());
                pd.setCode(o.getCode());
                pd.setName(o.getName());
                pd.setType(o.getType());
                pd.setCustom(o.getCustom());
                pd.setCostAmt(o.getCostAmt());
                pd.setRemark(o.getRemark());
                pd.setEnabled(o.getEnabled());
                list.add(pd);
            }
            dto.setList(list);
        }
        return dto;
    }

    @Override
    public ListDTO<ProductRechargeInfoDTO> getRechargeInfoList(String keyword, Long productId, Long managerId,
            Long rechargeType, Date fromDate, Date toDate, Page page)
    {
        ListDTO<ProductRechargeInfoDTO> dto = new ListDTO<>();
        int total = productRechargeInfoMapper.countBy1(keyword, productId, managerId, rechargeType, fromDate, toDate);
        int pages = page.getTotalPage(total);
        dto.setTotal(total);
        BigDecimal totalAmt = productRechargeInfoMapper.sumRechargeAmountBy(keyword, productId, managerId, rechargeType,
                fromDate, toDate);
        dto.addExtra(Field.TOTAL_AMT, NumberUtils.formatAmount(totalAmt));
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy1(keyword, productId, managerId,
                    rechargeType, fromDate, toDate);
            List<ProductRechargeInfoDTO> list = new ArrayList<>(dataList.size());
            for(ProductRechargeInfo o : dataList)
            {
                ProductRechargeInfoDTO pri = new ProductRechargeInfoDTO();
                pri.setTradeTime(o.getTradeTime());
                pri.setTradeNo(o.getTradeNo());
                pri.setCorpName(o.getCorpName());
                pri.setShortName(o.getShortName());
                pri.setUsername(o.getUsername());
                pri.setProductName(o.getProductName());
                pri.setRechargeType(o.getRechargeType());
                pri.setAmount(o.getAmount());
                pri.setBalance(o.getBalance());
                pri.setManagerName(o.getManagerName());
                pri.setContractNo(o.getContractNo());
                pri.setRemark(o.getRemark());
                list.add(pri);
            }
            dto.setList(list);
        }
        return dto;
    }

    @Override
    public ListDTO<ProductDTO> getProductList(Long clientId, List<Integer> typeList, Integer incOpened, Page page)
    {
        ListDTO<ProductDTO> dto = new ListDTO<>();
        incOpened = TrueOrFalse.TRUE.equals(incOpened) ? incOpened : null;
        int total = productClientInfoMapper.countBy(clientId, typeList, incOpened);
        int pages = page.getTotalPage(total);
        dto.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductClientInfo> dataList = productClientInfoMapper.getListBy(clientId, typeList, incOpened);
            List<ProductDTO> list = new ArrayList<>(dataList.size());
            for(ProductClientInfo o : dataList)
            {
                ProductDTO p = new ProductDTO();
                p.setId(o.getProductId());
                p.setName(o.getProductName());
                if(o.getClientProductId() != null)
                {
                    if(TrueOrFalse.TRUE.equals(o.getOpened()))
                    {
                        p.setOpened(TrueOrFalse.TRUE);
                        p.setBillPlan(o.getBillPlan());
                        if(BillPlan.BY_TIME.getId().equals(o.getBillPlan()))
                        {
                            p.setFromDate(o.getStartDate());
                            p.setToDate(o.getEndDate());
                        }
                        else
                        {
                            p.setCostAmt(o.getUnitAmt());
                            p.setBalance(o.getBalance());
                        }
                    }
                    else
                    {
                        p.setOpened(TrueOrFalse.FALSE);
                        p.setRemark(o.getRemark());
                    }
                }
                else
                {
                    p.setOpened(TrueOrFalse.FALSE);
                    p.setRemark(o.getRemark());
                }
                list.add(p);
            }
            dto.setList(list);
        }
        return dto;
    }
}
