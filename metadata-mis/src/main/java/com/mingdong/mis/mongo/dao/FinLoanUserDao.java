package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.FinLoanUser;

public interface FinLoanUserDao
{
    FinLoanUser findByPerson(String personId);
}
