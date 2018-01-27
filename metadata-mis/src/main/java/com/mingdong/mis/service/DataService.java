package com.mingdong.mis.service;

import com.mingdong.core.model.BLResp;

public interface DataService
{
    void getProductionData(String requestIp, Long productId, Long clientId, Long userId, String phone, BLResp resp);
}
