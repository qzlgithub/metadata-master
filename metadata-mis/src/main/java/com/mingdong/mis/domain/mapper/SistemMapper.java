package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Sistem;

import java.util.List;

public interface SistemMapper
{
    void add(Sistem o);

    void updateById(Sistem o);

    Sistem findByName(String name);

    List<Sistem> getAll();

    String getSubAccountMaximum();
}
