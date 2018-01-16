package com.mingdong.csp.service.impl;

import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.model.dto.SysConfigDTO;
import com.mingdong.core.service.RemoteSysConfigService;
import com.mingdong.csp.constant.Field;
import com.mingdong.csp.service.SysConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysConfigServiceImpl implements SysConfigService
{
    @Resource
    RemoteSysConfigService remoteSysConfigService;

    @Override
    public void getSysConfigByName(String name, BLResp resp)
    {
        SysConfigDTO sysConfigDTO = remoteSysConfigService.getSysConfigByName(name);
        if(sysConfigDTO.getResult() != RestResult.SUCCESS)
        {
            resp.result(sysConfigDTO.getResult());
            return;
        }
        resp.addData(Field.NAME, sysConfigDTO.getName());
        resp.addData(Field.VALUE, sysConfigDTO.getValue());
    }
}
