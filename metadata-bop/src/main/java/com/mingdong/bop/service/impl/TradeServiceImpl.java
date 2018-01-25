package com.mingdong.bop.service.impl;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
import com.mingdong.core.service.RemoteProductService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeServiceImpl implements TradeService
{
    @Resource
    private RemoteProductService remoteProductService;

    @Override
    public BLResp testList2(Long productId, Long clientId, Date time, Page page)
    {
        BLResp resp = BLResp.build();
        int total = 10;
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());

        List<Map<String, Object>> list = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("createTime", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:mm"));
            map.put("tradeNo", "123456");
            map.put("clientName", "荣耀科技");
            map.put("shortName", "荣科");
            map.put("username", "王科");
            map.put("productName", "白名单");
            map.put("pillBlan", "包年");
            map.put("enabled", "0");
            map.put("unitAmt", "10.00");
            map.put("amount", "150000.00");
            list.add(map);
        }

        resp.addData(Field.LIST, list);
        return resp;
    }

    /**
     * 测试数据
     */
    @Override
    public BLResp testList3(Long clientId, Date time, Page page)
    {
        BLResp resp = BLResp.build();
        int total = 10;
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());

        List<Map<String, Object>> list = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {

            Map<String, Object> map = new HashMap<>();
            map.put("createTime", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:mm"));
            map.put("tradeNo", "123456");
            map.put("clientName", "辉煌科技");
            map.put("shortName", "辉科");
            map.put("username", "李辉");
            map.put("amount", "10.50");
            map.put("rechargeType", "代充");
            map.put("balance", "100000.00");
            map.put("manager", "商务经理");
            map.put("remark", "荣耀科技公司的充值记录");
            list.add(map);
        }

        resp.addData(Field.LIST, list);
        return resp;
    }

    @Override
    public BLResp testList4(Long clientId, Date time, Page page)
    {
        BLResp resp = BLResp.build();
        int total = 10;
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());

        List<Map<String, Object>> list = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {

            Map<String, Object> map = new HashMap<>();
            map.put("createTime", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:mm"));
            map.put("tradeNo", "123456");
            map.put("clientName", "辉煌科技");
            map.put("shortName", "辉科");
            map.put("username", "李辉");
            map.put("purpose", "续费");
            map.put("productName", "白名单");
            map.put("consume", "2000.00");
            map.put("balance", "100000.00");
            list.add(map);
        }

        resp.addData(Field.LIST, list);
        return resp;
    }

    /**
     * 产品充值列表
     */
    @Override
    public void getProductRechargeList(Long clientId, Long productId, Date startTime, Date endTime, Page page,
            BLResp resp)
    {
        ProductRechargeInfoListDTO productRechargeInfoListDTO = remoteProductService.getProductRechargeInfoList(
                clientId, productId, startTime, endTime, page);
        resp.addData(Field.TOTAL, productRechargeInfoListDTO.getTotal());
        resp.addData(Field.PAGES, productRechargeInfoListDTO.getPages());
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        List<ProductRechargeInfoDTO> dataList = productRechargeInfoListDTO.getDataList();
        List<Map<String, Object>> list = new ArrayList<>(dataList.size());
        if(CollectionUtils.isNotEmpty(dataList))
        {
            for(ProductRechargeInfoDTO pri : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.TRADE_AT, DateUtils.format(pri.getTradeTime(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, pri.getTradeNo());
                map.put(Field.CORP_NAME, pri.getCorpName());
                map.put(Field.SHORT_NAME, pri.getShortName());
                map.put(Field.USERNAME, pri.getUsername());
                map.put(Field.PRODUCT_NAME, pri.getProductName());
                map.put(Field.RECHARGE_TYPE, pri.getRechargeType());
                map.put(Field.AMOUNT, pri.getAmount());
                map.put(Field.BALANCE, pri.getBalance());
                map.put(Field.MANAGER_NAME, pri.getManagerName());
                map.put(Field.CONTRACT_NO, pri.getContractNo());
                map.put(Field.REMARK, pri.getRemark());
                list.add(map);
            }
        }
        resp.addData(Field.LIST, list);
    }

}
