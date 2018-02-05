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
import com.mingdong.mis.domain.entity.UserProduct;
import com.mingdong.mis.domain.mapper.ClientMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.UserProductMapper;
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
    private UserProductMapper userProductMapper;

    @Override
    @Transactional
    public void getClientAccessToken(String appId, String timestamp, String accessKey, String username, Integer refresh,
            MetadataRes res)
    {
        ClientProduct clientProduct = clientProductMapper.findByAppId(appId);
        if(clientProduct == null)
        {
            res.setResult(MetadataResult.RC_4);
            return;
        }
        Client client = clientMapper.findById(clientProduct.getClientId());
        ClientUser user;
        if(StringUtils.isNullBlank(username))
        {
            user = clientUserMapper.findById(client.getPrimaryUserId());
        }
        else
        {
            user = clientUserMapper.findByUsername(username);
            if(user == null || !TrueOrFalse.FALSE.equals(user.getDeleted()) || !TrueOrFalse.TRUE.equals(
                    user.getEnabled()) || !client.getId().equals(user.getClientId()))
            {
                res.setResult(MetadataResult.RC_5);
                return;
            }
        }
        if(!TrueOrFalse.TRUE.equals(user.getEnabled()))
        {
            res.setResult(MetadataResult.RC_8);
            return;
        }
        UserProduct userProduct = userProductMapper.findByUserAndProduct(user.getId(), clientProduct.getProductId());
        if(userProduct == null)
        {
            res.setResult(MetadataResult.RC_6);
            return;
        }
        if(!checkAccessKey(userProduct.getAppSecret(), timestamp, accessKey))
        {
            res.setResult(MetadataResult.RC_7);
            return;
        }
        String accessToken;
        Date validTime;
        if(!TrueOrFalse.TRUE.equals(refresh) && userProduct.getAccessToken() != null &&
                userProduct.getValidTime() != null && res.getTimestamp().before(userProduct.getValidTime()))
        {
            accessToken = userProduct.getAccessToken();
            validTime = userProduct.getValidTime();
        }
        else
        {
            accessToken = BusinessUtils.createAccessToken();
            validTime = BusinessUtils.getTokenValidTime(res.getTimestamp());
            // 清除历史凭证
            if(userProduct.getAccessToken() != null)
            {
                redisDao.dropUserAuth(userProduct.getAccessToken());
            }
            // 更新数据库中存储的access token
            UserProduct upUpd = new UserProduct();
            upUpd.setId(userProduct.getId());
            upUpd.setUpdateTime(res.getTimestamp());
            upUpd.setAccessToken(accessToken);
            upUpd.setValidTime(validTime);
            userProductMapper.updateSkipNull(upUpd);
        }
        // 计算token剩余有效时间（秒）
        long seconds = validTime.getTime() - res.getTimestamp().getTime() + 60;
        // 缓存用户接入凭证
        UserAuth userAuth = new UserAuth();
        userAuth.setProductId(clientProduct.getProductId());
        userAuth.setClientId(clientProduct.getClientId());
        userAuth.setUserId(user.getId());
        userAuth.setAppSecret(userProduct.getAppSecret());
        userAuth.setHost(userProduct.getReqHost());
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
