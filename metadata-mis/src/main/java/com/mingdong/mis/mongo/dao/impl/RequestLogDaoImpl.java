package com.mingdong.mis.mongo.dao.impl;

import com.mingdong.common.model.Page;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.mis.mongo.dao.RequestLogDao;
import com.mingdong.mis.mongo.entity.RequestLog;
import com.mingdong.mis.mongo.entity.RequestNumber;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public class RequestLogDaoImpl implements RequestLogDao
{
    @Resource(name = "transactionMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(RequestLog requestLog)
    {
        mongoTemplate.insert(requestLog);
    }

    @Override
    public long countByRequestTime(Date startTime, Date endTime)
    {
        Query query = new Query();
        setTimeRange(query, startTime, endTime);
        return mongoTemplate.count(query, RequestLog.class);
    }

    @Override
    public long countByParam(Long clientId, Long clientUserId, Long productId, Date startTime, Date endTime,
            Integer hit)
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
        if(productId != null)
        {
            query.addCriteria(Criteria.where("product_id").is(productId));
        }
        if(hit != null)
        {
            query.addCriteria(Criteria.where("hit").is(hit));
        }
        setTimeRange(query, startTime, endTime);
        return mongoTemplate.count(query, RequestLog.class);
    }

    @Override
    public long countByParam(String keyword, Long productId, Integer billPlan, Integer hit, Date startTime,
            Date endTime)
    {
        Query query = new Query();
        if(!StringUtils.isNullBlank(keyword))
        {
            Criteria criteria = new Criteria();
            query.addCriteria(criteria.orOperator(Criteria.where("corp_name").regex(keyword))
                    .orOperator(Criteria.where("request_username").regex(keyword)));
        }
        if(productId != null)
        {
            query.addCriteria(Criteria.where("product_id").is(productId));
        }
        if(billPlan != null)
        {
            query.addCriteria(Criteria.where("bill_plan").is(billPlan));
        }
        if(hit != null)
        {
            query.addCriteria(Criteria.where("hit").is(hit));
        }
        setTimeRange(query, startTime, endTime);
        return mongoTemplate.count(query, RequestLog.class);
    }

    @Override
    public BigDecimal sumFeeByParam(String keyword, Long productId, Integer billPlan, Date fromDate, Date toDate)
    {
        Criteria criteria = new Criteria();
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.group().sum("fee").as("fee"));
        AggregationResults<RequestLog> res = mongoTemplate.aggregate(aggregation, RequestLog.class, RequestLog.class);
        if(res.getUniqueMappedResult() != null)
        {
            return NumberUtils.centAmtToYuan(res.getUniqueMappedResult().getFee());
        }
        return new BigDecimal("0");
    }

    @Override
    public List<RequestLog> findByParam(String keyword, Long clientId, Long clientUserId, Long productId,
            Integer billPlan, Date startTime, Date endTime, Integer hit, Page page)
    {
        Query query = new Query();
        if(!StringUtils.isNullBlank(keyword))
        {
            Criteria criteria = new Criteria();
            query.addCriteria(criteria.orOperator(Criteria.where("corp_name").regex(keyword))
                    .orOperator(Criteria.where("request_username").regex(keyword)));
        }
        if(clientId != null)
        {
            query.addCriteria(Criteria.where("client_id").is(clientId));
        }
        if(clientUserId != null)
        {
            query.addCriteria(Criteria.where("client_user_id").is(clientUserId));
        }
        if(productId != null)
        {
            query.addCriteria(Criteria.where("product_id").is(productId));
        }
        if(billPlan != null)
        {
            query.addCriteria(Criteria.where("bill_plan").is(billPlan));
        }
        if(hit != null)
        {
            query.addCriteria(Criteria.where("hit").is(hit));
        }
        setTimeRange(query, startTime, endTime);
        query.skip((page.getPageNum() - 1) * page.getPageSize()).limit(page.getPageSize());
        query.with(new Sort(Sort.Direction.DESC, "timestamp", "_id")); // 按请求序号倒序
        return mongoTemplate.find(query, RequestLog.class);
    }

    @Override
    public List<RequestNumber> findRequestGroupCountByTime(Date startTime, Date endTime)
    {
        Criteria criteria = Criteria.where("timestamp");
        if(startTime != null)
        {
            criteria.gte(startTime);
        }
        if(endTime != null)
        {
            criteria.lt(endTime);
        }
        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria),
                Aggregation.project("client_id", "product_id", "hit"), Aggregation.group("client_id", "product_id",
                        "hit").count().as("count"), Aggregation.project("client_id", "product_id", "hit", "count")
                        .and("id")
                        .previousOperation(), Aggregation.sort(Sort.Direction.DESC, "count"));
        AggregationResults<RequestNumber> groupResults = mongoTemplate.aggregate(agg, "request_log",
                RequestNumber.class);
        return groupResults.getMappedResults();
    }

    @Override
    public List<RequestNumber> findRequestCountByTime(Date startTime, Date endTime)
    {
        Criteria criteria = Criteria.where("timestamp");
        if(startTime != null)
        {
            criteria.gte(startTime);
        }
        if(endTime != null)
        {
            criteria.lt(endTime);
        }
        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), Aggregation.project("hit"),
                Aggregation.group("hit").count().as("count"), Aggregation.project("hit", "count")
                        .and("id")
                        .previousOperation(), Aggregation.sort(Sort.Direction.DESC, "count"));
        AggregationResults<RequestNumber> groupResults = mongoTemplate.aggregate(agg, "request_log",
                RequestNumber.class);
        return groupResults.getMappedResults();
    }

    private void setTimeRange(Query query, Date startTime, Date endTime)
    {
        if(startTime != null || endTime != null)
        {
            Criteria criteria = Criteria.where("timestamp");
            if(startTime != null)
            {
                criteria.gte(startTime);
            }
            if(endTime != null)
            {
                criteria.lt(endTime);
            }
            query.addCriteria(criteria);
        }
    }
}
