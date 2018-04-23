package com.mingdong.backend.service;

import com.mingdong.core.constant.QueryStatus;

public interface ClientRequestService
{
    void saveErrorRequest(Long timestamp, Long clientId, Long productId, QueryStatus queryStatus);
}
