package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ApiReq;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface ApiReqMapper
{
    void add(ApiReq obj);

    int countBy(@Param("clientId") Long clientId, @Param("userId") Long userId, @Param("productId") Long productId,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
