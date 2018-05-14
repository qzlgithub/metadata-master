package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.WarningManageDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarningManageDetailMapper
{
    void addAll(List<WarningManageDetail> detailAddList);

    List<WarningManageDetail> getListByManageId(@Param("manageId") Long manageId);

    int countByManageId(@Param("manageId") Long manageId);
}
