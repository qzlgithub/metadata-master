package com.mingdong.bop.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.bop.constant.Constant;
import com.mingdong.bop.domain.entity.ApiReqInfo;
import com.mingdong.bop.domain.entity.ClientProduct;
import com.mingdong.bop.domain.entity.ProductClientInfo;
import com.mingdong.bop.domain.entity.ProductRechargeInfo;
import com.mingdong.bop.domain.mapper.ApiReqInfoMapper;
import com.mingdong.bop.domain.mapper.ApiReqMapper;
import com.mingdong.bop.domain.mapper.ClientProductMapper;
import com.mingdong.bop.domain.mapper.ProductClientInfoMapper;
import com.mingdong.bop.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.bop.domain.mapper.ProductRechargeMapper;
import com.mingdong.common.model.Page;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.ProductStatus;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.DictDTO;
import com.mingdong.core.model.dto.ProductDTO;
import com.mingdong.core.model.dto.ProductDictDTO;
import com.mingdong.core.model.dto.ProductListDTO;
import com.mingdong.core.model.dto.ProductRecListDTO;
import com.mingdong.core.model.dto.ProductRechargeDTO;
import com.mingdong.core.model.dto.ProductReqListDTO;
import com.mingdong.core.model.dto.ProductRequestDTO;
import com.mingdong.core.service.RemoteProductService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteProductServiceImpl implements RemoteProductService
{
    private static Logger logger = LoggerFactory.getLogger(RemoteProductServiceImpl.class);

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

    @Override
    public ProductRecListDTO getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate,
            Page page)
    {
        if(page == null)
        {
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, fromDate,
                    endDate);
            ProductRecListDTO productRecListDTO = new ProductRecListDTO(RestResult.SUCCESS);
            if(CollectionUtils.isNotEmpty(dataList))
            {
                List<ProductRechargeDTO> dataDtoList = new ArrayList<>();
                ProductRechargeDTO productRechargeDTO;
                for(ProductRechargeInfo item : dataList)
                {
                    productRechargeDTO = new ProductRechargeDTO();
                    dataDtoList.add(productRechargeDTO);
                    productRechargeToDTO(item, productRechargeDTO);
                }
                productRecListDTO.setProductRechargeDTOList(dataDtoList);
                return productRecListDTO;
            }
            else
            {
                productRecListDTO.setProductRechargeDTOList(new ArrayList<>());
                return productRecListDTO;
            }
        }
        else
        {
            int total = productRechargeMapper.countBy(clientId, productId, fromDate, endDate);
            int pages = page.getTotalPage(total);
            ProductRecListDTO productRecListDTO = new ProductRecListDTO(RestResult.SUCCESS);
            productRecListDTO.setTotal(total);
            productRecListDTO.setPages(pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, fromDate,
                        endDate);
                List<ProductRechargeDTO> dataDtoList = new ArrayList<>();
                ProductRechargeDTO productRechargeDTO;
                for(ProductRechargeInfo item : dataList)
                {
                    productRechargeDTO = new ProductRechargeDTO();
                    dataDtoList.add(productRechargeDTO);
                    productRechargeToDTO(item, productRechargeDTO);
                }
                productRecListDTO.setProductRechargeDTOList(dataDtoList);
                return productRecListDTO;
            }
            else
            {
                productRecListDTO.setProductRechargeDTOList(new ArrayList<>());
                return productRecListDTO;
            }
        }
    }

    @Override
    public ProductReqListDTO getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate,
            Page page)
    {
        if(page == null)
        {
            List<ApiReqInfo> dataList = apiReqInfoMapper.getListBy(clientId, productId, fromDate, endDate);
            ProductReqListDTO productReqListDTO = new ProductReqListDTO(RestResult.SUCCESS);
            if(CollectionUtils.isNotEmpty(dataList))
            {
                List<ProductRequestDTO> dataDtoList = new ArrayList<>();
                ProductRequestDTO dataDto;
                for(ApiReqInfo item : dataList)
                {
                    dataDto = new ProductRequestDTO();
                    dataDtoList.add(dataDto);
                    productRequestToDto(item, dataDto);
                }
                productReqListDTO.setProductRequestDTOList(dataDtoList);
                return productReqListDTO;
            }
            else
            {
                productReqListDTO.setProductRequestDTOList(new ArrayList<>());
                return productReqListDTO;
            }
        }
        else
        {
            int total = apiReqMapper.countBy(clientId, productId, fromDate, endDate);
            int pages = page.getTotalPage(total);
            ProductReqListDTO productReqListDTO = new ProductReqListDTO(RestResult.SUCCESS);
            productReqListDTO.setTotal(total);
            productReqListDTO.setPages(pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ApiReqInfo> dataList = apiReqInfoMapper.getListBy(clientId, productId, fromDate, endDate);
                List<ProductRequestDTO> dataDtoList = new ArrayList<>();
                ProductRequestDTO dataDto;
                for(ApiReqInfo item : dataList)
                {
                    dataDto = new ProductRequestDTO();
                    dataDtoList.add(dataDto);
                    productRequestToDto(item, dataDto);
                }
                productReqListDTO.setProductRequestDTOList(dataDtoList);
                return productReqListDTO;
            }
            else
            {
                productReqListDTO.setProductRequestDTOList(new ArrayList<>());
                return productReqListDTO;
            }
        }
    }

    @Override
    public ProductListDTO getIndexProductList(Long clientId)
    {
        ProductListDTO dto = new ProductListDTO();
        List<ProductClientInfo> dataList = productClientInfoMapper.getListByClient(clientId);
        List<ProductDTO> opened = new ArrayList<>();
        List<ProductDTO> toOpen = new ArrayList<>();
        for(ProductClientInfo info : dataList)
        {
            if(info.getClientProductId() != null)
            {
                if(opened.size() < Constant.HOME_PRODUCT_QTY)
                {
                    ProductDTO d = new ProductDTO();
                    d.setId(info.getProductId());
                    d.setName(info.getProductName());
                    d.setBillPlan(info.getBillPlan());
                    if(BillPlan.YEAR.getId().equals(info.getBillPlan()))
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
        if(BillPlan.YEAR.getId().equals(info.getBillPlan()))
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
            DictDTO dd = new DictDTO();
            dd.setKey(o.getProductId() + "");
            dd.setValue(o.getProductName());
            dictList.add(dd);
        }
        dto.setProductDictList(dictList);
        return dto;
    }

    private void productRechargeToDTO(ProductRechargeInfo left, ProductRechargeDTO right)
    {
        right.setId(left.getId());
        right.setAmount(left.getAmount());
        right.setBalance(left.getBalance());
        right.setContractNo(left.getContractNo());
        right.setCorpName(left.getCorpName());
        right.setContractNo(left.getContractNo());
        right.setProductName(left.getProductName());
        right.setRechargeType(left.getRechargeType());
        right.setRemark(left.getRemark());
        right.setShortName(left.getShortName());
        right.setTradeNo(left.getTradeNo());
        right.setTradeTime(left.getTradeTime());
        right.setUsername(left.getUsername());
    }

    private void productRequestToDto(ApiReqInfo left, ProductRequestDTO right)
    {
        right.setId(left.getId());
        right.setBalance(left.getBalance());
        right.setCorpName(left.getCorpName());
        right.setCallTime(left.getCallTime());
        right.setProductName(left.getProductName());
        right.setShortName(left.getShortName());
        right.setSuc(left.getSuc());
        right.setUnitAmt(left.getUnitAmt());
        right.setUsername(left.getUsername());
    }
}

