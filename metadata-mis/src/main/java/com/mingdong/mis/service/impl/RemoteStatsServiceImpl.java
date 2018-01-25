package com.mingdong.mis.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ClientInfoDTO;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.service.RemoteStatsService;
import com.mingdong.core.util.EntityUtils;
import com.mingdong.mis.domain.entity.ClientInfo;
import com.mingdong.mis.domain.entity.ProductRechargeInfo;
import com.mingdong.mis.domain.mapper.ClientInfoMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.mis.domain.mapper.StatsClientMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemoteStatsServiceImpl implements RemoteStatsService
{
    @Resource
    private StatsClientMapper statsClientMapper;
    @Resource
    private ClientInfoMapper clientInfoMapper;
    @Resource
    private ProductRechargeInfoMapper productRechargeInfoMapper;

    @Override
    public Integer getAllClientCount()
    {
        return statsClientMapper.getAllClientCount();
    }

    @Override
    public Integer getClientCountByDate(Date monthDay, Date currentDay)
    {
        return statsClientMapper.getClientCountByDate(monthDay, currentDay);
    }

    @Override
    public ClientInfoListDTO getClientInfoListByDate(Date date, Date currentDay, Page page)
    {
        ClientInfoListDTO clientInfoListDTO = new ClientInfoListDTO();
        List<ClientInfoDTO> dataList = new ArrayList<>();
        clientInfoListDTO.setDataList(dataList);
        if(page == null)
        {
            List<ClientInfo> clientInfoList = clientInfoMapper.getClientInfoListByDate(date, currentDay);
            if(CollectionUtils.isNotEmpty(clientInfoList))
            {
                findClientInfoDTO(clientInfoList, dataList);
            }
        }
        else
        {
            int total = statsClientMapper.getClientCountByDate(date, currentDay);
            int pages = page.getTotalPage(total);
            clientInfoListDTO.setPages(pages);
            clientInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ClientInfo> clientInfoList = clientInfoMapper.getClientInfoListByDate(date, currentDay);
                if(CollectionUtils.isNotEmpty(clientInfoList))
                {
                    findClientInfoDTO(clientInfoList, dataList);
                }
            }
        }
        return clientInfoListDTO;
    }

    @Override
    public BigDecimal getClientRechargeStatsByDate(Date nowDay, Date currentDay)
    {
        return statsClientMapper.getClientRechargeByDate(nowDay, currentDay);
    }

    @Override
    public BigDecimal getClientRechargeStatsAll()
    {
        return statsClientMapper.getClientRechargeAll();
    }

    @Override
    public ProductRechargeInfoListDTO getProductRechargeInfoListBy(Date date, Date currentDay, Page page)
    {
        ProductRechargeInfoListDTO productRechargeInfoListDTO = new ProductRechargeInfoListDTO();
        List<ProductRechargeInfoDTO> dataList = new ArrayList<>();
        productRechargeInfoListDTO.setDataList(dataList);
        if(page == null)
        {
            List<ProductRechargeInfo> productRechargeInfoList = productRechargeInfoMapper.getListBy(null, null, date,
                    currentDay);
            if(CollectionUtils.isNotEmpty(productRechargeInfoList))
            {
                findProductRechargeInfoDTO(productRechargeInfoList, dataList);
            }
        }
        else
        {
            int total = statsClientMapper.countClientRechargeByDate(date, currentDay);
            int pages = page.getTotalPage(total);
            productRechargeInfoListDTO.setPages(pages);
            productRechargeInfoListDTO.setTotal(total);
            if(total > 0 && page.getPageNum() <= pages)
            {
                PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
                List<ProductRechargeInfo> productRechargeInfoList = productRechargeInfoMapper.getListBy(null, null,
                        date, currentDay);
                if(CollectionUtils.isNotEmpty(productRechargeInfoList))
                {
                    findProductRechargeInfoDTO(productRechargeInfoList, dataList);
                }
            }
        }
        return productRechargeInfoListDTO;
    }

    @Override
    public Integer getClientRechargeCountByDate(Date date, Date currentDay)
    {
        return statsClientMapper.countClientRechargeByDate(date, currentDay);
    }

    private void findClientInfoDTO(List<ClientInfo> clientInfoList, List<ClientInfoDTO> dataList)
    {
        ClientInfoDTO clientInfoDTO;
        for(ClientInfo item : clientInfoList)
        {
            clientInfoDTO = new ClientInfoDTO();
            EntityUtils.copyProperties(item, clientInfoDTO);
            dataList.add(clientInfoDTO);
        }
    }

    private void findProductRechargeInfoDTO(List<ProductRechargeInfo> productRechargeInfoList,
            List<ProductRechargeInfoDTO> dataList)
    {
        ProductRechargeInfoDTO productRechargeInfoDTO;
        for(ProductRechargeInfo item : productRechargeInfoList)
        {
            productRechargeInfoDTO = new ProductRechargeInfoDTO();
            dataList.add(productRechargeInfoDTO);
            EntityUtils.copyProperties(item, productRechargeInfoDTO);
        }
    }
}
