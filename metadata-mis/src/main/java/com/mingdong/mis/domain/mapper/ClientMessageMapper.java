package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientMessage;

import java.util.List;

public interface ClientMessageMapper
{
    int countByClient(Long clientId);

    List<ClientMessage> getListByClient(Long clientId);
}
