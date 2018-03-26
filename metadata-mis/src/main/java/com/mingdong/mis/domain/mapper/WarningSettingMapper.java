package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.WarningSetting;

import java.util.List;

public interface WarningSettingMapper
{
    void updateSkipNull(WarningSetting warningSetting);

    WarningSetting findById(Long id);

    List<WarningSetting> getListAll();
}
