package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientAccount;

import java.util.List;

public interface ClientAccountMapper
{
    void add(ClientAccount clientAccount);

    void delete(Long id);

    void updateById(ClientAccount clientAccount);

    ClientAccount findById(Long id);

    List<ClientAccount> getAll();

    void updateSkipNull(ClientAccount clientAccount);
}
