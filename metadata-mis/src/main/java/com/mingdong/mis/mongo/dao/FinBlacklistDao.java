package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinBlacklist;

public interface FinBlacklistDao
{
    FinBlacklist findByPerson(String personId);
}
