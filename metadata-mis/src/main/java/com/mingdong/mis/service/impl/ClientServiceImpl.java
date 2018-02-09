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
import com.mingdong.mis.domain.entity.Product;
import com.mingdong.mis.domain.entity.UserProduct;
import com.mingdong.mis.domain.mapper.ClientMapper;
import com.mingdong.mis.domain.mapper.ClientProductMapper;
import com.mingdong.mis.domain.mapper.ClientUserMapper;
import com.mingdong.mis.domain.mapper.ProductMapper;
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
            res.setResult(MetadataResult.RC_4);
            return;
        }
        Product product = productMapper.findById(prodAcct.getProductId());
        if(!TrueOrFalse.TRUE.equals(product.getEnabled()))
        {
            res.setResult(MetadataResult.RC_12);
            return;
        }
        Client client = clientMapper.findById(prodAcct.getClientId());
        // 校验主账号是否禁用
        ClientUser user = clientUserMapper.findById(client.getPrimaryUserId());
        if(!TrueOrFalse.TRUE.equals(user.getEnabled()))
        {
            res.setResult(MetadataResult.RC_14);
            return;
        }
        // 判断是否是子账号接入，如果是，则验证该子账号的有效性
        if(!StringUtils.isNullBlank(username))
        {
            user = clientUserMapper.findByUsername(username);
            if(user == null || !TrueOrFalse.FALSE.equals(user.getDeleted()) || !client.getId().equals(
                    user.getClientId()))
            {
                res.setResult(MetadataResult.RC_5);
                return;
            }
            else if(!TrueOrFalse.TRUE.equals(user.getEnabled()))
            {
                res.setResult(MetadataResult.RC_8);
                return;
            }
        }
        // 查询用户账号对于产品的安全配置信息
        UserProduct userProduct = userProductMapper.findByUserAndProduct(user.getId(), product.getId());
        if(userProduct == null)
        {
            res.setResult(MetadataResult.RC_6);
            return;
        }
        // 验证请求密钥
        //        if(!checkAccessKey(userProduct.getAppSecret(), timestamp, accessKey))
        //        {
        //            res.setResult(MetadataResult.RC_7);
        //            return;
        //        }
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
        userAuth.setProduct(product.getCode());
        userAuth.setAccountId(prodAcct.getId());
        userAuth.setClientId(prodAcct.getClientId());
        userAuth.setUserId(user.getId());
        userAuth.setAppSecret(userProduct.getAppSecret());
        userAuth.setValidIP(userProduct.getReqHost());
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
