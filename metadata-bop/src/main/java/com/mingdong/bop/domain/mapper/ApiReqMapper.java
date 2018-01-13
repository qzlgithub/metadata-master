package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ApiReq;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ApiReqMapper
{
    void add(ApiReq apiReq);

    void delete(Long id);

    void updateById(ApiReq apiReq);

    ApiReq findById(Long id);

    List<ApiReq> getAll();

    int countBy(@Param("clientId") Long clientId, @Param("productId") Long productId, @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

}
