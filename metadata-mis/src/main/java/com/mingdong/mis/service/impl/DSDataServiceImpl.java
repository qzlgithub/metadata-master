package com.mingdong.mis.service.impl;

import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.exception.MetadataAPIException;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.core.util.IDUtils;
import com.mingdong.mis.component.Param;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.constant.MetadataResult;
import com.mingdong.mis.data.DataAPIProcessor;
import com.mingdong.mis.domain.entity.ApiReq;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.entity.ProductRecharge;
import com.mingdong.mis.domain.mapper.ApiReqMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
import com.mingdong.mis.domain.mapper.ProductRechargeMapper;
import com.mingdong.mis.model.IMetadata;
import com.mingdong.mis.model.MetadataRes;
import com.mingdong.mis.model.vo.BlacklistVO;
import com.mingdong.mis.service.DSDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class DSDataServiceImpl implements DSDataService
{
    private static Logger logger = LoggerFactory.getLogger(DSDataServiceImpl.class);
    @Resource
    private Param param;
    @Resource
    private DataAPIProcessor dataAPIProcessor;
    @Resource
    private RedisDao redisDao;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private ProductRechargeMapper productRechargeMapper;
    @Resource
    private ApiReqMapper apiReqMapper;
    @Resource
    private ProductMapper productMapper;

    @Override
    @Transactional
    public void getBlacklistData(Long productId, Long clientId, Long userId, String ip, BlacklistVO request,
            MetadataRes res)
    {
        Product product = productMapper.findById(productId);
        if(product == null || !TrueOrFalse.TRUE.equals(product.getEnabled()))
        {
            res.setResult(MetadataResult.RC_12);
            return;
        }
        ClientProduct clientProduct = clientProductMapper.findByClientAndProduct(clientId, productId);
        String lockName = StringUtils.getUuid();
        try
        {
            // 锁定客户产品账户
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
                    res.setResult(MetadataResult.RC_10);
                    return;
                }
                IMetadata data = dataAPIProcessor.revokeDataAPI(product.getCode(), request);

                Long id = IDUtils.getApiReqId(param.getNodeId());
                String reqNo = "RQ" + id;
                ApiReq apiReq = new ApiReq();
                apiReq.setId(id);
                apiReq.setCreateTime(res.getTimestamp());
                apiReq.setUpdateTime(res.getTimestamp());
                apiReq.setRequestNo(reqNo);
                apiReq.setThirdNo(data.getRequestNo());
                apiReq.setProductId(productId);
                apiReq.setClientId(clientId);
                apiReq.setUserId(userId);
                apiReq.setRequestIp(ip);
                apiReq.setHit(data.isHit() ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
                apiReq.setBillPlan(billPlan.getId());
                if(BillPlan.REQ == billPlan)
                {
                    BigDecimal balance = productRecharge.getBalance().subtract(productRecharge.getUnitAmt());
                    apiReq.setFee(productRecharge.getUnitAmt());
                    apiReq.setBalance(balance);
                    ProductRecharge prUpd = new ProductRecharge();
                    prUpd.setId(productRecharge.getId());
                    prUpd.setUpdateTime(res.getTimestamp());
                    prUpd.setBalance(balance);
                    productRechargeMapper.updateSkipNull(prUpd);
                }
                else if(BillPlan.RES == billPlan)
                {
                    BigDecimal fee = new BigDecimal(0); // 本次请求的费用
                    if(data.isHit()) // 成功击中结果
                    {
                        fee = productRecharge.getUnitAmt();
                        ProductRecharge prUpd = new ProductRecharge();
                        prUpd.setId(productRecharge.getId());
                        prUpd.setUpdateTime(res.getTimestamp());
                        prUpd.setBalance(productRecharge.getBalance().subtract(fee));
                        productRechargeMapper.updateSkipNull(prUpd);
                    }
                    apiReq.setFee(fee);
                    apiReq.setBalance(productRecharge.getBalance().subtract(fee));
                }
                apiReqMapper.add(apiReq);
                Map<String, Object> result = data.response();
                result.put(Field.REQUEST_NO, reqNo);
                res.add(Field.RESULT, result);
            }
            else
            {
                logger.warn("Failed to lock client product account, client: {}, product: {}", clientId, productId);
                res.setResult(MetadataResult.RC_11);
            }
        }
        catch(MetadataAPIException e)
        {
            logger.error("Failed to revoke data api: {}", e.getMessage());
            res.setResult(MetadataResult.RC_11);
        }
        finally
        {
            redisDao.freeClientProduct(clientProduct.getId(), lockName);
        }
    }
}
