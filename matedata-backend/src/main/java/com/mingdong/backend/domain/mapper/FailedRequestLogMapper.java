package com.mingdong.backend.domain.mapper;

import com.mingdong.backend.domain.entity.FailedRequestLog;
import com.mingdong.backend.domain.entity.FailedRequestLogCount;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface FailedRequestLogMapper
{
    void add(FailedRequestLog obj);

    List<FailedRequestLogCount> findListGroupByProductAndClient(@Param("fromDate") Date start,
            @Param("toDate") Date end);
}
