package com.mingdong.core.service;

import com.mingdong.core.model.dto.request.RechargeReqDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;

public interface TradeRpcService
{
    /**
     * 客户产品充值
     */
    ResponseDTO productRecharge(RechargeReqDTO reqDTO);
}
