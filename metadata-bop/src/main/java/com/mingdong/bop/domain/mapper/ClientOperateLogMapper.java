package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientOperateLog;

import java.util.List;

public interface ClientOperateLogMapper
{

    void addList(List<ClientOperateLog> logList);

    int countByClientUser(Long clientUserId);
}
