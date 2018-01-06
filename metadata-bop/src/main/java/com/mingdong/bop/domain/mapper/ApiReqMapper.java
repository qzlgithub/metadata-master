package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ApiReq;

import java.util.List;

public interface ApiReqMapper
{
    void add(ApiReq apiReq);

    void delete(Long id);

    void updateById(ApiReq apiReq);

    ApiReq findById(Long id);

    List<ApiReq> getAll();
}
