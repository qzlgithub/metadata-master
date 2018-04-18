package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinRegisterPlatform;

import java.util.List;

public interface FinRegisterPlatformDao
{
    List<FinRegisterPlatform> findByPerson(String personId);
}
