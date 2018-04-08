package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.mis.mongo.dao.PersonDao;
import com.mingdong.mis.mongo.entity.Person;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class PersonDaoImpl implements PersonDao
{
    @Resource(name = "analysisMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public Person findByMobile(String mobile)
    {
        return mongoTemplate.findOne(Query.query(Criteria.where("mobile").is(mobile)), Person.class);
    }
}
