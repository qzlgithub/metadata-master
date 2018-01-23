package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.DictIndustry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictIndustryMapper
{
    void add(DictIndustry dictIndustry);

    void updateById(DictIndustry dictIndustry);

    void updateSkipNull(DictIndustry industry);

    DictIndustry findById(Long id);

    List<DictIndustry> getByParentAndStatus(@Param("parentId") Long parentId, @Param("enabled") Integer enabled);

    List<DictIndustry> getIndustryInfo();

    int countAll();

    List<DictIndustry> getAll();

    DictIndustry findByCode(String code);
}
