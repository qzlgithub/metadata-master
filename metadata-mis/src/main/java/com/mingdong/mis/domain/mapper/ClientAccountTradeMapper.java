package com.mingdong.mis.domain.mapper;

import org.apache.ibatis.annotations.Param;

public interface ClientAccountTradeMapper
{
    int countBy(@Param("corpId") Long corpId, @Param("income") Integer income);
}
