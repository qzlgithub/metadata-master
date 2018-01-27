package com.mingdong.mis.service.impl;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.core.util.IDUtils;
import com.mingdong.mis.component.Param;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.domain.entity.ApiReq;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ProductRecharge;
import com.mingdong.mis.domain.mapper.ApiReqMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeMapper;
import com.mingdong.mis.model.api.APIData;
import com.mingdong.mis.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

@Service
public class DataServiceImpl implements DataService
{
    private static Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);
    @Resource
    private Param param;
    @Resource
    private RedisDao redisDao;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private ProductRechargeMapper productRechargeMapper;
    @Resource
    private ApiReqMapper apiReqMapper;

    @Override
    @Transactional
    public void getProductionData(String requestIp, Long productId, Long clientId, Long userId, String phone,
            BLResp resp)
    {

        ClientProduct clientProduct = clientProductMapper.findByClientAndProduct(clientId, productId);
        if(clientProduct == null || clientProduct.getLatestRechargeId() == null)
        {
            logger.warn("Cannot found client product data, clientId={}, productId={}", clientId, productId);
            resp.result(RestResult.INTERNAL_ERROR);
            return;
        }
        /*ProductRecharge productRecharge = productRechargeMapper.findById(clientProduct.getLatestRechargeId());
        if(productRecharge == null)
        {
            logger.warn("Data lost: table=product_recharge, id={}", clientProduct.getLatestRechargeId());
            resp.result(RestResult.INTERNAL_ERROR);
            return;
        }*/
        String lockName = StringUtils.getUuid();
        try
        {
            if(redisDao.lockClientProduct(clientProduct.getId(), lockName))
            {
                ProductRecharge productRecharge = productRechargeMapper.findById(clientProduct.getLatestRechargeId());
                // 验证账户有效性
                BillPlan billPlan = BillPlan.getById(productRecharge.getBillPlan());
                boolean isAcctValid;
                if(BillPlan.YEAR == billPlan)
                {
                    isAcctValid = BusinessUtils.checkAccountValid(productRecharge.getStartDate(),
                            productRecharge.getEndDate());
                }
                else
                {
                    isAcctValid = BusinessUtils.checkAccountValid(productRecharge.getUnitAmt(),
                            productRecharge.getBalance());
                }
                if(!isAcctValid)
                {
                    resp.result(RestResult.INSUFFICIENT_BALANCE);
                    return;
                }
                APIData apiData = new APIData(); // TODO 调用数据产品API
                Date current = new Date();
                ApiReq apiReq = new ApiReq();
                Long id = IDUtils.getApiReqId(param.getNodeId());
                apiReq.setId(id);
                apiReq.setCreateTime(current);
                apiReq.setUpdateTime(current);
                apiReq.setRequestNo("RQ" + id);
                apiReq.setProductId(productId);
                apiReq.setClientId(clientId);
                apiReq.setUserId(userId);
                apiReq.setRequestIp(requestIp);
                apiReq.setHit(apiData.hit() ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
                apiReq.setBillPlan(billPlan.getId());
                if(BillPlan.REQ == billPlan)
                {
                    BigDecimal balance = productRecharge.getBalance().subtract(productRecharge.getUnitAmt());
                    apiReq.setFee(productRecharge.getUnitAmt());
                    apiReq.setBalance(balance);
                    ProductRecharge prUpd = new ProductRecharge();
                    prUpd.setId(productRecharge.getId());
                    prUpd.setUpdateTime(current);
                    prUpd.setBalance(balance);
                    productRechargeMapper.updateSkipNull(prUpd);
                }
                else if(BillPlan.RES == billPlan)
                {
                    BigDecimal fee = new BigDecimal(0); // 本次请求的费用
                    if(apiData.hit()) // 成功击中结果
                    {
                        fee = productRecharge.getUnitAmt();
                        ProductRecharge prUpd = new ProductRecharge();
                        prUpd.setId(productRecharge.getId());
                        prUpd.setUpdateTime(current);
                        prUpd.setBalance(productRecharge.getBalance().subtract(fee));
                        productRechargeMapper.updateSkipNull(prUpd);
                    }
                    apiReq.setFee(fee);
                    apiReq.setBalance(productRecharge.getBalance().subtract(fee));
                }
                apiReqMapper.add(apiReq);
                resp.addData(Field.QUERY, apiData.getQuery());
                resp.addData(Field.RESULT, apiData.getResult());
            }
            else
            {
                resp.result(RestResult.INTERNAL_ERROR);
            }
        }
        catch(ParseException e)
        {
            resp.result(RestResult.INTERNAL_ERROR);
        }
        finally
        {
            redisDao.freeClientProduct(clientProduct.getId(), lockName);
        }
    }
}
