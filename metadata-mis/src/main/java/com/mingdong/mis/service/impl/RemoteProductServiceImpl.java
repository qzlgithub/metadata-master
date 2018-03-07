package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.Constant;
import com.mingdong.core.constant.Custom;
import com.mingdong.core.constant.ProdType;
import com.mingdong.core.constant.ProductStatus;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.ApiReqInfoDTO;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductClientDetailDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductInfoDTO;
import com.mingdong.core.model.dto.ProductInfoListDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ResultDTO;
import com.mingdong.core.service.RemoteProductService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.domain.entity.ApiReqInfo;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ClientUserProduct;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.entity.ProductClientInfo;
import com.mingdong.mis.domain.entity.ProductInfo;
import com.mingdong.mis.domain.entity.ProductRechargeInfo;
import com.mingdong.mis.domain.entity.ProductTxt;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.domain.mapper.ApiReqInfoMapper;
import com.mingdong.mis.domain.mapper.ApiReqMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserProductMapper;
import com.mingdong.mis.domain.mapper.ProductClientInfoMapper;
import com.mingdong.mis.domain.mapper.ProductInfoMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.mis.domain.mapper.ProductTxtMapper;
import com.mingdong.mis.domain.mapper.RechargeMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteProductServiceImpl implements RemoteProductService
{
    @Resource
    private RechargeMapper rechargeMapper;
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
    private ProductMapper productMapper;
    @Resource
    private ProductTxtMapper productTxtMapper;
    @Resource
    private ProductInfoMapper productInfoMapper;
    @Resource
    private ClientUserProductMapper clientUserProductMapper;
    @Resource
    private RedisDao redisDao;

    @Override
    public ListDTO<ProductRechargeInfoDTO> getProductRechargeRecord(Long clientId, Long productId, Date fromDate,
            Date endDate, Page page)
    {
        ListDTO<ProductRechargeInfoDTO> listDTO = new ListDTO<>();
        int total = rechargeMapper.countBy(clientId, productId, fromDate, endDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, fromDate,
                    endDate);
            List<ProductRechargeInfoDTO> list = new ArrayList<>(dataList.size());
            for(ProductRechargeInfo o : dataList)
            {
                ProductRechargeInfoDTO pri = new ProductRechargeInfoDTO();
                pri.setTradeTime(o.getTradeTime());
                pri.setTradeNo(o.getTradeNo());
                pri.setRechargeType(o.getRechargeType());
                pri.setBillPlan(o.getBillPlan());
                pri.setProductName(o.getProductName());
                pri.setAmount(o.getAmount());
                pri.setBalance(o.getBalance());
                pri.setContractNo(o.getContractNo());
                pri.setCorpName(o.getCorpName());
                pri.setShortName(o.getShortName());
                pri.setUsername(o.getUsername());
                pri.setManagerName(o.getManagerName());
                list.add(pri);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<ApiReqInfoDTO> getProductRequestRecord(Long clientId, Long userId, Long productId, Date fromDate,
            Date endDate, Page page)
    {
        ListDTO<ApiReqInfoDTO> listDTO = new ListDTO<>();
        int total = apiReqMapper.countBy(clientId, userId, productId, fromDate, endDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ApiReqInfo> apiReqInfoList = apiReqInfoMapper.getListBy(clientId, userId, productId, fromDate,
                    endDate);
            List<ApiReqInfoDTO> list = new ArrayList<>(apiReqInfoList.size());
            for(ApiReqInfo o : apiReqInfoList)
            {
                ApiReqInfoDTO ari = new ApiReqInfoDTO();
                ari.setId(o.getId());
                ari.setCreateTime(o.getCreateTime());
                ari.setRequestNo(o.getRequestNo());
                ari.setProductName(o.getProductName());
                ari.setBillPlan(o.getBillPlan());
                ari.setHit(o.getHit());
                ari.setFee(o.getFee());
                ari.setBalance(o.getBalance());
                list.add(ari);
            }
            listDTO.setList(list);
        }
        return listDTO;
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
                    d.setProductCode(info.getCode());
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
                    d.setProductCode(info.getCode());
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
    public ProductDTO getClientProductInfo(Long clientId, Long productId)
    {
        ProductDTO dto = new ProductDTO();
        ClientProduct clientProduct = clientProductMapper.findByClientAndProduct(clientId, productId);
        if(clientProduct == null || !TrueOrFalse.TRUE.equals(clientProduct.getOpened()))
        {
            dto.setResult(RestResult.PRODUCT_NOT_OPEN);
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
    public ListDTO<DictDTO> getClientProductDict(Long clientId)
    {
        ListDTO<DictDTO> listDTO = new ListDTO<>();
        List<ProductClientInfo> dataList = productClientInfoMapper.getClientDictList(clientId);
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<DictDTO> list = new ArrayList<>();
            for(ProductClientInfo o : dataList)
            {
                list.add(new DictDTO(o.getProductId() + "", o.getProductName()));
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public Integer checkIfContractExist(String contractNo)
    {
        Recharge pro = rechargeMapper.findByContractNo(contractNo);
        return pro != null ? TrueOrFalse.TRUE : TrueOrFalse.FALSE;
    }

    @Override
    public ProductRechargeDTO getProductRechargeById(Long id)
    {
        ProductRechargeDTO productRechargeDTO = new ProductRechargeDTO();
        Recharge pr = rechargeMapper.findById(id);
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
        return rechargeMapper.sumAmountByClientProduct(clientProductId);
    }

    @Override
    public ProductDTO getProductInfoData(Long productId)
    {
        ProductDTO productDTO = new ProductDTO();
        Product product = productMapper.findById(productId);
        if(product == null)
        {
            productDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return productDTO;
        }
        ProductTxt productTxt = productTxtMapper.findById(productId);
        productDTO.setType(product.getType());
        productDTO.setProductCode(product.getCode());
        productDTO.setName(product.getName());
        productDTO.setCostAmt(product.getCostAmt());
        productDTO.setCustom(product.getCustom());
        productDTO.setRemark(product.getRemark());
        productDTO.setContent(productTxt == null ? null : productTxt.getContent());
        productDTO.setEnabled(product.getEnabled());
        return productDTO;
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
                    productInfoDTO.setCustomName(Custom.getById(productInfoDTO.getCustom()) != null ?
                            Custom.getById(productInfoDTO.getCustom()).getName() : null);
                    productInfoDTO.setTypeName(ProdType.getById(productInfoDTO.getType()) != null ?
                            ProdType.getById(productInfoDTO.getType()).getName() : null);
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
                        productInfoDTO.setCustomName(Custom.getById(productInfoDTO.getCustom()) != null ?
                                Custom.getById(productInfoDTO.getCustom()).getName() : null);
                        productInfoDTO.setTypeName(ProdType.getById(productInfoDTO.getType()) != null ?
                                ProdType.getById(productInfoDTO.getType()).getName() : null);
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
    public ResultDTO editProduct(ProductDTO productDTO)
    {
        ResultDTO resultDTO = new ResultDTO();
        Product product = productMapper.findById(productDTO.getId());
        if(product == null)
        {
            resultDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return resultDTO;
        }
        Date date = new Date();
        Product tempProduct = new Product();
        tempProduct.setId(productDTO.getId());
        tempProduct.setUpdateTime(date);
        tempProduct.setName(productDTO.getName());
        tempProduct.setCostAmt(productDTO.getCostAmt());
        tempProduct.setRemark(productDTO.getRemark());
        tempProduct.setEnabled(productDTO.getEnabled());
        productMapper.updateSkipNull(tempProduct);
        ProductTxt productTxt = productTxtMapper.findById(productDTO.getId());
        if(productTxt == null)
        {
            if(!StringUtils.isNullBlank(productDTO.getContent()))
            {
                productTxt = new ProductTxt();
                productTxt.setId(productDTO.getId());
                productTxt.setCreateTime(date);
                productTxt.setUpdateTime(date);
                productTxt.setContent(productDTO.getContent());
                productTxtMapper.add(productTxt);
            }
        }
        else
        {
            productTxt.setUpdateTime(date);
            productTxt.setContent(productDTO.getContent());
            productTxtMapper.updateById(productTxt);
        }
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
            List<ClientUserProduct> clientUserProductList = clientUserProductMapper.getListByProduct(productId);
            if(!CollectionUtils.isEmpty(clientUserProductList))
            {
                List<Long> idList = new ArrayList<>();
                List<String> tokenList = new ArrayList<>();
                for(ClientUserProduct o : clientUserProductList)
                {
                    if(o.getAccessToken() != null)
                    {
                        idList.add(o.getId());
                        tokenList.add(o.getAccessToken());
                    }
                }
                if(idList.size() > 0)
                {
                    clientUserProductMapper.clearAccessToken(new Date(), idList);
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
                pd.setProductCode(o.getCode());
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

    @Override
    public ListDTO<DictDTO> getProductDict()
    {
        ListDTO<DictDTO> listDTO = new ListDTO<>();
        List<Product> dataList = productMapper.getListByStatus(TrueOrFalse.TRUE);
        if(!CollectionUtils.isEmpty(dataList))
        {
            List<DictDTO> list = new ArrayList<>(dataList.size());
            for(Product o : dataList)
            {
                list.add(new DictDTO(o.getId() + "", o.getName()));
            }
            listDTO.setList(list);
        }
        return listDTO;
    }
}
