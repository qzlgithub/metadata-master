package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.common.model.Page;
import com.mingdong.mis.mongo.dao.FinOverdueUserDao;
import com.mingdong.mis.mongo.entity.OverdueUser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class FinOverdueUserDaoImpl implements FinOverdueUserDao
{
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<OverdueUser> findByParam(Long clientId, Long clientUserId, Date startTime, Date endTime, Page page)
    {
        Query query = new Query();
        if(clientId != null)
        {
            query.addCriteria(Criteria.where("client_id").is(clientId));
        }
        if(clientUserId != null)
        {
            query.addCriteria(Criteria.where("client_user_id").is(clientUserId));
        }
        if(startTime != null)
        {
            query.addCriteria(Criteria.where("timestamp").gte(startTime));
        }
        if(endTime != null)
        {
            query.addCriteria(Criteria.where("timestamp").lt(endTime));
        }
        long total = mongoTemplate.count(query, OverdueUser.class);
        System.out.println("total: " + total);
        query.skip(page.getPageNum() * page.getPageSize()).limit(page.getPageSize());
        return mongoTemplate.find(query, OverdueUser.class);
    }
}
