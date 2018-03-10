package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ProductRechargeInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProductRechargeInfoMapper
{
    List<ProductRechargeInfo> getListBy(@Param("clientId") Long clientId, @Param("productId") Long productId,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    BigDecimal sumRechargeAmountBy(@Param("keyword") String keyword, @Param("productId") Long productId,
            @Param("managerId") Long managerId, @Param("rechargeType") Integer rechargeType,
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    List<ProductRechargeInfo> getListByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int countByClient(@Param("keyword") String keyword, @Param("clientId") Long clientId,
            @Param("productId") Long productId, @Param("managerId") Long managerId,
            @Param("rechargeType") Integer rechargeType, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    List<ProductRechargeInfo> getListByClient(@Param("keyword") String keyword, @Param("clientId") Long clientId,
            @Param("productId") Long productId, @Param("managerId") Long managerId,
            @Param("rechargeType") Integer rechargeType, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);
}
