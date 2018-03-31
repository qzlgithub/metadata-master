package com.mingdong.mis.service;

import com.mingdong.mis.model.vo.AbsPayload;

import java.math.BigDecimal;
import java.util.Date;

public interface DataService
{
    String saveRequestLog(AbsPayload payload, boolean hit, Date timestamp);

    String chargeAndSaveRequestLog(BigDecimal fee, BigDecimal balance, AbsPayload payload, boolean hit, Date timestamp);
}
