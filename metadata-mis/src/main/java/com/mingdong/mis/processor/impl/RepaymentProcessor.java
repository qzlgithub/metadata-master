package com.mingdong.mis.processor.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.metadata.RepaymentBO;
import com.mingdong.mis.model.metadata.RepaymentPlatformBO;
import com.mingdong.mis.model.vo.PersonVO;
import com.mingdong.mis.mongo.dao.FinRepaymentPlatformDao;
import com.mingdong.mis.mongo.dao.FinRepaymentUserDao;
import com.mingdong.mis.mongo.entity.FinRepaymentPlatform;
import com.mingdong.mis.mongo.entity.FinRepaymentUser;
import com.mingdong.mis.processor.IProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class RepaymentProcessor implements IProcessor<PersonVO>
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private BaseProcessor baseProcessor;
    @Resource
    private FinRepaymentUserDao finRepaymentUserDao;
    @Resource
    private FinRepaymentPlatformDao finRepaymentPlatformDao;

    @Override
    public Metadata<RepaymentBO> process(PersonVO payload)
    {
        Metadata<RepaymentBO> metadata = new Metadata<>();
        String personId = redisDao.findPersonByPhone(payload.getPhone());
        personId = baseProcessor.confirmPersonId(personId, payload.getPhone());
        if(personId == null)
        {
            metadata.setHit(false);
            return metadata;
        }
        FinRepaymentUser finRepaymentUser = finRepaymentUserDao.findByPerson(personId);
        if(finRepaymentUser != null)
        {
            RepaymentBO bo = new RepaymentBO();
            bo.setRepaymentAmountMax(finRepaymentUser.getRepaymentAmountMax());
            bo.setRepaymentDaysMax(finRepaymentUser.getRepaymentDaysMax());
            bo.setRepaymentEarliestDate(finRepaymentUser.getRepaymentEarliestDate());
            bo.setRepaymentLatestDate(finRepaymentUser.getRepaymentLatestDate());
            bo.setRepaymentPlatformToday(finRepaymentUser.getRepaymentPlatformToday());
            bo.setRepaymentPlatformTotal(finRepaymentUser.getRepaymentPlatformTotal());
            bo.setRepaymentPlatform3Days(finRepaymentUser.getRepaymentPlatform3Days());
            bo.setRepaymentPlatform7Days(finRepaymentUser.getRepaymentPlatform7Days());
            bo.setRepaymentPlatform15Days(finRepaymentUser.getRepaymentPlatform15Days());
            bo.setRepaymentPlatform30Days(finRepaymentUser.getRepaymentPlatform30Days());
            bo.setRepaymentPlatform60Days(finRepaymentUser.getRepaymentPlatform60Days());
            bo.setRepaymentPlatform90Days(finRepaymentUser.getRepaymentPlatform90Days());
            List<FinRepaymentPlatform> opList = finRepaymentPlatformDao.findByPerson(personId);
            if(!CollectionUtils.isEmpty(opList))
            {
                List<RepaymentPlatformBO> list = new ArrayList<>(opList.size());
                RepaymentPlatformBO op;
                for(FinRepaymentPlatform o : opList)
                {
                    op = new RepaymentPlatformBO();
                    op.setPlatformCode(o.getPlatformCode());
                    op.setPlatformType(o.getPlatformType());
                    op.setRepaymentEarliestTime(o.getRepaymentEarliestTime());
                    op.setRepaymentLatestTime(o.getRepaymentLatestTime());
                    list.add(op);
                }
                bo.setRepaymentPlatforms(list);
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
