package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientAccount;

public interface ClientAccountMapper
{
    void add(ClientAccount clientAccount);

    void updateById(ClientAccount clientAccount);

    ClientAccount findById(Long id);
}
