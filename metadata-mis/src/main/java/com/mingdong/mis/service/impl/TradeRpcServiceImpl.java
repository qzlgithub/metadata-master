package com.mingdong.mis.service.impl;

import com.mingdong.common.util.NumberUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.model.dto.request.RechargeReqDTO;
import com.mingdong.core.model.dto.ResponseDTO;
import com.mingdong.core.service.TradeRpcService;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.MessageType;
import com.mingdong.mis.domain.entity.ClientMessage;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.domain.mapper.ClientMessageMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
import com.mingdong.mis.domain.mapper.RechargeMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

public class TradeRpcServiceImpl implements TradeRpcService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private RechargeMapper rechargeMapper;
    @Resource
    private ClientMessageMapper clientMessageMapper;
    @Resource
    private ClientProductMapper clientProductMapper;

    @Override
    @Transactional
    public ResponseDTO productRecharge(RechargeReqDTO reqDTO)
    {
        ResponseDTO respDTO = new ResponseDTO();
        ClientProduct clientProduct = clientProductMapper.findByClientAndProduct(reqDTO.getClientId(),
                reqDTO.getProductId());
        Product product = productMapper.findById(reqDTO.getProductId());
        MessageType messageType;
        if(TrueOrFalse.TRUE.equals(reqDTO.getRenew()))
        {
            // 续费
            messageType = MessageType.RENEW;
            // 检查产品是否存在，是否被禁用
            if(product == null || !TrueOrFalse.TRUE.equals(product.getEnabled()))
            {
                respDTO.setResult(RestResult.PRODUCT_NOT_EXIST);
                return respDTO;
            }
            // 检查客户是否已开通该产品
            if(clientProduct == null || !TrueOrFalse.TRUE.equals(clientProduct.getOpened()))
            {
                respDTO.setResult(RestResult.PRODUCT_NOT_OPEN);
                return respDTO;
            }
        }
        else
        {
            // 新开
            messageType = MessageType.NEW_OPEN;
            if(product == null || !TrueOrFalse.TRUE.equals(product.getEnabled()))
            {
                respDTO.setResult(RestResult.PRODUCT_NOT_EXIST);
                return respDTO;
            }
            if(TrueOrFalse.TRUE.equals(product.getCustom()) && clientProduct == null)
            {
                respDTO.setResult(RestResult.FORBID_TO_OPEN);
                return respDTO;
            }
            Recharge recharge = rechargeMapper.findByContractNo(reqDTO.getContractNo());
            if(recharge != null)
            {
                respDTO.setResult(RestResult.DUPLICATE_CONTRACT_CODE);
                return respDTO;
            }
        }
        String lockKey = product.getCode() + "-C" + reqDTO.getClientId();
        String lockValue = StringUtils.getUuid();
        boolean locked = false;
        try
        {
            // 锁定**客户产品账户**
            locked = redisDao.lockProductAccount(lockKey, lockValue);
            if(!locked)
            {
                respDTO.setResult(RestResult.INTERNAL_ERROR);
                return respDTO;
            }
            BigDecimal balance = new BigDecimal(0);
            clientProduct = clientProductMapper.findByClientAndProduct(reqDTO.getClientId(), reqDTO.getProductId());
            if(clientProduct != null && TrueOrFalse.TRUE.equals(clientProduct.getOpened()))
            {
                // 续费时，如上次充值按次计费，则记录余额
                if(!BillPlan.BY_TIME.equals(clientProduct.getBillPlan()))
                {
                    balance = clientProduct.getBalance();
                }
            }
            Date date = new Date();
            // 保存充值记录
            Recharge recharge = new Recharge();
            recharge.setCreateTime(date);
            recharge.setUpdateTime(date);
            recharge.setClientId(reqDTO.getClientId());
            recharge.setProductId(reqDTO.getProductId());
            recharge.setTradeNo(redisDao.getRechargeOrderNo());
            recharge.setContractNo(reqDTO.getContractNo());
            recharge.setBillPlan(reqDTO.getBillPlan());
            recharge.setRechargeType(reqDTO.getRechargeType());
            recharge.setAmount(reqDTO.getAmount());
            recharge.setRemark(reqDTO.getRemark());
            if(BillPlan.BY_TIME.equals(reqDTO.getBillPlan()))
            {
                recharge.setStartDate(reqDTO.getStartDate());
                recharge.setEndDate(reqDTO.getEndDate());
                recharge.setBalance(reqDTO.getAmount());
            }
            else
            {
                recharge.setUnitAmt(reqDTO.getUnitAmt());
                recharge.setBalance(balance.add(reqDTO.getAmount()));
            }
            recharge.setManagerId(reqDTO.getManagerId());
            rechargeMapper.add(recharge);
            // 更新客户产品账户
            if(clientProduct == null)
            {
                clientProduct = new ClientProduct();
                clientProduct.setCreateTime(date);
                clientProduct.setUpdateTime(date);
                clientProduct.setClientId(reqDTO.getClientId());
                clientProduct.setProductId(reqDTO.getProductId());
                clientProduct.setAppId(StringUtils.getUuid());
                clientProduct.setBillPlan(reqDTO.getBillPlan());
                clientProduct.setBalance(reqDTO.getAmount());
                clientProduct.setLatestRechargeId(recharge.getId());
                clientProduct.setOpened(TrueOrFalse.TRUE);
                clientProductMapper.add(clientProduct);
            }
            else
            {
                ClientProduct temp = new ClientProduct();
                temp.setId(clientProduct.getId());
                temp.setUpdateTime(date);
                temp.setBillPlan(reqDTO.getBillPlan());
                temp.setLatestRechargeId(recharge.getId());
                if(!TrueOrFalse.TRUE.equals(clientProduct.getOpened()))
                {
                    temp.setOpened(TrueOrFalse.TRUE);
                }
                if(BillPlan.BY_TIME.equals(reqDTO.getBillPlan()))
                {
                    temp.setBalance(reqDTO.getAmount());
                }
                else
                {
                    temp.setBalance(balance.add(reqDTO.getAmount()));
                }
                clientProductMapper.updateSkipNull(temp);
            }
            // 保存客户充值消息
            ClientMessage clientMessage = new ClientMessage();
            clientMessage.setCreateTime(date);
            clientMessage.setUpdateTime(date);
            clientMessage.setClientId(reqDTO.getClientId());
            clientMessage.setType(messageType.getId());
            clientMessage.setContent(String.format(messageType.getContent(), product.getName(),
                    NumberUtils.formatAmount(reqDTO.getAmount())));
            clientMessageMapper.add(clientMessage);
        }
        finally
        {
            if(locked)
            {
                redisDao.freeProductAccount(lockKey, lockValue);
            }
        }
        return respDTO;
    }
}
