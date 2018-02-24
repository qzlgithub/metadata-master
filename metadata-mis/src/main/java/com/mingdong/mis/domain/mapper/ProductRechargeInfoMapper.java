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

    int countBy(@Param("clientId") Long clientId, @Param("productId") Long productId,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<ProductRechargeInfo> getProductRechargeInfoListBy(@Param("shortName") String shortName,
            @Param("typeId") Long typeId, @Param("productId") Long productId, @Param("managerId") Long managerId,
            @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int countProductRechargeInfoBy(@Param("shortName") String shortName, @Param("typeId") Long typeId,
            @Param("productId") Long productId, @Param("managerId") Long managerId, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    BigDecimal getProductRechargeInfoSumBy(@Param("shortName") String shortName, @Param("typeId") Long typeId,
            @Param("productId") Long productId, @Param("managerId") Long managerId, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    int countBy1(@Param("keyword") String keyword, @Param("productId") Long productId,
            @Param("managerId") Long managerId, @Param("rechargeType") Long rechargeType,
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    BigDecimal sumRechargeAmountBy(@Param("keyword") String keyword, @Param("productId") Long productId,
            @Param("managerId") Long managerId, @Param("rechargeType") Long rechargeType,
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    List<ProductRechargeInfo> getListBy1(@Param("keyword") String keyword, @Param("productId") Long productId,
            @Param("managerId") Long managerId, @Param("rechargeType") Long rechargeType,
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    int countByClient(@Param("clientId") Long clientId, @Param("productId") Long productId,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<ProductRechargeInfo> getListByClient(@Param("clientId") Long clientId, @Param("productId") Long productId,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
