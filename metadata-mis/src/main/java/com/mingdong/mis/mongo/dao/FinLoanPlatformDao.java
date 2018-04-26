package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinLoanPlatform;

import java.util.List;

public interface FinLoanPlatformDao
{
    List<FinLoanPlatform> findByPerson(String personId);
}
