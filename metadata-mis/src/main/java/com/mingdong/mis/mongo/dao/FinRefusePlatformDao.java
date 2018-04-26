package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinRefusePlatform;

import java.util.List;

public interface FinRefusePlatformDao
{
    List<FinRefusePlatform> findByPerson(String personId);
}
