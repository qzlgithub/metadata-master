package com.mingdong.mis.service.impl;

import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.RechargeMapper;
import com.mingdong.mis.model.CheckResult;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.service.ChargeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ChargeServiceImpl implements ChargeService
{
    @Resource
    private RechargeMapper rechargeMapper;
    @Resource
    private ClientProductMapper clientProductMapper;

    @Override
    public CheckResult checkAccountAndBillPlan()
    {
        CheckResult result = new CheckResult();
        ClientProduct clientProduct = clientProductMapper.findById(RequestThread.getClientProductId());
        if(!RequestThread.getBillPlan().equals(clientProduct.getBillPlan()))
        {
            // 计费方式改变
            result.setResult(MDResult.SYSTEM_BUSY);
            return result;
        }
        Recharge recharge = rechargeMapper.findById(clientProduct.getLatestRechargeId());
        if(clientProduct.getBalance().compareTo(recharge.getUnitAmt()) < 0)
        {
            // 余额不足
            result.setResult(MDResult.INSUFFICIENT_BALANCE);
            return result;
        }
        result.setBalance(clientProduct.getBalance());
        result.setUnitAmt(recharge.getUnitAmt());
        return result;
    }
}
