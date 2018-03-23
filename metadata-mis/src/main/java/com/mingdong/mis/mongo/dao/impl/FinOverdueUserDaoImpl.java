package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.mis.mongo.dao.FinOverdueUserDao;
import com.mingdong.mis.mongo.entity.FinOverdueUser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class FinOverdueUserDaoImpl implements FinOverdueUserDao
{
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public FinOverdueUser findByPhone(String phone)
    {
        return mongoTemplate.findOne(Query.query(Criteria.where("phone").is(phone)), FinOverdueUser.class);
    }
}
