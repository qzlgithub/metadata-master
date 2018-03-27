package com.test.mis;

import com.mingdong.mis.MISApplication;
import com.mingdong.mis.mongo.dao.RequestLogDao;
import com.mingdong.mis.mongo.entity.RequestLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MISApplication.class)
public class RequestLogDaoTest
{
    @Resource
    private RequestLogDao requestLogDao;

    @Test
    public void insert()
    {
        Long id = System.currentTimeMillis();
        RequestLog requestLog = new RequestLog();
        requestLog.setClientId(id);
        requestLogDao.insert(requestLog);
    }
}
