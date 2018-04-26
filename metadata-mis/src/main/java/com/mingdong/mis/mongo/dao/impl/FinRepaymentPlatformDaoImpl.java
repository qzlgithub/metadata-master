package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.mis.mongo.dao.FinRepaymentPlatformDao;
import com.mingdong.mis.mongo.entity.FinRepaymentPlatform;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class FinRepaymentPlatformDaoImpl implements FinRepaymentPlatformDao
{
    @Resource(name = "analysisMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public List<FinRepaymentPlatform> findByPerson(String personId)
    {
        return mongoTemplate.find(Query.query(Criteria.where("person_id").is(personId)), FinRepaymentPlatform.class);
    }
}
