package com.mingdong.mis.service.impl;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.mis.constant.ResCode;
import com.mingdong.mis.domain.mapper.SistemMapper;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.metadata.LoanBO;
import com.mingdong.mis.model.metadata.MultiRegisterBO;
import com.mingdong.mis.model.metadata.OverdueBO;
import com.mingdong.mis.model.metadata.RefuseBO;
import com.mingdong.mis.model.metadata.RepaymentBO;
import com.mingdong.mis.model.metadata.ReportBO;
import com.mingdong.mis.processor.impl.BaseProcessor;
import com.mingdong.mis.processor.impl.BlacklistProcessor;
import com.mingdong.mis.processor.impl.LoanProcessor;
import com.mingdong.mis.processor.impl.MultiRegisterProcessor;
import com.mingdong.mis.processor.impl.OverdueProcessor;
import com.mingdong.mis.processor.impl.RefuseProcessor;
import com.mingdong.mis.processor.impl.RepaymentProcessor;
import com.mingdong.mis.processor.impl.ReportProcessor;
import com.mingdong.mis.service.DetectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DetectServiceImpl implements DetectService
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
    private RepaymentProcessor repaymentProcessor;
    @Resource
    private RefuseProcessor refuseProcessor;
    @Resource
    private LoanProcessor loanProcessor;
    @Resource
    private ReportProcessor reportProcessor;
    @Resource
    private SistemMapper sistemMapper;

    @Override
    public boolean isDetectionTokenValid(String token)
    {
        String testToken = sistemMapper.getTestToken();
        if(StringUtils.isNullBlank(testToken))
        {
            return false;
        }
        return testToken.equals(token);
    }

    @Override
    public void getBlacklistInfo(List<String> phones, MDResp resp)
    {
        Map<String, Object> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(phones))
        {
            for(String phone : phones)
            {
                String personId = baseProcessor.confirmPersonId(phone);
                boolean hit = personId != null && blacklistProcessor.search(personId);
                map.put(phone, hit ? ResCode.NORMAL : ResCode.NOT_HIT);
            }
        }
        resp.setResData(map);
    }

    @Override
    public void getOverdueInfo(List<String> phones, MDResp resp)
    {
        Map<String, Object> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(phones))
        {
            OverdueBO overdueBO;
            for(String phone : phones)
            {
                String personId = baseProcessor.confirmPersonId(phone);
                if(personId != null)
                {
                    overdueBO = overdueProcessor.search(personId);
                }
                else
                {
                    overdueBO = null;
                }
                map.put(phone, overdueBO);
            }
        }
        resp.setResData(map);
    }

    @Override
    public void getMultiRegisterInfo(List<String> phones, MDResp resp)
    {
        Map<String, Object> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(phones))
        {
            MultiRegisterBO multiRegisterBO;
            for(String phone : phones)
            {
                String personId = baseProcessor.confirmPersonId(phone);
                if(personId != null)
                {
                    multiRegisterBO = multiRegisterProcessor.search(personId);
                }
                else
                {
                    multiRegisterBO = null;
                }
                map.put(phone, multiRegisterBO);
            }
        }
        resp.setResData(map);
    }

    @Override
    public void getCreditableInfo(List<String> phones, MDResp resp)
    {
        Map<String, Object> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(phones))
        {
            LoanBO loanBO;
            for(String phone : phones)
            {
                String personId = baseProcessor.confirmPersonId(phone);
                if(personId != null)
                {
                    loanBO = loanProcessor.search(personId);
                }
                else
                {
                    loanBO = null;
                }
                map.put(phone, loanBO);
            }
        }
        resp.setResData(map);
    }

    @Override
    public void getFavourableInfo(List<String> phones, MDResp resp)
    {
        Map<String, Object> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(phones))
        {
            RepaymentBO repaymentBO;
            for(String phone : phones)
            {
                String personId = baseProcessor.confirmPersonId(phone);
                if(personId != null)
                {
                    repaymentBO = repaymentProcessor.search(personId);
                }
                else
                {
                    repaymentBO = null;
                }
                map.put(phone, repaymentBO);
            }
        }
        resp.setResData(map);
    }

    @Override
    public void getRejecteeInfo(List<String> phones, MDResp resp)
    {
        Map<String, Object> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(phones))
        {
            RefuseBO refuseBO;
            for(String phone : phones)
            {
                String personId = baseProcessor.confirmPersonId(phone);
                if(personId != null)
                {
                    refuseBO = refuseProcessor.search(personId);
                }
                else
                {
                    refuseBO = null;
                }
                map.put(phone, refuseBO);
            }
        }
        resp.setResData(map);
    }

    @Override
    public void getHologramInfo(List<String> phones, MDResp resp)
    {
        Map<String, Object> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(phones))
        {
            ReportBO rb;
            for(String phone : phones)
            {
                String personId = baseProcessor.confirmPersonId(phone);
                if(personId != null)
                {
                    rb = reportProcessor.search(personId);
                }
                else
                {
                    rb = null;
                }
                map.put(phone, rb);
            }
        }
        resp.setResData(map);
    }
}
