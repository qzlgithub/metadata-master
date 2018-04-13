package com.mingdong.backend.service.rpc;

import com.mingdong.backend.domain.entity.WarningSetting;
import com.mingdong.backend.domain.mapper.WarningSettingMapper;
import com.mingdong.backend.service.WarningService;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.request.WarningSettingReqDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;
import com.mingdong.core.model.dto.response.WarningSettingResDTO;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WarningServiceImpl implements WarningService
{
    @Resource
    private WarningSettingMapper warningSettingMapper;

    @Override
    public ListDTO<WarningSettingResDTO> getWarningSettingList()
    {
        ListDTO<WarningSettingResDTO> listDTO = new ListDTO<>();
        List<WarningSetting> listAll = warningSettingMapper.getListAll();
        List<WarningSettingResDTO> dataList = new ArrayList<>();
        WarningSettingResDTO warningSettingResDTO;
        for(WarningSetting item : listAll)
        {
            warningSettingResDTO = new WarningSettingResDTO();
            warningSettingResDTO.setId(item.getId());
            warningSettingResDTO.setContent(item.getContent());
            warningSettingResDTO.setEnabled(item.getEnabled());
            warningSettingResDTO.setFileName(item.getFileName());
            warningSettingResDTO.setFilePath(item.getFilePath());
            warningSettingResDTO.setPlay(item.getPlay());
            warningSettingResDTO.setSend(item.getSend());
            warningSettingResDTO.setType(item.getType());
            warningSettingResDTO.setGeneralLimit(item.getGeneralLimit());
            warningSettingResDTO.setSeverityLimit(item.getSeverityLimit());
            warningSettingResDTO.setWarningLimit(item.getWarningLimit());
            dataList.add(warningSettingResDTO);
        }
        listDTO.setList(dataList);
        return listDTO;
    }

    @Override
    public WarningSettingResDTO getWarningSetting(Long id)
    {
        WarningSetting warningSetting = warningSettingMapper.findById(id);
        if(warningSetting == null)
        {
            return null;
        }
        WarningSettingResDTO warningSettingResDTO = new WarningSettingResDTO();
        warningSettingResDTO.setId(warningSetting.getId());
        warningSettingResDTO.setFileName(warningSetting.getFileName());
        warningSettingResDTO.setWarningLimit(warningSetting.getWarningLimit());
        warningSettingResDTO.setType(warningSetting.getType());
        warningSettingResDTO.setSeverityLimit(warningSetting.getSeverityLimit());
        warningSettingResDTO.setSend(warningSetting.getSend());
        warningSettingResDTO.setPlay(warningSetting.getPlay());
        warningSettingResDTO.setGeneralLimit(warningSetting.getGeneralLimit());
        warningSettingResDTO.setFilePath(warningSetting.getFilePath());
        warningSettingResDTO.setEnabled(warningSetting.getEnabled());
        warningSettingResDTO.setContent(warningSetting.getContent());
        return warningSettingResDTO;
    }

    @Override
    @Transactional
    public ResponseDTO updateWarningSetting(WarningSettingReqDTO warningSettingResDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        WarningSetting warningSetting = new WarningSetting();
        warningSetting.setId(warningSettingResDTO.getId());
        warningSetting.setUpdateTime(new Date());
        warningSetting.setFileName(warningSettingResDTO.getFileName());
        warningSetting.setFilePath(warningSettingResDTO.getFilePath());
        warningSetting.setSend(warningSettingResDTO.getSend());
        warningSetting.setPlay(warningSettingResDTO.getPlay());
        warningSetting.setGeneralLimit(warningSettingResDTO.getGeneralLimit());
        warningSetting.setSeverityLimit(warningSettingResDTO.getSeverityLimit());
        warningSetting.setWarningLimit(warningSettingResDTO.getWarningLimit());
        warningSettingMapper.updateSkipNull(warningSetting);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO changeWarningSettingStatus(Long id, Integer status)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        WarningSetting warningSetting = new WarningSetting();
        warningSetting.setId(id);
        warningSetting.setUpdateTime(new Date());
        warningSetting.setEnabled(status);
        warningSettingMapper.updateSkipNull(warningSetting);
        return responseDTO;
    }
}
