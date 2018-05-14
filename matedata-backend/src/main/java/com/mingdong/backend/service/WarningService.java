package com.mingdong.backend.service;

import com.mingdong.backend.model.Traffic;
import com.mingdong.core.constant.QueryStatus;

public interface WarningService
{
    void verifyWarning(Traffic traffic, QueryStatus queryStatus);
}
