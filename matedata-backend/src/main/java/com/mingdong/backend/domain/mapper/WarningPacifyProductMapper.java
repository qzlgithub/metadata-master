package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.WarningPacifyProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarningPacifyProductMapper
{
    void addAll(List<WarningPacifyProduct> list);

    List<WarningPacifyProduct> getListByPacifyIds(@Param("list") List<Long> pacifyIds);

    List<WarningPacifyProduct> getListByPacifyId(Long pacifyId);
}
