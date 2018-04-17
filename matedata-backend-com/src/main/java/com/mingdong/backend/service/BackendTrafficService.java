package com.mingdong.backend.service;

import com.mingdong.backend.model.LineDiagramDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.response.RequestDetailResDTO;
import com.mingdong.core.model.dto.response.ResponseDTO;

import java.util.Date;
import java.util.List;

public interface BackendTrafficService
{
    ResponseDTO getStatsClientRequestCache(List<Long> clientIdList);

    LineDiagramDTO getStatsProductRequestCache(List<Long> productIdList, Date date);

    ResponseDTO getStatsProductRatio();

    ListDTO<RequestDetailResDTO> getProductRequestList(Integer size);

    ListDTO<RequestDetailResDTO> getClientRequestList(Integer size);
}
