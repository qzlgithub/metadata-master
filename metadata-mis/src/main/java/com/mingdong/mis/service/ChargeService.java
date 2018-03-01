package com.mingdong.mis.service;

import com.mingdong.core.constant.BillPlan;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.Recharge;

import java.util.Date;

public interface ChargeService
{
    void chargeAndLog(Long requestId, ClientProduct account, Long userId, Recharge recharge, BillPlan billPlan,
            String ip, String thirdNo, boolean hit, Date date);

    void renewClientProduct(Recharge pr, ClientProduct cp);
}
