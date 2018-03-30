package com.mingdong.mis.service.impl;

import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.domain.entity.Client;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ClientUser;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.mapper.ClientMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
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
    private ClientMapper clientMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ClientUserMapper clientUserMapper;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private RequestLogDao requestLogDao;

    @Override
    public String saveRequestLog(Long clientId, Long clientUserId, Long productId, String host, AbsPayload payload,
            boolean hit, Date timestamp)
    {
        // 保存请求记录并返回请求单号
        return insertRequestLog(clientId, clientUserId, productId, host, payload, hit, BillPlan.BY_TIME, null, null,
                timestamp);
    }

    @Override
    @Transactional
    public String chargeAndSaveRequestLog(Long clientId, Long clientUserId, Long productId, Long clientProductId,
            BillPlan billPlan, BigDecimal fee, BigDecimal balance, String host, AbsPayload payload, boolean hit,
            Date timestamp)
    {
        if(BillPlan.PER_USE != billPlan && BillPlan.PER_HIT != billPlan)
        {
            return null;
        }
        if(BillPlan.PER_HIT == billPlan && !hit)
        {
            fee = new BigDecimal("0");
        }
        // 更新产品账户余额
        ClientProduct clientProduct = new ClientProduct();
        clientProduct.setId(clientProductId);
        clientProduct.setUpdateTime(timestamp);
        clientProduct.setBalance(balance.subtract(fee));
        clientProductMapper.updateSkipNull(clientProduct);
        // 保存请求记录并返回请求单号
        return insertRequestLog(clientId, clientUserId, productId, host, payload, hit, billPlan, fee, balance,
                timestamp);
    }

    private String insertRequestLog(Long clientId, Long clientUserId, Long productId, String host, AbsPayload payload,
            boolean hit, BillPlan billPlan, BigDecimal fee, BigDecimal balance, Date timestamp)
    {
        String requestNo = redisDao.getAPIRequestNo(timestamp);
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestNo(requestNo);
        requestLog.setTimestamp(timestamp);
        requestLog.setClientId(clientId);
        requestLog.setClientUserId(clientUserId);
        requestLog.setProductId(productId);
        requestLog.setHost(host);
        requestLog.setRequestParams(payload);
        requestLog.setHit(hit ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
        requestLog.setBillPlan(billPlan.getId());
        if(billPlan != BillPlan.BY_TIME)
        {
            requestLog.setFee(NumberUtils.yuanToCent(fee));
            requestLog.setBalance(NumberUtils.yuanToCent(balance.subtract(fee)));
        }
        Client client = clientMapper.findById(clientId);
        requestLog.setCorpName(client.getCorpName());
        requestLog.setPrimaryUsername(client.getUsername());
        ClientUser requestUser = clientUserMapper.findById(clientUserId);
        requestLog.setRequestUsername(requestUser.getUsername());
        Product product = productMapper.findById(productId);
        requestLog.setProductName(product.getName());
        requestLogDao.insert(requestLog);
        return requestNo;
    }
}
