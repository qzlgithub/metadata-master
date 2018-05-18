package com.mingdong.mis.processor.impl;

import com.mingdong.core.exception.MetadataDataBaseException;
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
    private FinBlacklistDao finBlacklistDao;
    @Resource
    private BaseProcessor baseProcessor;

    @Override
    public Metadata<OverdueBO> process(PersonVO payload) throws Exception
    {
        Metadata<OverdueBO> metadata = new Metadata<>();
        try
        {
            String personId = baseProcessor.confirmPersonId(payload.getPhone());
            if(personId == null)
            {
                metadata.setHit(false);
                return metadata;
            }
            metadata.setHit(search(personId));
        }
        catch(Exception e)
        {
            throw new MetadataDataBaseException("mongo error");
        }
        return metadata;
    }

    public boolean search(String personId)
    {
        FinBlacklist finBlacklist = finBlacklistDao.findByPerson(personId);
        return finBlacklist != null;
    }
}
