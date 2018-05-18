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

    public String confirmPersonId(String phone)
    {
        String personId = redisDao.findPersonByPhone(phone);
        if(personId == null)
        {
            Person person = personDao.findByMobile(phone);
            redisDao.setPersonCache(phone, person == null ? IProcessor.DEFAULT_PERSON_ID : person.getId());
            if(person != null)
            {
                personId = person.getId();
            }
        }
        else if(IProcessor.DEFAULT_PERSON_ID.equals(personId))
        {
            personId = null;
        }
        return personId;
    }
}
