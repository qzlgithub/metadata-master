package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.SysConfig;

public interface SysConfigMapper
{
    void add(SysConfig obj);

    void updateById(SysConfig obj);

    SysConfig findByName(String name);
}
