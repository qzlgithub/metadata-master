package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinOverduePlatform;

import java.util.List;

public interface FinOverduePlatformDao
{
    List<FinOverduePlatform> findByPhone(String phone);
}
