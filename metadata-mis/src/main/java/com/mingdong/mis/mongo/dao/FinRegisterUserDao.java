package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinRegisterUser;

public interface FinRegisterUserDao
{
    FinRegisterUser findByPerson(String personId);
}
