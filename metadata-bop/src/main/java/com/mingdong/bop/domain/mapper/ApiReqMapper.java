package com.mingdong.bop.domain.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface ApiReqMapper
{

    int countBy(@Param("clientId") Long clientId, @Param("productId") Long productId,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
