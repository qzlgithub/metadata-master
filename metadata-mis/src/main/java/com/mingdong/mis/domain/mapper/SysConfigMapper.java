package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.SysConfig;

import java.util.List;

public interface SysConfigMapper
{
    void add(SysConfig obj);

    void updateById(SysConfig obj);

    SysConfig findByName(String name);

    List<SysConfig> getAll();
}
