package com.mingdong.mis.service.impl;

import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.domain.entity.Client;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ClientUser;
import com.mingdong.mis.domain.entity.ClientUserProduct;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.entity.Recharge;
import com.mingdong.mis.domain.mapper.ClientMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.ClientUserProductMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
import com.mingdong.mis.domain.mapper.RechargeMapper;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.UserAuth;
import com.mingdong.mis.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ClientServiceImpl implements ClientService
{
    @Resource
    private RedisDao redisDao;
    @Resource
    private ClientMapper clientMapper;
    @Resource
    private ClientUserMapper clientUserMapper;
    @Resource
    private ClientProductMapper clientProductMapper;
    @Resource
    private ClientUserProductMapper clientUserProductMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private RechargeMapper rechargeMapper;

    @Override
    @Transactional
    public void getClientAccessToken(String appId, String timestamp, String accessKey, String username, Integer refresh,
            MDResp res)
    {
        ClientProduct clientProduct = clientProductMapper.findByAppId(appId);
        if(clientProduct == null)
        {
            res.setResult(MDResult.APP_ID_NOT_EXIST); // AppID无效
            return;
        }
        Product product = productMapper.findById(clientProduct.getProductId());
        if(product == null || !TrueOrFalse.TRUE.equals(product.getEnabled()))
        {
            res.setResult(MDResult.PRODUCT_DISABLED); // 服务已禁用
            return;
        }
        // 校验客户账号是否禁用
        Client client = clientMapper.findById(clientProduct.getClientId());
        if(!TrueOrFalse.TRUE.equals(client.getEnabled()) || !TrueOrFalse.FALSE.equals(client.getDeleted()))
        {
            res.setResult(MDResult.CLIENT_ACCT_DISABLED); // 企业账号已被禁用
            return;
        }
        ClientUser user;
        // 判断是否是子账号接入，如果是，则验证该子账号的有效性
        if(!StringUtils.isNullBlank(username))
        {
            user = clientUserMapper.findByUsername(username);
            if(user == null || !TrueOrFalse.FALSE.equals(user.getDeleted()) || !client.getId().equals(
                    user.getClientId()))
            {
                res.setResult(MDResult.CLIENT_SUB_ACCT_NOT_EXIST); // 子账号不存在
                return;
            }
            else if(!TrueOrFalse.TRUE.equals(user.getEnabled()))
            {
                res.setResult(MDResult.CLIENT_SUB_ACCT_DISABLED); // 子账号已被禁用
                return;
            }
        }
        else
        {
            user = clientUserMapper.findById(client.getPrimaryUserId());
        }
        if(StringUtils.isNullBlank(user.getAppSecret()))
        {
            res.setResult(MDResult.SECRET_KEY_NOT_SET); // 未设置用户密钥
            return;
        }
        // 查询用户账号对于产品的安全配置信息
        ClientUserProduct clientUserProduct = clientUserProductMapper.findByUserAndProduct(user.getId(),
                product.getId());
        if(clientUserProduct == null || StringUtils.isNullBlank(clientUserProduct.getReqHost()))
        {
            res.setResult(MDResult.CLIENT_IP_NOT_SET);
            return;
        }
        // 验证请求密钥
        if(!checkAccessKey(user.getAppSecret(), timestamp, accessKey))
        {
            res.setResult(MDResult.INVALID_ACCESS_KEY);
            return;
        }
        String accessToken;
        Date validTime;
        if(!TrueOrFalse.TRUE.equals(refresh) && clientUserProduct.getAccessToken() != null &&
                clientUserProduct.getValidTime() != null && res.getTimestamp().before(clientUserProduct.getValidTime()))
        {
            accessToken = clientUserProduct.getAccessToken();
            validTime = clientUserProduct.getValidTime();
        }
        else
        {
            accessToken = BusinessUtils.createAccessToken();
            validTime = BusinessUtils.getTokenValidTime(res.getTimestamp());
            // 清除历史凭证
            if(clientUserProduct.getAccessToken() != null)
            {
                redisDao.dropUserAuth(clientUserProduct.getAccessToken());
            }
            // 更新数据库中存储的access token
            ClientUserProduct upUpd = new ClientUserProduct();
            upUpd.setId(clientUserProduct.getId());
            upUpd.setUpdateTime(res.getTimestamp());
            upUpd.setAccessToken(accessToken);
            upUpd.setValidTime(validTime);
            clientUserProductMapper.updateSkipNull(upUpd);
        }
        // 计算token剩余有效时间（秒）
        long seconds = validTime.getTime() - res.getTimestamp().getTime() + 60;
        // 缓存用户接入凭证
        UserAuth auth = new UserAuth();
        auth.setClientId(clientProduct.getClientId());
        auth.setUserId(user.getId());
        auth.setProductId(product.getId());
        auth.setClientProductId(clientProduct.getId());
        auth.setBillPlan(clientProduct.getBillPlan());
        if(BillPlan.BY_TIME.equals(clientProduct.getBillPlan()))
        {
            Recharge recharge = rechargeMapper.findById(clientProduct.getLatestRechargeId());
            auth.setStart(recharge.getStartDate().getTime());
            auth.setEnd(recharge.getEndDate().getTime());
        }
        auth.setProduct(product.getCode());
        auth.setSecretKey(user.getAppSecret());
        auth.setHost(clientUserProduct.getReqHost());
        redisDao.saveUserAuth(accessToken, auth, seconds);
        res.add(Field.ACCESS_TOKEN, accessToken);
        res.add(Field.EXPIRATION, validTime);
    }

    /**
     * 验证用户请求key的有效性
     */
    private boolean checkAccessKey(String secretKey, String timestamp, String accessKey)
    {
        String org = secretKey + ":" + timestamp;
        return accessKey.equals(Md5Utils.encrypt(org));
    }
}
