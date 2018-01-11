package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientOperateLog;

import java.util.List;

public interface ClientOperateLogMapper
{
    void add(ClientOperateLog obj);

    void addList(List<ClientOperateLog> logList);

    void updateById(ClientOperateLog obj);

    void updateSkipNull(ClientOperateLog obj);

    int countByClientUser(Long clientUserId);
}
