package com.mingdong.mis.processor.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.mis.component.RedisDao;
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
    private RedisDao redisDao;
    @Resource
    private BaseProcessor baseProcessor;
    @Resource
    private FinOverdueUserDao finOverdueUserDao;
    @Resource
    private FinOverduePlatformDao finOverduePlatformDao;

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
        FinOverdueUser finOverdueUser = finOverdueUserDao.findByPerson(personId);
        if(finOverdueUser != null)
        {
            OverdueBO bo = new OverdueBO();
            bo.setOverdueAmountMax(finOverdueUser.getOverdueAmountMax());
            bo.setOverdueDaysMax(finOverdueUser.getOverdueDaysMax());
            bo.setOverdueEarliestTime(finOverdueUser.getOverdueEarliestDate());
            bo.setOverdueLatestTime(finOverdueUser.getOverdueLatestDate());
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
            metadata.setHit(true);
            metadata.setData(bo);
        }
        else
        {
            metadata.setHit(false);
        }
        return metadata;
    }
}
