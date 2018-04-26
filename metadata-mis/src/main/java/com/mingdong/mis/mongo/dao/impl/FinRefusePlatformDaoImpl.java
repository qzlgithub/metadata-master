package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.mis.mongo.dao.FinRefusePlatformDao;
import com.mingdong.mis.mongo.entity.FinRefusePlatform;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class FinRefusePlatformDaoImpl implements FinRefusePlatformDao
{
    @Resource(name = "analysisMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public List<FinRefusePlatform> findByPerson(String personId)
    {
        return mongoTemplate.find(Query.query(Criteria.where("person_id").is(personId)), FinRefusePlatform.class);
    }
}
