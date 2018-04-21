package com.mingdong.mis.mongo.dao;

import com.mingdong.common.model.Page;
import com.mingdong.mis.mongo.entity.RequestLog;
import com.mingdong.mis.mongo.entity.RequestNumber;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RequestLogDao
{
    void insert(RequestLog requestLog);

    long countByRequestTime(Date startTime, Date endTime);

    long countByParam(Long clientId, Long clientUserId, Long productId, Date startTime, Date endTime, Integer hit);

    long countByParam(String keyword, Long productId, Integer billPlan, Integer hit, Date startTime, Date endTime);

    BigDecimal sumFeeByParam(String keyword, Long productId, Integer billPlan, Date fromDate, Date toDate);

    List<RequestLog> findByParam(String keyword, Long clientId, Long clientUserId, Long productId, Integer billPlan,
            Date startTime, Date endTime, Integer hit, Page page);

    List<RequestNumber> findRequestGroupCountByTime(Date startTime, Date endTime);

    List<RequestNumber> findRequestCountByTime(Date startTime, Date endTime);
}
