package com.mingdong.mis.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.ProductStatus;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.ProductReqDTO;
import com.mingdong.core.model.dto.response.AccessResDTO;
import com.mingdong.core.model.dto.response.ProductDetailResDTO;
import com.mingdong.core.model.dto.response.ProductRechargeResDTO;
import com.mingdong.core.model.dto.response.ProductResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.service.ProductRpcService;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.domain.entity.ApiReqInfo;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ClientUserProduct;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.entity.ProductClientInfo;
import com.mingdong.mis.domain.entity.ProductRechargeInfo;
import com.mingdong.mis.domain.entity.ProductTxt;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.domain.mapper.ApiReqInfoMapper;
import com.mingdong.mis.domain.mapper.ApiReqMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserProductMapper;
import com.mingdong.mis.domain.mapper.ProductClientInfoMapper;
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

public class ProductRpcServiceImpl implements ProductRpcService
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
    private ClientUserProductMapper clientUserProductMapper;
    @Resource
    private RedisDao redisDao;

    @Override
    public ListDTO<ProductRechargeResDTO> getProductRechargeRecord(Long clientId, Long productId, Date fromDate,
            Date endDate, Page page)
    {
        ListDTO<ProductRechargeResDTO> listDTO = new ListDTO<>();
        int total = rechargeMapper.countBy(clientId, productId, fromDate, endDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, fromDate,
                    endDate);
            List<ProductRechargeResDTO> list = new ArrayList<>(dataList.size());
            for(ProductRechargeInfo o : dataList)
            {
                ProductRechargeResDTO pri = new ProductRechargeResDTO();
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
    public ListDTO<AccessResDTO> getProductRequestRecord(Long clientId, Long userId, Long productId, Date fromDate,
            Date endDate, Page page)
    {
        ListDTO<AccessResDTO> listDTO = new ListDTO<>();
        int total = apiReqMapper.countBy(clientId, userId, productId, fromDate, endDate);
        int pages = page.getTotalPage(total);
        listDTO.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ApiReqInfo> apiReqInfoList = apiReqInfoMapper.getListBy(clientId, userId, productId, fromDate,
                    endDate);
            List<AccessResDTO> list = new ArrayList<>(apiReqInfoList.size());
            for(ApiReqInfo o : apiReqInfoList)
            {
                AccessResDTO ari = new AccessResDTO();
                ari.setRequestAt(o.getCreateTime());
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
    public ListDTO<ProductResDTO> getOpenedProductList(Long clientId)
    {
        ListDTO<ProductResDTO> listDTO = new ListDTO<>();
        int total = productClientInfoMapper.countByClientOpened(clientId, TrueOrFalse.TRUE);
        listDTO.setTotal(total);
        if(total > 0)
        {
            List<ProductClientInfo> dataList = productClientInfoMapper.getListByClientOpened(clientId,
                    TrueOrFalse.TRUE);
            List<ProductResDTO> list = new ArrayList<>();
            for(ProductClientInfo o : dataList)
            {
                ProductResDTO p = new ProductResDTO();
                p.setId(o.getProductId());
                p.setName(o.getProductName());
                p.setProductCode(o.getCode());
                p.setType(o.getType());
                p.setTypeName(o.getTypeName());
                p.setBillPlan(o.getBillPlan());
                if(BillPlan.BY_TIME.getId().equals(o.getBillPlan()))
                {
                    p.setStatus(ProductStatus.getStatusByDate(o.getStartDate(), o.getEndDate()));
                    p.setFromDate(o.getStartDate());
                    p.setToDate(o.getEndDate());
                }
                else
                {
                    p.setStatus(ProductStatus.getStatusByBalance(o.getUnitAmt(), o.getBalance()));
                    p.setCostAmt(o.getUnitAmt());
                    p.setBalance(o.getBalance());
                    p.setArrearTime(o.getArrearTime());
                }
                list.add(p);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ListDTO<ProductResDTO> getUnopenedProductList(Long clientId)
    {
        ListDTO<ProductResDTO> listDTO = new ListDTO<>();
        int total = productClientInfoMapper.countByClientOpened(clientId, TrueOrFalse.FALSE);
        listDTO.setTotal(total);
        if(total > 0)
        {
            List<ProductClientInfo> dataList = productClientInfoMapper.getListByClientOpened(clientId,
                    TrueOrFalse.FALSE);
            List<ProductResDTO> list = new ArrayList<>();
            for(ProductClientInfo o : dataList)
            {
                ProductResDTO p = new ProductResDTO();
                p.setId(o.getProductId());
                p.setName(o.getProductName());
                p.setRemark(o.getRemark());
                p.setProductCode(o.getCode());
                p.setTypeName(o.getTypeName());
                p.setType(o.getType());
                list.add(p);
            }
            listDTO.setList(list);
        }
        return listDTO;
    }

    @Override
    public ProductResDTO getClientProductInfo(Long clientId, Long productId)
    {
        ProductResDTO dto = new ProductResDTO();
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
    public Integer checkIfContractExist(String contractNo)
    {
        Recharge pro = rechargeMapper.findByContractNo(contractNo);
        return pro != null ? TrueOrFalse.TRUE : TrueOrFalse.FALSE;
    }

    @Override
    public ProductResDTO getProductInfoData(Long productId)
    {
        ProductResDTO productResDTO = new ProductResDTO();
        Product product = productMapper.findById(productId);
        if(product == null)
        {
            productResDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return productResDTO;
        }
        ProductTxt productTxt = productTxtMapper.findById(productId);
        productResDTO.setType(product.getType());
        productResDTO.setProductCode(product.getCode());
        productResDTO.setName(product.getName());
        productResDTO.setCostAmt(product.getCostAmt());
        productResDTO.setCustom(product.getCustom());
        productResDTO.setRemark(product.getRemark());
        productResDTO.setContent(productTxt == null ? null : productTxt.getContent());
        productResDTO.setEnabled(product.getEnabled());
        return productResDTO;
    }

    @Override
    public List<ProductDetailResDTO> getProductInfoList(Long clientId)
    {
        List<ProductDetailResDTO> list = new ArrayList<>();
        List<ProductClientInfo> dataList = productClientInfoMapper.getListByClient(clientId);
        for(ProductClientInfo d : dataList)
        {
            ProductDetailResDTO o = new ProductDetailResDTO();
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
    public ResponseDTO editProduct(ProductReqDTO reqDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        Product product = productMapper.findById(reqDTO.getId());
        if(product == null)
        {
            responseDTO.setResult(RestResult.OBJECT_NOT_FOUND);
            return responseDTO;
        }
        Date date = new Date();
        Product tempProduct = new Product();
        tempProduct.setId(reqDTO.getId());
        tempProduct.setUpdateTime(date);
        tempProduct.setName(reqDTO.getName());
        tempProduct.setCostAmt(reqDTO.getCostAmt());
        tempProduct.setRemark(reqDTO.getRemark());
        tempProduct.setEnabled(reqDTO.getEnabled());
        productMapper.updateSkipNull(tempProduct);
        ProductTxt productTxt = productTxtMapper.findById(reqDTO.getId());
        if(productTxt == null)
        {
            if(!StringUtils.isNullBlank(reqDTO.getContent()))
            {
                productTxt = new ProductTxt();
                productTxt.setId(reqDTO.getId());
                productTxt.setCreateTime(date);
                productTxt.setUpdateTime(date);
                productTxt.setContent(reqDTO.getContent());
                productTxtMapper.add(productTxt);
            }
        }
        else
        {
            productTxt.setUpdateTime(date);
            productTxt.setContent(reqDTO.getContent());
            productTxtMapper.updateById(productTxt);
        }
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO changeProductStatus(Long productId, Integer enabled)
    {
        ResponseDTO dto = new ResponseDTO();
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
    public ListDTO<ProductResDTO> getProductList(String keyword, Integer type, Integer custom, Integer status, Page page)
    {
        ListDTO<ProductResDTO> dto = new ListDTO<>();
        int total = productMapper.countBy(keyword, type, custom, status);
        int pages = page.getTotalPage(total);
        dto.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<Product> dataList = productMapper.getListBy(keyword, type, custom, status);
            List<ProductResDTO> list = new ArrayList<>(dataList.size());
            for(Product o : dataList)
            {
                ProductResDTO pd = new ProductResDTO();
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
    public ListDTO<ProductRechargeResDTO> getRechargeInfoList(String keyword, Long productId, Long managerId,
            Long rechargeType, Date fromDate, Date toDate, Page page)
    {
        ListDTO<ProductRechargeResDTO> dto = new ListDTO<>();
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
            List<ProductRechargeResDTO> list = new ArrayList<>(dataList.size());
            for(ProductRechargeInfo o : dataList)
            {
                ProductRechargeResDTO pri = new ProductRechargeResDTO();
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
                pri.setStartDate(o.getStartDate());
                pri.setEndDate(o.getEndDate());
                pri.setBillPlan(o.getBillPlan());
                pri.setUnitAmt(o.getUnitAmt());
                list.add(pri);
            }
            dto.setList(list);
        }
        return dto;
    }

    @Override
    public ListDTO<ProductResDTO> getProductList(Long clientId, List<Integer> typeList, Integer incOpened, Page page)
    {
        ListDTO<ProductResDTO> dto = new ListDTO<>();
        incOpened = TrueOrFalse.TRUE.equals(incOpened) ? incOpened : null;
        int total = productClientInfoMapper.countBy(clientId, typeList, incOpened);
        int pages = page.getTotalPage(total);
        dto.setTotal(total);
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductClientInfo> dataList = productClientInfoMapper.getListBy(clientId, typeList, incOpened);
            List<ProductResDTO> list = new ArrayList<>(dataList.size());
            for(ProductClientInfo o : dataList)
            {
                ProductResDTO p = new ProductResDTO();
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
