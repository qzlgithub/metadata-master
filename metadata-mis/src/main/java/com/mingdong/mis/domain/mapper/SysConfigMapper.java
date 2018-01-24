package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.SysConfig;

public interface SysConfigMapper
{
    void add(SysConfig obj);

    void updateById(SysConfig obj);

    SysConfig findByName(String name);
}
