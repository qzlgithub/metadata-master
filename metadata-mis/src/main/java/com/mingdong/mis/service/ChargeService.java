package com.mingdong.mis.service;

import com.mingdong.mis.model.CheckResult;

public interface ChargeService
{
    /**
     * 查询账户余额及计费方式
     */
    CheckResult checkAccountAndBillPlan();
}
