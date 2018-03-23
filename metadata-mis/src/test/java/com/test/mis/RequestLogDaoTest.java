package com.test.mis;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.constant.DateFormat;
import com.mingdong.common.util.DateUtils;
import com.mingdong.mis.MISApplication;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.mongo.dao.RequestLogDao;
import com.mingdong.mis.mongo.entity.RequestLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MISApplication.class)
public class RequestLogDaoTest
{
    private static final Long CLIENT_ID = 302508220918857729L;
    private static final String CORP_NAME = "IBM China";
    private static final String REQUEST_USERNAME = "ibm123456";

    private static final Long CLIENT_USER_ID = 302508220918857729L;
    private static final Long PRODUCT_ID = 8L;
    private static final String PRODUCT_NAME = "常欠客";

    private static final String TEMPLATE =
            "{\"requestNo\":\"RQ20180323000001\",\"timestamp\":234554365464,\"client_id\":302508220918857700,\"client_user_id\":302508220918857700,\"product_id\":8,\"host\":\"123.10.23.22\",\"hit\":1,\"bill_plan\":1,\"fee\":0,\"balance\":2939.09,\"corp_name\":\"IBM China\",\"request_username\":\"ibm123456\",\"primary_username\":\"ibm123456\",\"product_name\":\"常欠客\"}";
    @Resource
    private RedisDao redisDao;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private RequestLogDao requestLogDao;

    @Test
    public void insert()
    {
        Long clientId = Long.MAX_VALUE;
        System.out.println("........: " + clientId);
        mongoTemplate.dropCollection("request_log");
        RequestLog requestLog = JSON.parseObject(TEMPLATE, RequestLog.class);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -20);
        Random random = new Random();
        for(int i = 0; i < 20; i++)
        {
            calendar.add(Calendar.HOUR_OF_DAY, i * 2);
            Date date = calendar.getTime();
            requestLog.setRequestNo(redisDao.getAPIRequestNo(date));
            requestLog.setTimestamp(date);
            requestLog.setClientId(clientId);
            requestLog.setClientUserId(CLIENT_USER_ID);
            requestLog.setProductId(PRODUCT_ID);
            requestLog.setProductName(PRODUCT_NAME);
            requestLog.setCorpName(CORP_NAME);
            requestLog.setRequestUsername(REQUEST_USERNAME);
            requestLog.setFee(random.nextLong());
            requestLogDao.insert(requestLog);
        }
        long total = requestLogDao.countByParam(clientId, CLIENT_USER_ID, PRODUCT_ID, null, null);
        assert total == 20;
    }

    @Test
    public void countByParam() throws ParseException
    {
        Date startTime = DateUtils.parseToDate("2018-03-21 17:52:29", DateFormat.YYYY_MM_DD_HH_MM_SS);
        Date endTime = DateUtils.parseToDate("2018-03-21 17:56:06", DateFormat.YYYY_MM_DD_HH_MM_SS);
        long total = requestLogDao.countByParam("IBM", PRODUCT_ID, null, null, startTime, endTime);
        assert total == 2;
    }

    @Test
    public void sumFee()
    {
        BigDecimal fee = requestLogDao.sumFeeByParam("IBM", null, null, null, null);
        System.out.println(fee);
        assert fee != null;
    }
}
