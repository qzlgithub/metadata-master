package com.mingdong.mis.processor.impl;

import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.metadata.OverdueBO;
import com.mingdong.mis.model.vo.PersonVO;
import com.mingdong.mis.mongo.dao.FinBlacklistDao;
import com.mingdong.mis.mongo.entity.FinBlacklist;
import com.mingdong.mis.processor.IProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BlacklistProcessor implements IProcessor<PersonVO>
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private FinBlacklistDao finBlacklistDao;
    @Resource
    private BaseProcessor baseProcessor;

    @Override
    public Metadata<OverdueBO> process(PersonVO payload)
    {
        Metadata<OverdueBO> metadata = new Metadata<>();
        String personId = redisDao.findPersonByPhone(payload.getPhone());
        personId = baseProcessor.confirmPersonId(personId, payload.getPhone());
        if(personId == null)
        {
            metadata.setHit(false);
            return metadata;
        }
        FinBlacklist finBlacklist = finBlacklistDao.findByPerson(personId);
        metadata.setHit(finBlacklist != null);
        return metadata;
    }
}
