package com.mingdong.mis.processor.impl;

import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.ResCode;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.metadata.LoanBO;
import com.mingdong.mis.model.metadata.MultiRegisterBO;
import com.mingdong.mis.model.metadata.OverdueBO;
import com.mingdong.mis.model.metadata.RefuseBO;
import com.mingdong.mis.model.metadata.RepaymentBO;
import com.mingdong.mis.model.metadata.ReportBO;
import com.mingdong.mis.model.vo.PersonVO;
import com.mingdong.mis.processor.IProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ReportProcessor implements IProcessor<PersonVO>
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private BaseProcessor baseProcessor;
    @Resource
    private BlacklistProcessor blacklistProcessor;
    @Resource
    private OverdueProcessor overdueProcessor;
    @Resource
    private MultiRegisterProcessor multiRegisterProcessor;
    @Resource
    private LoanProcessor loanProcessor;
    @Resource
    private RepaymentProcessor repaymentProcessor;
    @Resource
    private RefuseProcessor refuseProcessor;

    @Override
    public Metadata<ReportBO> process(PersonVO payload)
    {
        Metadata<ReportBO> metadata = new Metadata<>();
        String personId = redisDao.findPersonByPhone(payload.getPhone());
        personId = baseProcessor.confirmPersonId(personId, payload.getPhone());
        if(personId == null)
        {
            metadata.setHit(false);
            return metadata;
        }
        ReportBO reportBO = new ReportBO();
        boolean isHit = false;
        Metadata<OverdueBO> blacklistBOMetadata = blacklistProcessor.process(payload);
        if(blacklistBOMetadata.isHit())
        {
            isHit = true;
            reportBO.setBlacklistCode(ResCode.NORMAL);
        }
        else
        {
            reportBO.setBlacklistCode(ResCode.NOT_HIT);
        }
        Metadata<OverdueBO> overdueBOMetadata = overdueProcessor.process(payload);
        if(overdueBOMetadata.isHit())
        {
            isHit = true;
            reportBO.setOverdueCode(ResCode.NORMAL);
            reportBO.setOverdueRes(overdueBOMetadata.getData());
        }
        else
        {
            reportBO.setOverdueCode(ResCode.NOT_HIT);
        }
        Metadata<MultiRegisterBO> multiRegisterBOMetadata = multiRegisterProcessor.process(payload);
        if(multiRegisterBOMetadata.isHit())
        {
            isHit = true;
            reportBO.setMultiRegisterCode(ResCode.NORMAL);
            reportBO.setMultiRegisterRes(multiRegisterBOMetadata.getData());
        }
        else
        {
            reportBO.setMultiRegisterCode(ResCode.NOT_HIT);
        }
        Metadata<LoanBO> loanBOMetadata = loanProcessor.process(payload);
        if(loanBOMetadata.isHit())
        {
            isHit = true;
            reportBO.setCreditableCode(ResCode.NORMAL);
            reportBO.setCreditableRes(loanBOMetadata.getData());
        }
        else
        {
            reportBO.setCreditableCode(ResCode.NOT_HIT);
        }
        Metadata<RepaymentBO> repaymentBOMetadata = repaymentProcessor.process(payload);
        if(repaymentBOMetadata.isHit())
        {
            isHit = true;
            reportBO.setFavourableCode(ResCode.NORMAL);
            reportBO.setFavourableRes(repaymentBOMetadata.getData());
        }
        else
        {
            reportBO.setFavourableCode(ResCode.NOT_HIT);
        }
        Metadata<RefuseBO> refuseBOMetadata = refuseProcessor.process(payload);
        if(refuseBOMetadata.isHit())
        {
            isHit = true;
            reportBO.setRejecteeCode(ResCode.NORMAL);
            reportBO.setRejecteeRes(refuseBOMetadata.getData());
        }
        else
        {
            reportBO.setRejecteeCode(ResCode.NOT_HIT);
        }
        metadata.setHit(isHit);
        return metadata;
    }
}
