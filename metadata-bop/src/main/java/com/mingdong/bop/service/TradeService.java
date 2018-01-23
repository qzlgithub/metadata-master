package com.mingdong.bop.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;

import java.util.Date;

public interface TradeService
{
    /**
     * 测试数据
     */
    BLResp testList2(Long productId, Long clientId, Date time, Page page);

    BLResp testList3(Long clientId, Date time, Page page);

    BLResp testList4(Long clientId, Date time, Page page);

    void getProductRechargeList(Long clientId, Long productId, Date startTime, Date endTime, Page page, BLResp resp);
}
