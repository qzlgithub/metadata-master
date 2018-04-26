package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinRepaymentPlatform;

import java.util.List;

public interface FinRepaymentPlatformDao
{
    List<FinRepaymentPlatform> findByPerson(String personId);
}
