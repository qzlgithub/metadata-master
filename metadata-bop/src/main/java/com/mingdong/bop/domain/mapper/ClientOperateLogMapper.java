package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientOperateLog;

public interface ClientOperateLogMapper
{
    void add(ClientOperateLog obj);

    void updateById(ClientOperateLog obj);

    void updateSkipNull(ClientOperateLog obj);
}
