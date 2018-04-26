package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinRepaymentUser;

public interface FinRepaymentUserDao
{
    FinRepaymentUser findByPerson(String personId);
}
