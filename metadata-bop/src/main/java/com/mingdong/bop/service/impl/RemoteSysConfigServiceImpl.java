package com.mingdong.bop.service.impl;

import com.mingdong.bop.domain.entity.SysConfig;
import com.mingdong.bop.domain.mapper.SysConfigMapper;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.dto.SysConfigDTO;
import com.mingdong.core.service.RemoteSysConfigService;

import javax.annotation.Resource;

public class RemoteSysConfigServiceImpl implements RemoteSysConfigService
{
    @Resource
    private SysConfigMapper sysConfigMapper;

    @Override
    public SysConfigDTO getSysConfigByName(String name)
    {
        SysConfig sysConfig = sysConfigMapper.findByName(name);
        SysConfigDTO sysConfigDTO = new SysConfigDTO(RestResult.SUCCESS);
        sysConfigDTO.setId(sysConfig.getId());
        sysConfigDTO.setName(sysConfig.getName());
        sysConfigDTO.setValue(sysConfig.getValue());
        return sysConfigDTO;
    }
}
