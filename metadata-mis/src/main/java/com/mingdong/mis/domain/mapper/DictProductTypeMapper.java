package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.DictProductType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictProductTypeMapper
{
    void add(DictProductType dictProductType);

    void updateById(DictProductType dictProductType);

    DictProductType findById(Long id);

    DictProductType findByCode(String code);

    void updateSkipNull(DictProductType dictProductType);

    int countListByStatus(@Param("enabled") Integer enabled);

    List<DictProductType> getListByStatus(@Param("enabled") Integer enabled);
}
