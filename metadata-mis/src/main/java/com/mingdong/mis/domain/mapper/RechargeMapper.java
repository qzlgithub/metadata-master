package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Recharge;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RechargeMapper
{
    void add(Recharge o);

    void updateSkipNull(Recharge o);

    Recharge findById(Long id);

    List<Recharge> findByContractNo(String contractNo);

    BigDecimal sumRechargeAmount(@Param("clientId") Long clientId, @Param("productId") Long productId);
}
