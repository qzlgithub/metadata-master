package com.mingdong.mis.data.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.constant.Charset;
import com.mingdong.common.util.MapUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.core.exception.MetadataAPIException;
import com.mingdong.core.exception.MetadataCoreException;
import com.mingdong.mis.model.metadata.Blacklist;
import com.mingdong.mis.util.HttpUtils;
import com.mingdong.mis.util.SignUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@Component
public class DSDataAPI
{
    private static final String GENERALIZE_BLACK_API = "https://api.dsdatas.com/blackData/fireGeneralizeBlack";
    private static final String MULTIPLE_APP_API = "https://api.dsdatas.com/credit/api/v1/query";
    private static final String MULTIPLE_APP_API_2 =
            "https://api.dsdatas.com/notify/fire/multipleApplications/report/v1";
    private static final String APP_KEY = "e7e552ec9d83114ee17b81fa04230200";
    private static final String SECURITY_KEY = "cb17fe4f427c8eaf232f37ab3cfa2ab6c204f670";
    private static Logger logger = LoggerFactory.getLogger(DSDataAPI.class);

    private static Map<String, String> getHeader()
    {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");
        return header;
    }

    public Blacklist callBlacklist(String idNo, String name, String phone) throws MetadataAPIException
    {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("name", name);
        body.put("idCard", idNo);
        body.put("phone", phone);
        Map<String, String> header = getHeader();
        header.put("auth_token", getAuthToken());
        try
        {
            HttpEntity entity = HttpUtils.postData(GENERALIZE_BLACK_API, header, JSON.toJSONString(body));
            String resp = EntityUtils.toString(entity, Charset.UTF_8);
            logger.info("DS API response: {}", resp);
            JSONObject res = JSON.parseObject(resp);
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
     */
    private String getAuthToken()
    {
        return null;
    }
}
