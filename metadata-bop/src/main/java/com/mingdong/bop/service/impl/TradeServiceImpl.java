package com.mingdong.bop.service.impl;

import com.github.pagehelper.PageHelper;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.domain.entity.CorpTradeInfo;
import com.mingdong.bop.domain.entity.ProdRechargeInfo;
import com.mingdong.bop.domain.entity.ProductRechargeInfo;
import com.mingdong.bop.domain.mapper.ClientAccountTradeMapper;
import com.mingdong.bop.domain.mapper.CorpProdRechargeMapper;
import com.mingdong.bop.domain.mapper.CorpTradeInfoMapper;
import com.mingdong.bop.domain.mapper.ProdRechargeInfoMapper;
import com.mingdong.bop.domain.mapper.ProductRechargeInfoMapper;
import com.mingdong.bop.domain.mapper.ProductRechargeMapper;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.model.BLResp;
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
    private ClientAccountTradeMapper clientAccountTradeMapper;
    @Resource
    private CorpTradeInfoMapper corpTradeInfoMapper;
    @Resource
    private CorpProdRechargeMapper corpProdRechargeMapper;
    @Resource
    private ProdRechargeInfoMapper prodRechargeInfoMapper;
    @Resource
    private ProductRechargeInfoMapper productRechargeInfoMapper;
    @Resource
    private ProductRechargeMapper productRechargeMapper;

    @Override
    public BLResp getCorpAccountTrade(Long corpId, boolean isRecharge, Page page)
    {
        BLResp resp = BLResp.build();
        Integer income = isRecharge ? 1 : 0;
        int total = clientAccountTradeMapper.countBy(corpId, income);
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(total > 0 && page.getPageNum() <= pages)
        {
            List<CorpTradeInfo> dataList;
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            if(isRecharge)
            {
                dataList = corpTradeInfoMapper.getCorpRechargeTrade(corpId);
            }
            else
            {
                dataList = corpTradeInfoMapper.getCorpSpendTrade(corpId);
            }
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(CorpTradeInfo info : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.TRADE_ID, info.getTradeId() + "");
                map.put(Field.TRADE_AT, DateUtils.format(info.getTradeTime(), "yyyy-MM-dd HH:mm:ss"));
                map.put(Field.TRADE_NO, info.getTradeNo());
                map.put(Field.CORP_NAME, info.getCorpName());
                map.put(Field.SHORT_NAME, info.getShortName());
                map.put(Field.ACCOUNT, info.getAccount());
                map.put(Field.TRADE_TYPE, info.getTradeType());
                map.put(Field.AMOUNT, NumberUtils.formatAmount(info.getAmount()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(info.getBalance()));
                map.put(Field.MANAGER_ID, info.getManagerId() + "");
                map.put(Field.MANAGER_NAME, info.getManagerName());
                map.put(Field.PRODUCT, info.getProduct());
                map.put(Field.REMARK, info.getRemark());
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
        return resp;
    }

    @Override
    public BLResp getCorpProdRechargeTrade(Long corpId, Long productId, Page page)
    {
        BLResp resp = new BLResp();
        int total = corpProdRechargeMapper.countBy(corpId, productId);
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProdRechargeInfo> dataList = prodRechargeInfoMapper.getBy(corpId, productId);
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(ProdRechargeInfo info : dataList)
            {
                Map<String, Object> map = new HashMap<>();
                map.put(Field.TRADE_ID, info.getTradeId() + "");
                map.put(Field.TRADE_AT, DateUtils.format(info.getTradeAt(), DateFormat.YYYY_MM_DD_HH_MM_SS));
                map.put(Field.TRADE_NO, info.getTradeNo());
                map.put(Field.CORP_NAME, info.getCorpName());
                map.put(Field.SHORT_NAME, info.getShortName());
                map.put(Field.ACCOUNT, info.getAccount());
                map.put(Field.PRODUCT, info.getProduct());
                map.put(Field.AMOUNT, NumberUtils.formatAmount(info.getAmount()));
                map.put(Field.BALANCE, NumberUtils.formatAmount(info.getBalance()));
                map.put(Field.MANAGER_ID, info.getManagerId() + "");
                map.put(Field.MANAGER_NAME, info.getManagerName());
                map.put(Field.CONTRACT_NO, info.getContractNo());
                map.put(Field.REMARK, info.getRemark());
                list.add(map);
            }
            resp.addData(Field.LIST, list);
        }
        return resp;
    }

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
    public BLResp testList(Long productId, Long clientId, Date time, Page page)
    {
        BLResp resp = BLResp.build();
        int total = 5;
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total);
        resp.addData(Field.PAGES, pages);
        resp.addData(Field.PAGE_NUM, page.getPageNum());
        resp.addData(Field.PAGE_SIZE, page.getPageSize());

        List<Map<String, Object>> list = new ArrayList<>();
        for(int i = 0; i < 5; i++)
        {

            Map<String, Object> map = new HashMap<>();
            map.put("createTime", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:mm"));
            map.put("tradeNo", "123456");
            map.put("clientName", "荣耀科技");
            map.put("shortName", "荣科");
            map.put("username", "王科");
            map.put("productName", "白名单");
            map.put("rechargeType", "自充");
            map.put("amount", "8.50");
            map.put("balance", "100000.00");
            map.put("manager", "商务经理");
            map.put("contractNo", "112233");
            map.put("remark", "荣耀科技公司的充值记录");
            list.add(map);
        }

        resp.addData(Field.LIST, list);
        return resp;
    }

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
        int total = productRechargeMapper.countBy(clientId, productId, startTime, endTime);
        int pages = page.getTotalPage(total);
        resp.addData(Field.TOTAL, total).addData(Field.PAGES, pages).addData(Field.PAGE_NUM, page.getPageNum()).addData(
                Field.PAGE_SIZE, page.getPageSize());
        if(total > 0 && page.getPageNum() <= pages)
        {
            PageHelper.startPage(page.getPageNum(), page.getPageSize(), false);
            List<ProductRechargeInfo> dataList = productRechargeInfoMapper.getListBy(clientId, productId, startTime,
                    endTime);
            List<Map<String, Object>> list = new ArrayList<>(dataList.size());
            for(ProductRechargeInfo pri : dataList)
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
            resp.addData(Field.LIST, list);
        }
    }

}
