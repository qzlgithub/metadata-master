package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.mis.mongo.dao.FinOverdueUserDao;
import com.mingdong.mis.mongo.entity.FinOverdueUser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class FinOverdueUserDaoImpl implements FinOverdueUserDao
{
    @Resource(name = "analysisMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public FinOverdueUser findByPerson(String personId)
    {
        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(personId)), FinOverdueUser.class);
    }
}
