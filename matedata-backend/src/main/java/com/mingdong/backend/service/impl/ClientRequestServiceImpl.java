package com.mingdong.backend.service.impl;

import com.mingdong.backend.domain.entity.FailedRequestLog;
import com.mingdong.backend.domain.mapper.FailedRequestLogMapper;
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
    public void saveErrorRequest(Long timestamp, Long clientId, Long productId, QueryStatus queryStatus)
    {
        FailedRequestLog obj = new FailedRequestLog();
        obj.setRequestTime(new Date(timestamp));
        obj.setClientId(clientId);
        obj.setProductId(productId);
        obj.setStatus(queryStatus.getCode());
        failedRequestLogMapper.add(obj);
    }
}
