package com.mingdong.mis.processor.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.exception.MetadataDataBaseException;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.metadata.MultiRegisterBO;
import com.mingdong.mis.model.metadata.MultiRegisterPlatformBO;
import com.mingdong.mis.model.vo.PersonVO;
import com.mingdong.mis.mongo.dao.FinRegisterPlatformDao;
import com.mingdong.mis.mongo.dao.FinRegisterUserDao;
import com.mingdong.mis.mongo.entity.FinRegisterPlatform;
import com.mingdong.mis.mongo.entity.FinRegisterUser;
import com.mingdong.mis.processor.IProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class MultiRegisterProcessor implements IProcessor<PersonVO>
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private BaseProcessor baseProcessor;
    @Resource
    private FinRegisterUserDao finRegisterUserDao;
    @Resource
    private FinRegisterPlatformDao finRegisterPlatformDao;

    @Override
    public Metadata<MultiRegisterBO> process(PersonVO payload) throws Exception
    {
        Metadata<MultiRegisterBO> metadata = new Metadata<>();
        try
        {

            String personId = redisDao.findPersonByPhone(payload.getPhone());
            personId = baseProcessor.confirmPersonId(personId, payload.getPhone());
            if(personId == null)
            {
                metadata.setHit(false);
                return metadata;
            }
            FinRegisterUser user = finRegisterUserDao.findByPerson(personId);
            if(user != null)
            {
                MultiRegisterBO bo = new MultiRegisterBO();
                bo.setRegisterInOneDayMax(user.getRegisterInOneDayMax());
                bo.setRegisterEarliestDate(user.getRegisterEarliestDate());
                bo.setRegisterLatestDate(user.getRegisterLatestDate());
                bo.setRegisterPlatformTotal(user.getRegisterPlatformTotal());
                bo.setRegisterPlatformToday(user.getRegisterPlatformToday());
                bo.setRegisterPlatform3Days(user.getRegisterPlatform3Days());
                bo.setRegisterPlatform7Days(user.getRegisterPlatform7Days());
                bo.setRegisterPlatform15Days(user.getRegisterPlatform15Days());
                bo.setRegisterPlatform30Days(user.getRegisterPlatform30Days());
                bo.setRegisterPlatform60Days(user.getRegisterPlatform60Days());
                bo.setRegisterPlatform90Days(user.getRegisterPlatform90Days());
                List<FinRegisterPlatform> rpList = finRegisterPlatformDao.findByPerson(personId);
                if(!CollectionUtils.isEmpty(rpList))
                {
                    List<MultiRegisterPlatformBO> list = new ArrayList<>(rpList.size());
                    MultiRegisterPlatformBO rp;
                    for(FinRegisterPlatform o : rpList)
                    {
                        rp = new MultiRegisterPlatformBO();
                        rp.setPlatformCode(o.getPlatformCode());
                        rp.setPlatformType(o.getPlatformType());
                        rp.setRegisterDate(o.getRegisterDate());
                        list.add(rp);
                    }
                    bo.setRegisterPlatforms(list);
                }
                metadata.setHit(true);
                metadata.setData(bo);
            }
            else
            {
                metadata.setHit(false);
            }
        }
        catch(Exception e)
        {
            throw new MetadataDataBaseException("mongo error");
        }
        return metadata;
    }
}
