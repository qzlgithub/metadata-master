package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientContact;

public interface ClientContactMapper
{
    void add(ClientContact obj);

    void updateById(ClientContact obj);

    void updateSkipNull(ClientContact obj);
}
