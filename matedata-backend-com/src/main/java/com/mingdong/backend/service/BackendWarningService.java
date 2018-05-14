package com.mingdong.backend.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.WarningManageReqDTO;
import com.mingdong.core.model.dto.request.WarningPacifyReqDTO;
import com.mingdong.core.model.dto.request.WarningSettingReqDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.WarningManageDetailResDTO;
import com.mingdong.core.model.dto.response.WarningManageResDTO;
import com.mingdong.core.model.dto.response.WarningOutResDTO;
import com.mingdong.core.model.dto.response.WarningPacifyInfoResDTO;
import com.mingdong.core.model.dto.response.WarningSettingResDTO;

import java.util.Date;
import java.util.List;

public interface BackendWarningService
{
    WarningSettingResDTO getWarningSetting(Long id);

    ResponseDTO updateWarningSetting(WarningSettingReqDTO warningSettingResDTO);

    ResponseDTO changeWarningSettingStatus(Long id, Integer status);

    List<WarningSettingResDTO> getWarningSettingList();

    ListDTO<WarningManageResDTO> getWarningManageInfoListByType(Integer type, Long managerId, Date fromDate,
            Date toDate, Integer dispose, Page page);

    ListDTO<WarningManageDetailResDTO> getWarningDetailListByManageId(Long manageId, Page page);

    WarningManageResDTO getWarningManageInfoById(Long id);

    ResponseDTO disposeWarningManage(WarningManageReqDTO warningManageReqDTO);

    List<WarningManageResDTO> getWarningDisposeManagerList();

    ListDTO<WarningOutResDTO> getWarningOutListByWarningType(Integer warningType, Page page);

    ListDTO<WarningPacifyInfoResDTO> getWarningPacifyInfoList(List<Long> clientIds, Integer errorStatus,
            Integer dispose, Date fromDate, Date toDate, Page page);

    ResponseDTO disposeWarningPacify(WarningPacifyReqDTO warningPacifyReqDTO);

    WarningPacifyInfoResDTO getWarningPacifyInfoById(Long id);

    List<WarningSettingResDTO> getWarningSettingListByWarningType(Integer warningType);

}
