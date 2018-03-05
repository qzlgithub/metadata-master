package com.mingdong.mis.service.impl;

import com.mingdong.common.util.Md5Utils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.util.BusinessUtils;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.Field;
import com.mingdong.mis.constant.MetadataResult;
import com.mingdong.mis.domain.entity.Client;
import com.mingdong.mis.domain.entity.ClientProduct;
import com.mingdong.mis.domain.entity.ClientUser;
import com.mingdong.mis.domain.entity.ClientUserProduct;
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.mapper.ClientMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.ClientUserProductMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
import com.mingdong.mis.model.MetadataRes;
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

    @Override
    @Transactional
    public void getClientAccessToken(String appId, String timestamp, String accessKey, String username, Integer refresh,
            MetadataRes res)
    {
        ClientProduct prodAcct = clientProductMapper.findByAppId(appId);
        if(prodAcct == null)
        {
            res.setResult(MetadataResult.RC_4); // AppID无效
            return;
        }
        Product product = productMapper.findById(prodAcct.getProductId());
        if(!TrueOrFalse.TRUE.equals(product.getEnabled()))
        {
            res.setResult(MetadataResult.RC_12); // 服务已禁用
            return;
        }
        // 校验客户账号是否禁用
        Client client = clientMapper.findById(prodAcct.getClientId());
        if(!TrueOrFalse.TRUE.equals(client.getEnabled()) || !TrueOrFalse.TRUE.equals(client.getDeleted()))
        {
            res.setResult(MetadataResult.RC_14); // 企业账号已被禁用
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
                res.setResult(MetadataResult.RC_5); // 无效的用户名
                return;
            }
            else if(!TrueOrFalse.TRUE.equals(user.getEnabled()))
            {
                res.setResult(MetadataResult.RC_8); // 账号已被禁用
                return;
            }
        }
        else
        {
            user = clientUserMapper.findById(client.getPrimaryUserId());
        }
        // 查询用户账号对于产品的安全配置信息
        ClientUserProduct clientUserProduct = clientUserProductMapper.findByUserAndProduct(user.getId(),
                product.getId());
        if(clientUserProduct == null)
        {
            res.setResult(MetadataResult.RC_6);
            return;
        }
        // 验证请求密钥
        if(!checkAccessKey(clientUserProduct.getAppSecret(), timestamp, accessKey))
        {
            res.setResult(MetadataResult.RC_7);
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
        UserAuth userAuth = new UserAuth();
        userAuth.setProduct(product.getCode());
        userAuth.setAccountId(prodAcct.getId());
        userAuth.setClientId(prodAcct.getClientId());
        userAuth.setUserId(user.getId());
        userAuth.setAppSecret(clientUserProduct.getAppSecret());
        userAuth.setValidIP(clientUserProduct.getReqHost());
        redisDao.saveUserAuth(accessToken, userAuth, seconds);
        res.add(Field.ACCESS_TOKEN, accessToken);
        res.add(Field.EXPIRATION, validTime);
    }

    /**
     * 验证用户请求key的有效性
     */
    private boolean checkAccessKey(String appSecret, String timestamp, String accessKey)
    {
        String org = appSecret + "-" + timestamp;
        return accessKey != null && accessKey.equals(Md5Utils.encrypt(org));
    }
}
