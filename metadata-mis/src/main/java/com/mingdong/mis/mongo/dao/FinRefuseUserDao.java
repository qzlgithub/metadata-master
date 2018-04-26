package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinRefuseUser;

public interface FinRefuseUserDao
{
    FinRefuseUser findByPerson(String personId);
}
