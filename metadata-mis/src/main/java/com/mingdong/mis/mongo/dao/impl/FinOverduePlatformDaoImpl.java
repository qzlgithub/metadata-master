package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.mis.mongo.dao.FinOverduePlatformDao;
import com.mingdong.mis.mongo.entity.FinOverduePlatform;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class FinOverduePlatformDaoImpl implements FinOverduePlatformDao
{
    @Resource(name = "analysisMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public List<FinOverduePlatform> findByPhone(String phone)
    {
        return mongoTemplate.find(Query.query(Criteria.where("phone").is(phone)), FinOverduePlatform.class);
    }
}