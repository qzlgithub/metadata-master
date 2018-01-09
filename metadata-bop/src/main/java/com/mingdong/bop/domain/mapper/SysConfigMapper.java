package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.SysConfig;

import java.util.List;

public interface SysConfigMapper
{
    void add(SysConfig obj);

    void updateById(SysConfig obj);

    List<SysConfig> getAll();
}
