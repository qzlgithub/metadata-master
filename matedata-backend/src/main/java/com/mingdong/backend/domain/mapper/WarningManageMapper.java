package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.WarningManage;
import com.mingdong.backend.domain.entity.WarningManageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WarningManageMapper
{
    void add(WarningManage warningManage);

    int countByWarningType(@Param("warningType") Integer warningType, @Param("managerId") Long managerId,
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("dispose") Integer dispose);

    List<WarningManageInfo> getWarningManageListByWarningType(@Param("warningType") Integer warningType,
            @Param("managerId") Long managerId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
            @Param("dispose") Integer dispose);

    WarningManageInfo getWarningManageById(@Param("id") Long id);

    void updateSkipNull(WarningManage warningManage);

    List<WarningManage> getWarningDisposeManagerList();

}
