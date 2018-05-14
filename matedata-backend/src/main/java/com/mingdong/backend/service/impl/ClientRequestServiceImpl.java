package com.mingdong.backend.service.impl;

import com.mingdong.backend.domain.entity.FailedRequestLog;
import com.mingdong.backend.domain.mapper.FailedRequestLogMapper;
import com.mingdong.backend.model.Traffic;
import com.mingdong.backend.service.ClientRequestService;
import com.mingdong.core.constant.QueryStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ClientRequestServiceImpl implements ClientRequestService
{
    @Resource
    private FailedRequestLogMapper failedRequestLogMapper;

    @Override
    @Transactional
    public void saveErrorRequest(Traffic traffic, QueryStatus queryStatus)
    {
        FailedRequestLog obj = new FailedRequestLog();
        obj.setRequestTime(new Date(traffic.getTimestamp()));
        obj.setClientId(traffic.getClientId());
        obj.setProductId(traffic.getProductId());
        obj.setStatus(queryStatus.getCode());
        failedRequestLogMapper.add(obj);
    }
}
