package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;

import java.util.Date;

public interface TradeService
{
    /**
     * 查询客户的交易记录
     *
     * @param corpId     指定客户的企业ID，为null时查询所有客户的交易数据
     * @param isRecharge 是：充值记录，否-消费记录
     */
    BLResp getCorpAccountTrade(Long corpId, boolean isRecharge, Page page);

    /**
     * 查询客户产品账户的充值记录
     */
    BLResp getCorpProdRechargeTrade(Long corpId, Long productId, Page page);

    /**
     * 测试数据
     */
    BLResp testList2(Long productId, Long clientId, Date time, Page page);

    BLResp testList(Long productId, Long clientId, Date time, Page page);

    BLResp testList3(Long clientId, Date time, Page page);

    BLResp testList4(Long clientId, Date time, Page page);

    void getProductRechargeList(Long clientId, Long productId, Date startTime, Date endTime, Page page, BLResp resp);
}
