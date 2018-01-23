package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientMessage;

import java.util.List;

public interface ClientMessageMapper
{
    int countByClient(Long clientId);

    List<ClientMessage> getListByClient(Long clientId);
}
