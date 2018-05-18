package com.mingdong.mis.processor.impl;

import com.mingdong.core.exception.MetadataDataBaseException;
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
    public Metadata<ReportBO> process(PersonVO payload) throws Exception
    {
        Metadata<ReportBO> metadata = new Metadata<>();
        try
        {
            String personId = baseProcessor.confirmPersonId(payload.getPhone());
            if(personId == null)
            {
                metadata.setHit(false);
                return metadata;
            }
            ReportBO reportBO = search(personId);
            if(reportBO == null)
            {
                metadata.setHit(false);
            }
            else
            {
                metadata.setHit(true);
                metadata.setData(reportBO);
            }
            return metadata;
        }
        catch(Exception e)
        {
            throw new MetadataDataBaseException("mongo error");
        }
    }

    public ReportBO search(String personId)
    {
        // 1. 黑名单
        boolean isHitBlacklist = blacklistProcessor.search(personId);
        // 2. 常欠客
        OverdueBO overdueBO = overdueProcessor.search(personId);
        // 3. 多头客
        MultiRegisterBO multiRegisterBO = multiRegisterProcessor.search(personId);
        // 4. 通过客
        LoanBO loanBO = loanProcessor.search(personId);
        // 5. 优良客
        RepaymentBO repaymentBO = repaymentProcessor.search(personId);
        // 6. 拒贷客
        RefuseBO refuseBO = refuseProcessor.search(personId);

        if(!isHitBlacklist && overdueBO == null && multiRegisterBO == null && loanBO == null && repaymentBO == null &&
                refuseBO == null)
        {
            return null;
        }
        ReportBO reportBO = new ReportBO();
        reportBO.setBlacklistCode(isHitBlacklist ? ResCode.NORMAL : ResCode.NOT_HIT);
        reportBO.setOverdueCode(overdueBO != null ? ResCode.NORMAL : ResCode.NOT_HIT);
        reportBO.setOverdueRes(overdueBO);
        reportBO.setMultiRegisterCode(multiRegisterBO != null ? ResCode.NORMAL : ResCode.NOT_HIT);
        reportBO.setMultiRegisterRes(multiRegisterBO);
        reportBO.setCreditableCode(loanBO != null ? ResCode.NORMAL : ResCode.NOT_HIT);
        reportBO.setCreditableRes(loanBO);
        reportBO.setFavourableCode(repaymentBO != null ? ResCode.NORMAL : ResCode.NOT_HIT);
        reportBO.setFavourableRes(repaymentBO);
        reportBO.setRejecteeCode(refuseBO != null ? ResCode.NORMAL : ResCode.NOT_HIT);
        reportBO.setRejecteeRes(refuseBO);
        return reportBO;
    }
}
