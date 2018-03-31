package com.mingdong.mis.service.impl;

import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.domain.entity.Client;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ClientUser;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.domain.mapper.ClientMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
import com.mingdong.mis.domain.mapper.RechargeMapper;
import com.mingdong.mis.model.CheckResult;
import com.mingdong.mis.mongo.dao.RequestLogDao;
import com.mingdong.mis.mongo.entity.RequestLog;
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
    private RedisDao redisDao;
    @Resource
    private RequestLogDao requestLogDao;
    @Resource
    private ClientMapper clientMapper;
    @Resource
    private ClientUserMapper clientUserMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private RechargeMapper rechargeMapper;
    @Resource
    private ClientProductMapper clientProductMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String chargeAndLog(ClientProduct account, Long userId, Recharge recharge, BillPlan billPlan, String ip,
            String thirdNo, boolean hit, Date date)
    {
        if(BillPlan.PER_USE == billPlan || (BillPlan.PER_HIT == billPlan && hit))
        {
            Recharge rechargeUpd = new Recharge();
            rechargeUpd.setId(recharge.getId());
            rechargeUpd.setUpdateTime(date);
            rechargeUpd.setBalance(recharge.getBalance().subtract(recharge.getUnitAmt()));
            rechargeMapper.updateSkipNull(rechargeUpd);
        }
        String requestNo = redisDao.getRequestNo(date);
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestNo(requestNo);
        requestLog.setTimestamp(date);
        requestLog.setClientId(account.getClientId());
        requestLog.setClientUserId(userId);
        requestLog.setProductId(account.getProductId());
        requestLog.setHost(ip);
        requestLog.setRequestParams(null);
        requestLog.setHit(hit ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        requestLog.setBillPlan(billPlan.getId());
        if(billPlan == BillPlan.PER_USE)
        {
            requestLog.setFee(NumberUtils.yuanToCent(recharge.getUnitAmt()));
            requestLog.setBalance(NumberUtils.yuanToCent(account.getBalance().subtract(recharge.getUnitAmt())));
        }
        else if(billPlan == BillPlan.PER_HIT)
        {
            BigDecimal fee = hit ? recharge.getUnitAmt() : new BigDecimal("0");
            requestLog.setFee(NumberUtils.yuanToCent(fee));
            requestLog.setBalance(NumberUtils.yuanToCent(account.getBalance().subtract(fee)));
        }
        Client client = clientMapper.findById(account.getClientId());
        requestLog.setCorpName(client.getCorpName());
        requestLog.setPrimaryUsername(client.getUsername());
        ClientUser requestUser = clientUserMapper.findById(userId);
        requestLog.setRequestUsername(requestUser.getUsername());
        Product product = productMapper.findById(account.getProductId());
        requestLog.setProductName(product.getName());
        requestLogDao.insert(requestLog);
        return requestNo;
    }

    @Override
    @Transactional
    public void renewClientProduct(Recharge pr, ClientProduct cp)
    {
        rechargeMapper.add(pr);
        clientProductMapper.updateSkipNull(cp);
    }

    @Override
    public CheckResult checkAccountAndBillPlan(Long clientId, Long productId, Integer billPlan)
    {
        CheckResult result = new CheckResult();
        ClientProduct clientProduct = clientProductMapper.findByClientAndProduct(clientId, productId);
        if(!billPlan.equals(clientProduct.getBillPlan()))
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
        result.setClientProductId(clientProduct.getId());
        result.setBalance(clientProduct.getBalance());
        result.setUnitAmt(recharge.getUnitAmt());
        return result;
    }
}
