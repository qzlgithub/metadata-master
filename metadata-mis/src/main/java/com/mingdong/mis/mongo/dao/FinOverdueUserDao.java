package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinOverdueUser;

public interface FinOverdueUserDao
{
    FinOverdueUser findByPerson(String personId);
}
