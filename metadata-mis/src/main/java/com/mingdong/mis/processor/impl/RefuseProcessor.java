package com.mingdong.mis.processor.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.exception.MetadataDataBaseException;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.metadata.RefuseBO;
import com.mingdong.mis.model.metadata.RefusePlatformBO;
import com.mingdong.mis.model.vo.PersonVO;
import com.mingdong.mis.mongo.dao.FinRefusePlatformDao;
import com.mingdong.mis.mongo.dao.FinRefuseUserDao;
import com.mingdong.mis.mongo.entity.FinRefusePlatform;
import com.mingdong.mis.mongo.entity.FinRefuseUser;
import com.mingdong.mis.processor.IProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class RefuseProcessor implements IProcessor<PersonVO>
{
    @Resource
    private BaseProcessor baseProcessor;
    @Resource
    private FinRefuseUserDao finRefuseUserDao;
    @Resource
    private FinRefusePlatformDao finRefusePlatformDao;

    @Override
    public Metadata<RefuseBO> process(PersonVO payload) throws Exception
    {
        Metadata<RefuseBO> metadata = new Metadata<>();
        try
        {
            String personId = baseProcessor.confirmPersonId(payload.getPhone());
            if(personId == null)
            {
                metadata.setHit(false);
                return metadata;
            }
            RefuseBO bo = search(personId);
            if(bo != null)
            {
                metadata.setHit(true);
                metadata.setData(bo);
            }
            else
            {
                metadata.setHit(false);
            }
            return metadata;
        }
        catch(Exception e)
        {
            throw new MetadataDataBaseException("mongo error");
        }
    }

    public RefuseBO search(String personId)
    {
        FinRefuseUser finRefuseUser = finRefuseUserDao.findByPerson(personId);
        if(finRefuseUser != null)
        {
            RefuseBO bo = new RefuseBO();
            bo.setRefuseAmountMax(finRefuseUser.getRefuseAmountMax());
            bo.setRefuseEarliestDate(finRefuseUser.getRefuseEarliestDate());
            bo.setRefuseLatestDate(finRefuseUser.getRefuseLatestDate());
            bo.setRefusePlatformToday(finRefuseUser.getRefusePlatformToday());
            bo.setRefusePlatformTotal(finRefuseUser.getRefusePlatformTotal());
            bo.setRefusePlatform3Days(finRefuseUser.getRefusePlatform3Days());
            bo.setRefusePlatform7Days(finRefuseUser.getRefusePlatform7Days());
            bo.setRefusePlatform15Days(finRefuseUser.getRefusePlatform15Days());
            bo.setRefusePlatform30Days(finRefuseUser.getRefusePlatform30Days());
            bo.setRefusePlatform60Days(finRefuseUser.getRefusePlatform60Days());
            bo.setRefusePlatform90Days(finRefuseUser.getRefusePlatform90Days());
            List<FinRefusePlatform> opList = finRefusePlatformDao.findByPerson(personId);
            if(!CollectionUtils.isEmpty(opList))
            {
                List<RefusePlatformBO> list = new ArrayList<>(opList.size());
                RefusePlatformBO op;
                for(FinRefusePlatform o : opList)
                {
                    op = new RefusePlatformBO();
                    op.setPlatformCode(o.getPlatformCode());
                    op.setPlatformType(o.getPlatformType());
                    op.setRefuseEarliestDate(o.getRefuseEarliestDate());
                    op.setRefuseLatestDate(o.getRefuseLatestDate());
                    list.add(op);
                }
                bo.setRefusePlatforms(list);
            }
            return bo;
        }
        return null;
    }
}
