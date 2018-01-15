package com.mingdong.bop.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.domain.entity.ApiReqInfo;
import com.mingdong.bop.domain.entity.ProductRechargeInfo;
import com.mingdong.bop.domain.mapper.ApiReqInfoMapper;
import com.mingdong.bop.domain.mapper.ApiReqMapper;
import com.mingdong.bop.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.bop.domain.mapper.ProductRechargeMapper;
import com.mingdong.common.model.Page;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
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
    ProductRechargeMapper productRechargeMapper;
    @Resource
    ProductRechargeInfoMapper productRechargeInfoMapper;
    @Resource
    ApiReqMapper apiReqMapper;
    @Resource
    ApiReqInfoMapper apiReqInfoMapper;

    @Override
    public ProductRecListDTO getProductRechargeRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page,
            BLResp resp)
    {
        if(page == null)
        {
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, fromDate, endDate);
            ProductRecListDTO productRecListDTO = new ProductRecListDTO(RestResult.SUCCESS);
            if(CollectionUtils.isNotEmpty(dataList))
            {
                List<ProductRechargeDTO> dataDtoList = new ArrayList<ProductRechargeDTO>();
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
                productRecListDTO.setProductRechargeDTOList(new ArrayList<ProductRechargeDTO>());
                return productRecListDTO;
            }
        }
        else
        {
            int total = productRechargeMapper.countBy(clientId, productId, fromDate, endDate);
            int pages = page.getTotalPage(total);
            resp.addData(Field.TOTAL, total);
            resp.addData(Field.PAGES, pages);
            resp.addData(Field.PAGE_NUM, page.getPageNum());
            resp.addData(Field.PAGE_SIZE, page.getPageSize());
            ProductRecListDTO productRecListDTO = new ProductRecListDTO(RestResult.SUCCESS);
            productRecListDTO.setTotal(total);
            productRecListDTO.setPages(pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, fromDate, endDate);
                List<ProductRechargeDTO> dataDtoList = new ArrayList<ProductRechargeDTO>();
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
                productRecListDTO.setProductRechargeDTOList(new ArrayList<ProductRechargeDTO>());
                return productRecListDTO;
            }
        }
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

    @Override
    public ProductReqListDTO getProductRequestRecord(Long clientId, Long productId, Date fromDate, Date endDate, Page page,
            BLResp resp)
    {
        if(page == null)
        {
            List<ApiReqInfo> dataList = apiReqInfoMapper.getListBy(clientId, productId, fromDate, endDate);
            ProductReqListDTO productReqListDTO = new ProductReqListDTO(RestResult.SUCCESS);
            if(CollectionUtils.isNotEmpty(dataList))
            {
                List<ProductRequestDTO> dataDtoList = new ArrayList<ProductRequestDTO>();
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
                productReqListDTO.setProductRequestDTOList(new ArrayList<ProductRequestDTO>());
                return productReqListDTO;
            }
        }
        else
        {
            int total = apiReqMapper.countBy(clientId, productId, fromDate, endDate);
            int pages = page.getTotalPage(total);
            resp.addData(Field.TOTAL, total);
            resp.addData(Field.PAGES, pages);
            resp.addData(Field.PAGE_NUM, page.getPageNum());
            resp.addData(Field.PAGE_SIZE, page.getPageSize());
            ProductReqListDTO productReqListDTO = new ProductReqListDTO(RestResult.SUCCESS);
            productReqListDTO.setTotal(total);
            productReqListDTO.setPages(pages);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ApiReqInfo> dataList = apiReqInfoMapper.getListBy(clientId, productId, fromDate, endDate);
                List<ProductRequestDTO> dataDtoList = new ArrayList<ProductRequestDTO>();
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
                productReqListDTO.setProductRequestDTOList(new ArrayList<ProductRequestDTO>());
                return productReqListDTO;
            }
        }
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

