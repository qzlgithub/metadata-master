package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ProductRecharge;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

public interface ProductRechargeMapper
{
    void add(ProductRecharge obj);

    ProductRecharge findById(Long id);

    BigDecimal sumAmountByClientProduct(Long clientProductId);

    int countBy(@Param("clientId") Long clientId, @Param("productId") Long productId,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
