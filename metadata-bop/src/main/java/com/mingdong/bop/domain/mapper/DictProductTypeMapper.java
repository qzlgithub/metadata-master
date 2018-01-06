package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.DictProductType;

import java.util.List;

public interface DictProductTypeMapper
{
    void add(DictProductType dictProductType);

    void delete(Long id);

    void updateById(DictProductType dictProductType);

    DictProductType findById(Long id);

    List<DictProductType> getAll();

    DictProductType findByCode(String code);

    void updateSkipNull(DictProductType dictProductType);

    int countAll();
}
