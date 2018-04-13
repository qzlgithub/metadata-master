package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.WarningSetting;

import java.util.List;

public interface WarningSettingMapper
{
    void updateSkipNull(WarningSetting warningSetting);

    WarningSetting findById(Long id);

    List<WarningSetting> getListAll();
}
