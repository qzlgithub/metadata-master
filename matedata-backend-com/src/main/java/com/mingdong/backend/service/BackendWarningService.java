package com.mingdong.backend.service;

import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.WarningSettingReqDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.WarningSettingResDTO;

public interface BackendWarningService
{
    WarningSettingResDTO getWarningSetting(Long id);

    ResponseDTO updateWarningSetting(WarningSettingReqDTO warningSettingResDTO);

    ResponseDTO changeWarningSettingStatus(Long id, Integer status);

    ListDTO<WarningSettingResDTO> getWarningSettingList();
}
