package com.mingdong.mis.processor.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.exception.MetadataDataBaseException;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.metadata.LoanBO;
import com.mingdong.mis.model.metadata.LoanPlatformBO;
import com.mingdong.mis.model.vo.PersonVO;
import com.mingdong.mis.mongo.dao.FinLoanPlatformDao;
import com.mingdong.mis.mongo.dao.FinLoanUserDao;
import com.mingdong.mis.mongo.entity.FinLoanPlatform;
import com.mingdong.mis.mongo.entity.FinLoanUser;
import com.mingdong.mis.processor.IProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoanProcessor implements IProcessor<PersonVO>
{
    @Resource
    private BaseProcessor baseProcessor;
    @Resource
    private FinLoanUserDao finLoanUserDao;
    @Resource
    private FinLoanPlatformDao finLoanPlatformDao;

    @Override
    public Metadata<LoanBO> process(PersonVO payload) throws Exception
    {
        Metadata<LoanBO> metadata = new Metadata<>();
        try
        {
            String personId = baseProcessor.confirmPersonId(payload.getPhone());
            if(personId == null)
            {
                metadata.setHit(false);
                return metadata;
            }
            LoanBO bo = search(personId);
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

    public LoanBO search(String personId)
    {
        FinLoanUser finLoanUser = finLoanUserDao.findByPerson(personId);
        if(finLoanUser != null)
        {
            LoanBO bo = new LoanBO();
            bo.setLoanAmountMax(finLoanUser.getLoanAmountMax());
            bo.setLoanEarliestDate(finLoanUser.getLoanEarliestDate());
            bo.setLoanLatestDate(finLoanUser.getLoanLatestDate());
            bo.setLoanPlatformToday(finLoanUser.getLoanPlatformToday());
            bo.setLoanPlatformTotal(finLoanUser.getLoanPlatformTotal());
            bo.setLoanPlatform3Days(finLoanUser.getLoanPlatform3Days());
            bo.setLoanPlatform7Days(finLoanUser.getLoanPlatform7Days());
            bo.setLoanPlatform15Days(finLoanUser.getLoanPlatform15Days());
            bo.setLoanPlatform30Days(finLoanUser.getLoanPlatform30Days());
            bo.setLoanPlatform60Days(finLoanUser.getLoanPlatform60Days());
            bo.setLoanPlatform90Days(finLoanUser.getLoanPlatform90Days());
            List<FinLoanPlatform> opList = finLoanPlatformDao.findByPerson(personId);
            if(!CollectionUtils.isEmpty(opList))
            {
                List<LoanPlatformBO> list = new ArrayList<>(opList.size());
                LoanPlatformBO op;
                for(FinLoanPlatform o : opList)
                {
                    op = new LoanPlatformBO();
                    op.setPlatformCode(o.getPlatformCode());
                    op.setPlatformType(o.getPlatformType());
                    op.setLoanEarliestDate(o.getLoanEarliestDate());
                    op.setLoanLatestDate(o.getLoanLatestDate());
                    list.add(op);
                }
                bo.setLoanPlatforms(list);
            }
            return bo;
        }
        return null;
    }
}
