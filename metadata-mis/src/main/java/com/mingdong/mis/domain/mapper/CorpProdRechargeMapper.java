package com.mingdong.mis.domain.mapper;

import org.apache.ibatis.annotations.Param;

public interface CorpProdRechargeMapper
{
    int countBy(@Param("corpId") Long corpId, @Param("productId") Long productId);
}
