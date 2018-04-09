package com.mingdong.mis.processor.impl;

import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.mongo.dao.PersonDao;
import com.mingdong.mis.mongo.entity.Person;
import com.mingdong.mis.processor.IProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BaseProcessor
{
    @Resource
    private PersonDao personDao;
    @Resource
    private RedisDao redisDao;

    public String confirmPersonId(String personId, String phone)
    {
        if(personId == null)
        {
            Person person = personDao.findByMobile(phone);
            if(person == null)
            {
                redisDao.setPersonCache(phone, IProcessor.DEFAULT_PERSON_ID);
            }
            else
            {
                personId = person.getId();
                redisDao.setPersonCache(phone, personId);
            }
            return personId;
        }
        else
        {
            return IProcessor.DEFAULT_PERSON_ID.equals(personId) ? null : personId;
        }
    }
}