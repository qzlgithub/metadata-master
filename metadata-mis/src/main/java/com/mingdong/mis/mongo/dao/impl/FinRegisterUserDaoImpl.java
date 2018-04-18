package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.mis.mongo.dao.FinRegisterUserDao;
import com.mingdong.mis.mongo.entity.FinRegisterUser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FinRegisterUserDaoImpl implements FinRegisterUserDao
{
    @Resource(name = "analysisMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public FinRegisterUser findByPerson(String personId)
    {
        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(personId)), FinRegisterUser.class);
    }
}
