package com.mingdong.mis.service;

import com.mingdong.core.constant.BillPlan;
import com.mingdong.mis.model.vo.AbsPayload;

import java.math.BigDecimal;
import java.util.Date;

public interface DataService
{
    String saveRequestLog(Long clientId, Long clientUserId, Long productId, String host, AbsPayload payload,
            boolean hit, Date timestamp);

    String chargeAndSaveRequestLog(Long clientId, Long clientUserId, Long productId, Long clientProductId,
            BillPlan billPlan, BigDecimal fee, BigDecimal balance, String host, AbsPayload payload, boolean hit,
            Date timestamp);
}
