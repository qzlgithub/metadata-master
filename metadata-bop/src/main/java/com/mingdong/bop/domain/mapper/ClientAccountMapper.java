package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientAccount;

public interface ClientAccountMapper
{
    void add(ClientAccount clientAccount);

    void updateById(ClientAccount clientAccount);

    void updateSkipNull(ClientAccount clientAccount);

    ClientAccount findById(Long id);
}
