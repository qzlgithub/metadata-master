package com.mingdong.mis.data.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.constant.Charset;
import com.mingdong.common.util.MapUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.exception.MetadataAPIException;
import com.mingdong.core.exception.MetadataCoreException;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.model.DSAuthToken;
import com.mingdong.mis.model.metadata.Blacklist;
import com.mingdong.mis.util.HttpUtils;
import com.mingdong.mis.util.SignUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@Component
public class DSDataAPI
{
    private static final String AUTH_TOKEN_API = "https://api.dsdatas.com/credit/api/token";
    private static final String GENERALIZE_BLACK_API = "https://api.dsdatas.com/blackData/fireGeneralizeBlack";
    private static final String MULTIPLE_APP_API = "https://api.dsdatas.com/credit/api/v1/query";
    private static final String MULTIPLE_APP_API_2 =
            "https://api.dsdatas.com/notify/fire/multipleApplications/report/v1";

    private static final String APP_KEY = "e7e552ec9d83114ee17b81fa04230200";
    private static final String SECURITY_KEY = "cb17fe4f427c8eaf232f37ab3cfa2ab6c204f670";
    private static Logger logger = LoggerFactory.getLogger(DSDataAPI.class);

    @Resource
    private RedisDao redisDao;

    private static Map<String, String> getHeader()
    {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");
        return header;
    }

    private DSAuthToken obtainAuthToken() throws MetadataAPIException
    {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", APP_KEY);
        body.put("securityKey", SECURITY_KEY);
        body.put("refresh", 1);
        try
        {
            HttpEntity entity = HttpUtils.postData(AUTH_TOKEN_API, getHeader(), JSON.toJSONString(body));
            String resp = EntityUtils.toString(entity, Charset.UTF_8);
            logger.info("DS API response: {}", resp);
            JSONObject res = JSON.parseObject(resp);
            if(res.getIntValue("code") == 200)
            {
                JSONObject data = res.getJSONObject("data");
                return JSON.parseObject(data.toJSONString(), DSAuthToken.class);
            }
        }
        catch(Exception e)
        {
            logger.error("Failed to obtain DaSheng auth token: {}", e.getMessage());
        }
        throw new MetadataAPIException("error to revoke DaSheng data api");
    }

    public Blacklist callBlacklist(String idNo, String name, String phone) throws MetadataAPIException
    {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("name", name);
        body.put("idCard", idNo);
        body.put("phone", phone);
        Map<String, String> header = getHeader();
        header.put("auth_token", getAuthToken(false));
        try
        {
            HttpEntity entity = HttpUtils.postData(GENERALIZE_BLACK_API, header, JSON.toJSONString(body));
            String resp = EntityUtils.toString(entity, Charset.UTF_8);
            logger.info("DS API response: {}", resp);
            JSONObject res = JSON.parseObject(resp);
            if(res.getIntValue("code") == 102)
            {
                // 如果token过期则刷新后再请求一次
                header.put("auth_token", refreshAuthToken());
                entity = HttpUtils.postData(GENERALIZE_BLACK_API, header, JSON.toJSONString(body));
                resp = EntityUtils.toString(entity, Charset.UTF_8);
                logger.info("DS API response: {}", resp);
                res = JSON.parseObject(resp);
            }
            if(res.getIntValue("code") == 200)
            {
                String orderNo = res.getString("orderNo");
                JSONObject data = res.getJSONObject("res");
                Integer stat = data.getInteger("stat");
                Blacklist blacklist = new Blacklist();
                blacklist.setOrderNo(orderNo);
                blacklist.setHit(TrueOrFalse.TRUE.equals(stat) ? TrueOrFalse.TRUE : TrueOrFalse.FALSE);
                return blacklist;
            }
        }
        catch(Exception e)
        {
            logger.error("Failed to revoke DaSheng blacklist api: {}", e.getMessage());
        }
        throw new MetadataAPIException("error to revoke DaSheng data api");
    }

    public Blacklist callMultiAppApiS1(String idNo, String name, String phone)
    {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", APP_KEY);
        body.put("channelNo", "CH0426533894");
        body.put("interfaceName", "multipleApplications");
        body.put("timestamp", System.currentTimeMillis());
        Map<String, Object> payload = new HashMap<>();
        payload.put("idCard", idNo);
        payload.put("name", name);
        payload.put("phone", phone);
        Map<String, Object> signMap = new HashMap<>();
        signMap.putAll(body);
        signMap.putAll(payload);
        SortedMap sortedMap = MapUtils.sortKey(signMap);
        String orgStr = JSON.toJSONString(sortedMap);
        try
        {
            String sign = SignUtils.sign(orgStr, SECURITY_KEY);
            body.put("payload", payload);
            body.put("sign", sign);
            HttpEntity entity = HttpUtils.postData(MULTIPLE_APP_API, getHeader(), JSON.toJSONString(body));
            String resp = EntityUtils.toString(entity, "UTF-8");
            return JSON.parseObject(resp, Blacklist.class);
        }
        catch(Exception e)
        {
            logger.error("Error occurred while revoke dasheng data api");
        }
        return null;
    }

    public void callMultiAppApiS2(String orderNo) throws MetadataCoreException, IOException
    {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", APP_KEY);
        body.put("orderNo", orderNo);
        body.put("timestamp", System.currentTimeMillis());

        SortedMap sortedMap = MapUtils.sortKey(body);
        String orgStr = JSON.toJSONString(sortedMap);
        body.put("sign", SignUtils.sign(orgStr, SECURITY_KEY));

        HttpEntity entity = HttpUtils.get(MULTIPLE_APP_API_2, null, body);
        String resp = EntityUtils.toString(entity, "UTF-8");
        System.out.println(resp);
    }

    /**
     * 获取大圣数据的请求凭证
     *
     * @param refresh 是否强制刷新。false：先在缓存中查找，未找到才强制刷新；true：直接调用大圣数据接口获取新的请求凭证并缓存
     */
    private String getAuthToken(boolean refresh) throws MetadataAPIException
    {
        String token;
        if(!refresh)
        {
            token = redisDao.getDSAuthToken();
            if(StringUtils.isNullBlank(token))
            {
                // 如果缓存中未找到大圣数据的请求凭证，则强制刷新
                token = refreshAuthToken();
            }
        }
        else
        {
            token = refreshAuthToken();
        }
        return token;
    }

    /**
     * 强制刷新大圣数据的请求凭证
     */
    private String refreshAuthToken() throws MetadataAPIException
    {
        DSAuthToken authToken = obtainAuthToken();
        String token = authToken.getToken();
        Long expiration = authToken.getExpire();
        Long ex = (expiration - System.currentTimeMillis() - 600000) / 1000;
        redisDao.setDSAuthToken(token, ex);
        return token;
    }
}
