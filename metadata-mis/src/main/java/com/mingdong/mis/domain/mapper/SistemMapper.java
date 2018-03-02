package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Sistem;

import java.util.List;

public interface SistemMapper
{
    void add(Sistem obj);

    void updateById(Sistem obj);

    Sistem findByName(String name);

    List<Sistem> getAll();

    String getSubAccountMaximum();
}
