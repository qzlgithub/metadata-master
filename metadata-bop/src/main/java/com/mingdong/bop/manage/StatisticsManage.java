package com.mingdong.bop.manage;

import com.mingdong.core.model.RestResp;

import java.util.Map;

public interface StatisticsManage
{
    Map<String, Object> getSummaryStatistics();

    void getThirdRequestDiagramIn24h(RestResp resp);
}