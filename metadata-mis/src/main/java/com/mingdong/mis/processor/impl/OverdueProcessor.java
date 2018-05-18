package com.mingdong.mis.processor.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.exception.MetadataDataBaseException;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.metadata.OverdueBO;
import com.mingdong.mis.model.metadata.OverduePlatformBO;
import com.mingdong.mis.model.vo.PersonVO;
import com.mingdong.mis.mongo.dao.FinOverduePlatformDao;
import com.mingdong.mis.mongo.dao.FinOverdueUserDao;
import com.mingdong.mis.mongo.entity.FinOverduePlatform;
import com.mingdong.mis.mongo.entity.FinOverdueUser;
import com.mingdong.mis.processor.IProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class OverdueProcessor implements IProcessor<PersonVO>
{
    @Resource
    private BaseProcessor baseProcessor;
    @Resource
    private FinOverdueUserDao finOverdueUserDao;
    @Resource
    private FinOverduePlatformDao finOverduePlatformDao;

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
            OverdueBO bo = search(personId);
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

    public OverdueBO search(String personId)
    {
        FinOverdueUser finOverdueUser = finOverdueUserDao.findByPerson(personId);
        if(finOverdueUser != null)
        {
            OverdueBO bo = new OverdueBO();
            bo.setOverdueAmountMax(finOverdueUser.getOverdueAmountMax());
            bo.setOverdueDaysMax(finOverdueUser.getOverdueDaysMax());
            bo.setOverdueEarliestTime(finOverdueUser.getOverdueEarliestTime());
            bo.setOverdueLatestTime(finOverdueUser.getOverdueLatestTime());
            bo.setOverduePlatformToday(finOverdueUser.getOverduePlatformToday());
            bo.setOverduePlatformTotal(finOverdueUser.getOverduePlatformTotal());
            bo.setOverduePlatform3Days(finOverdueUser.getOverduePlatform3Days());
            bo.setOverduePlatform7Days(finOverdueUser.getOverduePlatform7Days());
            bo.setOverduePlatform15Days(finOverdueUser.getOverduePlatform15Days());
            bo.setOverduePlatform30Days(finOverdueUser.getOverduePlatform30Days());
            bo.setOverduePlatform60Days(finOverdueUser.getOverduePlatform60Days());
            bo.setOverduePlatform90Days(finOverdueUser.getOverduePlatform90Days());
            List<FinOverduePlatform> opList = finOverduePlatformDao.findByPerson(personId);
            if(!CollectionUtils.isEmpty(opList))
            {
                List<OverduePlatformBO> list = new ArrayList<>(opList.size());
                OverduePlatformBO op;
                for(FinOverduePlatform o : opList)
                {
                    op = new OverduePlatformBO();
                    op.setPlatformCode(o.getPlatformCode());
                    op.setPlatformType(o.getPlatformType());
                    op.setOverdueEarliestTime(o.getOverdueEarliestTime());
                    op.setOverdueLatestTime(o.getOverdueLatestTime());
                    list.add(op);
                }
                bo.setOverduePlatforms(list);
            }
            return bo;
        }
        return null;
    }
}
