package com.mingdong.mis.service.impl;

import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.mis.domain.entity.ApiReq;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ProductRecharge;
import com.mingdong.mis.domain.mapper.ApiReqMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeMapper;
import com.mingdong.mis.service.ChargeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class ChargeServiceImpl implements ChargeService
{
    @Resource
    private ApiReqMapper apiReqMapper;
    @Resource
    private ProductRechargeMapper productRechargeMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void chargeAndLog(Long requestId, ClientProduct account, Long userId, ProductRecharge recharge,
            BillPlan billPlan, String ip, String thirdNo, boolean hit, Date date)
    {
        if(BillPlan.PER_USE == billPlan || (BillPlan.PER_HIT == billPlan && hit))
        {
            ProductRecharge rechargeUpd = new ProductRecharge();
            rechargeUpd.setId(recharge.getId());
            rechargeUpd.setUpdateTime(date);
            rechargeUpd.setBalance(recharge.getBalance().subtract(recharge.getUnitAmt()));
            productRechargeMapper.updateSkipNull(rechargeUpd);
        }

        ApiReq apiReq = new ApiReq();
        apiReq.setId(requestId);
        apiReq.setCreateTime(date);
        apiReq.setUpdateTime(date);
        apiReq.setRequestNo("RQ" + requestId);
        apiReq.setThirdNo(thirdNo);
        apiReq.setProductId(account.getProductId());
        apiReq.setClientId(account.getClientId());
        apiReq.setUserId(userId);
        apiReq.setRequestIp(ip);
        apiReq.setHit(hit ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        apiReq.setBillPlan(billPlan.getId());
        if(BillPlan.PER_USE == billPlan || BillPlan.PER_HIT == billPlan)
        {
            BigDecimal fee = new BigDecimal(0);
            if(BillPlan.PER_USE == billPlan || hit)
            {
                fee = recharge.getUnitAmt();
            }
            apiReq.setFee(fee);
            apiReq.setBalance(recharge.getBalance().subtract(fee));
        }
        apiReqMapper.add(apiReq);
    }
}
