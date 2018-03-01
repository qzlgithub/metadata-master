package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Recharge;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

public interface RechargeMapper
{
    void add(Recharge obj);

    void updateSkipNull(Recharge obj);

    Recharge findById(Long id);

    BigDecimal sumAmountByClientProduct(Long clientProductId);

    int countBy(@Param("clientId") Long clientId, @Param("productId") Long productId,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Recharge findByContractNo(String contractNo);
}
