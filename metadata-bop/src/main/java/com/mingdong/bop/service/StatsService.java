package com.mingdong.bop.service;

import com.mingdong.bop.constant.ScopeType;
import com.mingdong.common.model.Page;
import com.mingdong.core.model.BLResp;

public interface StatsService
{
    BLResp getIndexStats();

    BLResp getClientIndexStats();

    BLResp getClientList(ScopeType scopeTypeEnum, Page page);

}
