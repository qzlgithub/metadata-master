package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.DictIndustry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictIndustryMapper
{
    void add(DictIndustry o);

    void updateSkipNull(DictIndustry o);

    DictIndustry findById(Long id);

    List<DictIndustry> getByParentAndStatus(@Param("parentId") Long parentId, @Param("enabled") Integer enabled);

    List<DictIndustry> getIndustryInfo();

    DictIndustry findByCode(String code);
}
