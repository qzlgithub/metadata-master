package com.mingdong.backend.service;

import com.mingdong.backend.model.Traffic;
import com.mingdong.core.constant.QueryStatus;

public interface ClientRequestService
{
    void saveErrorRequest(Traffic traffic, QueryStatus queryStatus);
}
