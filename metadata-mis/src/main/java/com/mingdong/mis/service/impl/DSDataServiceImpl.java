package com.mingdong.mis.service.impl;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.exception.MetadataAPIException;
import com.mingdong.core.exception.MetadataCoreException;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.APIProduct;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.data.DataAPIProcessor;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.RechargeMapper;
import com.mingdong.mis.model.IMetadata;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.vo.AbsPayload;
import com.mingdong.mis.service.ChargeService;
import com.mingdong.mis.service.DSDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class DSDataServiceImpl implements DSDataService
{
    private static final Logger logger = LoggerFactory.getLogger(DSDataServiceImpl.class);
    @Resource
    private RedisDao redisDao;
    @Resource
    private ChargeService chargeService;
    @Resource
    private DataAPIProcessor dataAPIProcessor;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private RechargeMapper rechargeMapper;

    @Override
    public <T extends AbsPayload> void getData(APIProduct product, Long accountId, Long clientId, Long userId,
            String ip, T req, MDResp res)
    {
        String lockAccount = product.name() + "-C" + clientId;
        String lockUUID = StringUtils.getUuid();
        boolean locked = false;
        try
        {
            // 锁定产品账户
            locked = redisDao.lockProductAccount(lockAccount, lockUUID);
            if(!locked)
            {
                logger.warn("Failed to lock product account, client: {}, product: {}", clientId, product.name());
                res.setResult(MDResult.SYSTEM_BUSY);
                return;
            }
            // 查询计费方式，如果按时间计费则即时释放账户锁
            ClientProduct account = clientProductMapper.findById(accountId);
            BillPlan billPlan = BillPlan.getById(account.getBillPlan());
            if(BillPlan.BY_TIME == billPlan)
            {
                redisDao.freeProductAccount(lockAccount, lockUUID);
                locked = false;
            }
            // 查询当前有效的充值记录，并验证账户有效性
            Recharge recharge = rechargeMapper.findById(account.getLatestRechargeId());
            if(!checkAccountIsAvailable(recharge, res.requestAt()))
            {
                res.setResult(MDResult.INSUFFICIENT_BALANCE);
                return;
            }
            // 调取数据API
            IMetadata data = dataAPIProcessor.revokeDataAPI(product, req);
            if(data.isSaveLog())
            {
                // 保存本次请求记录，并更新产品账余额
                String requestNo = chargeService.chargeAndLog(account, userId, recharge, billPlan, ip,
                        data.getThirdNo(), data.isHit(), res.requestAt());
                res.add(Field.REQUEST_NO, requestNo);
            }
            res.addAll(data.response());
        }
        catch(MetadataAPIException | MetadataCoreException e)
        {
            logger.error("Failed to revoke data api: {}", e.getMessage());
            res.setResult(MDResult.SYSTEM_BUSY);
        }
        finally
        {
            if(locked)
            {
                redisDao.freeProductAccount(lockAccount, lockUUID);
            }
        }
    }

    private boolean checkAccountIsAvailable(Recharge recharge, Date current)
    {
        BillPlan billPlan = BillPlan.getById(recharge.getBillPlan());
        if(BillPlan.BY_TIME == billPlan)
        {
            long time = current.getTime();
            long start = recharge.getStartDate().getTime();
            long end = recharge.getEndDate().getTime() + 86400000L;
            return time >= start && time < end;
        }
        else if(BillPlan.PER_USE == billPlan || BillPlan.PER_HIT == billPlan)
        {
            return recharge.getBalance().doubleValue() >= recharge.getUnitAmt().doubleValue();
        }
        return false;
    }
}
