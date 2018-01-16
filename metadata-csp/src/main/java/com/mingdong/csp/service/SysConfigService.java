package com.mingdong.csp.service;

import com.mingdong.core.model.BLResp;

public interface SysConfigService
{
    /**
     * 获取系统参数
     * @param name
     * @param resp
     */
    void getSysConfigByName(String name, BLResp resp);
}
