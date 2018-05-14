package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.WarningPacifyInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WarningPacifyInfoMapper
{
    List<WarningPacifyInfo> getListBy(@Param("clientIds") List<Long> clientIds,
            @Param("errorStatus") Integer errorStatus, @Param("dispose") Integer dispose,
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    int countBy(@Param("clientIds") List<Long> clientIds, @Param("errorStatus") Integer errorStatus,
            @Param("dispose") Integer dispose, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    WarningPacifyInfo findById(Long id);
}
