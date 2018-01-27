package com.mingdong.mis.service.impl;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.BLResp;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ProductRecharge;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeMapper;
import com.mingdong.mis.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;

@Service
public class DataServiceImpl implements DataService
{
    private static Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);
    @Resource
    private RedisDao redisDao;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private ProductRechargeMapper productRechargeMapper;

    @Override
    public void getProductionData(Long productId, Long clientId, Long userId, String phone, BLResp resp)
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
