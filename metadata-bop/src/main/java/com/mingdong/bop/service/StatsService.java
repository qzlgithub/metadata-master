package com.mingdong.bop.service;

import com.mingdong.bop.model.BLResp;

public interface StatsService
{
    BLResp getIndexClientStats();

    void getIndexStats(BLResp resp);
}
