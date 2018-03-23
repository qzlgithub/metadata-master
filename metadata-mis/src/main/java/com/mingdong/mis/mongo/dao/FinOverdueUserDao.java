package com.mingdong.mis.mongo.dao;

import com.mingdong.common.model.Page;
import com.mingdong.mis.mongo.entity.OverdueUser;

import java.util.Date;
import java.util.List;

public interface FinOverdueUserDao
{
    List<OverdueUser> findByParam(Long clientId, Long clientUserId, Date startTime, Date endTime, Page page);
}
