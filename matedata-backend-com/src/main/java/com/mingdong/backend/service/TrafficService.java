package com.mingdong.backend.service;

import com.mingdong.core.model.dto.response.ResponseDTO;

import java.util.Date;
import java.util.List;

public interface TrafficService
{
    void cleanTraffic(Date date);

    ResponseDTO getStatsClientRequestCache(List<Long> clientIdList);

    ResponseDTO getStatsProductRequestCache(List<Long> productIdList);
}
