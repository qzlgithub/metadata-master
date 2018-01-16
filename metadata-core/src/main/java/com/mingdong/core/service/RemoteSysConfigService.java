package com.mingdong.core.service;

import com.mingdong.core.model.dto.SysConfigDTO;

public interface RemoteSysConfigService
{
    SysConfigDTO getSysConfigByName(String name);
}
