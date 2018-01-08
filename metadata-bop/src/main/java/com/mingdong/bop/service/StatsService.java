package com.mingdong.bop.service;

import com.mingdong.core.model.BLResp;

public interface StatsService
{
    BLResp getIndexClientStats();

    void getIndexStats(BLResp resp);
}
