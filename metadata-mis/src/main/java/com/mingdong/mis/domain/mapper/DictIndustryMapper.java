package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.DictIndustry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictIndustryMapper
{
    void add(DictIndustry dictIndustry);

    void updateSkipNull(DictIndustry industry);

    DictIndustry findById(Long id);

    List<DictIndustry> getByParentAndStatus(@Param("parentId") Long parentId, @Param("enabled") Integer enabled);

    List<DictIndustry> getIndustryInfo();

    int countAll();

    List<DictIndustry> getAll();

    DictIndustry findByCode(String code);
}
