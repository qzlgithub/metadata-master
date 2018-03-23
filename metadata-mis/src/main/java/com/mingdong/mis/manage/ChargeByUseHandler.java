package com.mingdong.mis.manage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.model.CheckResult;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.AbsPayload;
import com.mingdong.mis.model.vo.PhoneVO;
import com.mingdong.mis.processor.OverdueProcessor;
import com.mingdong.mis.service.ChargeService;
import com.mingdong.mis.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ChargeByUseHandler implements IChargeHandler
{
    private static Logger logger = LoggerFactory.getLogger(ChargeByUseHandler.class);
    @Resource
    private RedisDao redisDao;
    @Resource
    private DataService dataService;
    @Resource
    private ChargeService chargeService;
    @Resource
    private OverdueProcessor overdueProcessor;

    @Override
    public void work(AbsPayload payload, MDResp resp)
    {
        String lockId = RequestThread.getProduct().name() + "-C" + RequestThread.getClientId();
        String lockVal = StringUtils.getUuid();
        boolean locked = false;
        try
        {
            // 锁定产品账户
            locked = redisDao.lockProductAccount(lockId, lockVal);
            if(!locked)
            {
                resp.setResult(MDResult.SYSTEM_BUSY);
                return;
            }
            // 查询产品余额及计费方式
            CheckResult checkResult = chargeService.checkAccountAndBillPlan(RequestThread.getClientId(),
                    RequestThread.getProductId(), RequestThread.getBillPlan());
            if(checkResult.getResult() != MDResult.OK)
            {
                resp.setResult(checkResult.getResult());
                return;
            }
            Metadata metadata;
            switch(RequestThread.getProduct())
            {
                case CDK:
                    metadata = overdueProcessor.process((PhoneVO) payload);
                    break;
                default:
                    logger.warn("Invalid product: " + RequestThread.getProduct());
                    return;
            }
            String requestNo = dataService.chargeAndSaveRequestLog(RequestThread.getClientId(),
                    RequestThread.getUserId(), RequestThread.getProductId(), checkResult.getClientProductId(),
                    checkResult.getUnitAmt(), checkResult.getBalance(), RequestThread.getHost(), payload,
                    metadata.isHit(), resp.getTimestamp());
            resp.setStatus(metadata.isHit() ? TrueOrFalse.FALSE : TrueOrFalse.TRUE);
            resp.setRequestNo(requestNo);
            resp.setData((JSONObject) JSON.toJSON(metadata.getData()));
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
