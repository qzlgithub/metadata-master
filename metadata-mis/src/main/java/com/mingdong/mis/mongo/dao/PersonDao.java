package com.mingdong.mis.mongo.dao;

import com.mingdong.mis.mongo.entity.Person;

public interface PersonDao
{
    Person findByMobile(String mobile);
}
