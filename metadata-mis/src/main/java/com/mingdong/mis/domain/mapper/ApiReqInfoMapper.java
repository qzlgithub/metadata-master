package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ApiReqInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ApiReqInfoMapper
{
    List<ApiReqInfo> getListBy(@Param("clientId") Long clientId, @Param("userId") Long userId,
            @Param("productId") Long productId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int countClientBillListBy(@Param("shortName") String shortName, @Param("typeId") Long typeId,
            @Param("clientId") Long clientId, @Param("userId") Long userId, @Param("productId") Long productId,
            @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<ApiReqInfo> getClientBillListBy(@Param("shortName") String shortName, @Param("typeId") Long typeId,
            @Param("clientId") Long clientId, @Param("userId") Long userId, @Param("productId") Long productId,
            @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    BigDecimal getClientBillFeeSum(@Param("shortName") String shortName, @Param("typeId") Long typeId,
            @Param("clientId") Long clientId, @Param("userId") Long userId, @Param("productId") Long productId,
            @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int countBy1(@Param("keyword") String keyword, @Param("productId") Long productId,
            @Param("billPlan") Integer billPlan, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    BigDecimal sumFeeBy(@Param("keyword") String keyword, @Param("productId") Long productId,
            @Param("billPlan") Integer billPlan, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    List<ApiReqInfo> getListBy1(@Param("keyword") String keyword, @Param("productId") Long productId,
            @Param("billPlan") Integer billPlan, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    int countMiss(@Param("keyword") String keyword, @Param("productId") Long productId,
            @Param("billPlan") Integer billPlan, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}
