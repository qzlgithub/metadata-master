package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.WarningOut;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarningOutMapper
{
    void add(WarningOut warningOut);

    List<WarningOut> getListByWarningType(@Param("warningType") Integer warningType);

    int countByWarningType(@Param("warningType") Integer warningType);
}
