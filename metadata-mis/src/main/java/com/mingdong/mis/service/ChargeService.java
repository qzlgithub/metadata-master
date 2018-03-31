package com.mingdong.mis.service;

import com.mingdong.core.constant.BillPlan;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.model.CheckResult;

import java.util.Date;

public interface ChargeService
{
    /**
     * 计费并保存客户请求记录
     */
    String chargeAndLog(ClientProduct account, Long userId, Recharge recharge, BillPlan billPlan, String ip,
            String thirdNo, boolean hit, Date date);

    void renewClientProduct(Recharge pr, ClientProduct cp);

    /**
     * 查询账户余额及计费方式
     */
    CheckResult checkAccountAndBillPlan();
}
