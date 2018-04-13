package com.mingdong.backend.service;

import com.mingdong.core.model.dto.response.ResponseDTO;

import java.util.List;

public interface TrafficService
{
    ResponseDTO getStatsClientRequestCache(List<Long> clientIdList);

    ResponseDTO getStatsProductRequestCache(List<Long> productIdList);

    ResponseDTO getStatsProductRatio();
}
