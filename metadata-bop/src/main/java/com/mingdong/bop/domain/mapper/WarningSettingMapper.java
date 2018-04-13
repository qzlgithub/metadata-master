package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.WarningSetting;

import java.util.List;

public interface WarningSettingMapper
{
    void updateSkipNull(WarningSetting warningSetting);

    WarningSetting findById(Long id);

    List<WarningSetting> getListAll();
}
