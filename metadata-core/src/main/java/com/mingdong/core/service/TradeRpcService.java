package com.mingdong.core.service;

import com.mingdong.core.model.dto.RechargeReqDTO;
import com.mingdong.core.model.dto.base.ResponseDTO;

public interface TradeRpcService
{
    /**
     * 客户产品充值
     */
    ResponseDTO productRecharge(RechargeReqDTO rechargeReqDTO);
}
