package com.mingdong.mis.service.impl;

import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.AbsPayload;
import com.mingdong.mis.mongo.dao.RequestLogDao;
import com.mingdong.mis.mongo.entity.RequestLog;
import com.mingdong.mis.service.DataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class DataServiceImpl implements DataService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private RequestLogDao requestLogDao;
    @Resource
    private ClientProductMapper clientProductMapper;

    @Override
    public String saveRequestLog(AbsPayload payload, boolean hit, Date timestamp)
    {
        // 保存请求记录并返回请求单号
        return insertRequestLog(payload, hit, null, null, timestamp);
    }

    @Override
    @Transactional
    public String chargeAndSaveRequestLog(BigDecimal fee, BigDecimal balance, AbsPayload payload, boolean hit,
            Date timestamp)
    {
        if(!BillPlan.PER_USE.equals(RequestThread.getBillPlan()) && !BillPlan.PER_HIT.equals(
                RequestThread.getBillPlan()))
        {
            return null;
        }
        if(BillPlan.PER_HIT.equals(RequestThread.getBillPlan()) && !hit)
        {
            fee = new BigDecimal("0");
        }
        // 更新产品账户余额
        ClientProduct clientProduct = new ClientProduct();
        clientProduct.setId(RequestThread.getClientProductId());
        clientProduct.setUpdateTime(timestamp);
        clientProduct.setBalance(balance.subtract(fee));
        clientProductMapper.updateSkipNull(clientProduct);
        // 保存请求记录并返回请求单号
        return insertRequestLog(payload, hit, fee, balance, timestamp);
    }

    private String insertRequestLog(AbsPayload payload, boolean hit, BigDecimal fee, BigDecimal balance, Date timestamp)
    {
        String requestNo = redisDao.getRequestNo(timestamp);
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestNo(requestNo);
        requestLog.setTimestamp(timestamp);
        requestLog.setClientId(RequestThread.getClientId());
        requestLog.setClientUserId(RequestThread.getUserId());
        requestLog.setProductId(RequestThread.getProductId());
        requestLog.setHost(RequestThread.getHost());
        requestLog.setRequestParams(payload);
        requestLog.setHit(hit ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        requestLog.setBillPlan(RequestThread.getBillPlan());
        if(!BillPlan.BY_TIME.equals(RequestThread.getBillPlan()))
        {
            requestLog.setFee(NumberUtils.yuanToCent(fee));
            requestLog.setBalance(NumberUtils.yuanToCent(balance.subtract(fee)));
        }
        requestLog.setCorpName(RequestThread.getCorpName());
        requestLog.setPrimaryUsername(RequestThread.getPrimaryUsername());
        requestLog.setRequestUsername(RequestThread.getUsername());
        requestLog.setProductName(RequestThread.getProductName());
        requestLogDao.insert(requestLog);
        return requestNo;
    }
}
