package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.mis.mongo.dao.FinRefuseUserDao;
import com.mingdong.mis.mongo.entity.FinRefuseUser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FinRefuseUserDaoImpl implements FinRefuseUserDao
{
    @Resource(name = "analysisMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public FinRefuseUser findByPerson(String personId)
    {
        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(personId)), FinRefuseUser.class);
    }
}
