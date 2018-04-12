package com.mingdong.mis.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.util.StringUtils;
import com.mingdong.mis.component.MQProducer;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.constant.ResCode;
import com.mingdong.mis.handler.IChargeHandler;
import com.mingdong.mis.model.CheckResult;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.AbsPayload;
import com.mingdong.mis.service.ChargeService;
import com.mingdong.mis.service.DataService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 按使用次数计费
 */
@Component
public class ChargeByUseHandler implements IChargeHandler
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private MQProducer mqProducer;
    @Resource
    private DataService dataService;
    @Resource
    private ChargeService chargeService;
    @Resource
    private RouteHandler routeHandler;

    @Override
    public void work(AbsPayload payload, MDResp resp)
    {
        mqProducer.userRequest(RequestThread.getClientId(), RequestThread.getCorpName(), RequestThread.getProductId(),
                RequestThread.getProductName(), RequestThread.getHost(), payload, resp.getTimestamp());
        String lockId = RequestThread.getProduct().name() + "-C" + RequestThread.getClientId();
        String lockVal = StringUtils.getUuid();
        boolean locked = false;
        try
        {
            // 锁定产品账户
            locked = redisDao.lockProductAccount(lockId, lockVal);
            if(!locked)
            {
                resp.response(MDResult.SYSTEM_BUSY);
                return;
            }
            // 查询产品余额及计费方式
            CheckResult checkResult = chargeService.checkAccountAndBillPlan();
            if(checkResult.getResult() != MDResult.OK)
            {
                resp.response(checkResult.getResult());
                return;
            }
            Metadata metadata = routeHandler.routeProcessor(payload);
            // 请求计费，保存查询记录，并返回请求编号
            String requestNo = dataService.chargeAndSaveRequestLog(checkResult.getUnitAmt(), checkResult.getBalance(),
                    payload, metadata.isHit(), resp.requestAt());
            resp.setRequestNo(requestNo);
            if(metadata.isHit())
            {
                resp.setResCode(ResCode.NORMAL);
                resp.setResData((JSONObject) JSON.toJSON(metadata.getData()));
            }
            else
            {
                resp.setResCode(ResCode.NOT_HIT);
            }
        }
        finally
        {
            if(locked)
            {
                redisDao.freeProductAccount(lockId, lockVal);
            }
        }
    }
}
