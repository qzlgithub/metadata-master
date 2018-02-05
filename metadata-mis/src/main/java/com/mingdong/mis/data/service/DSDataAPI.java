package com.mingdong.mis.data.service;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.constant.Charset;
import com.mingdong.core.exception.MetadataAPIException;
import com.mingdong.core.exception.MetadataHttpException;
import com.mingdong.mis.model.metadata.Blacklist;
import com.mingdong.mis.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class DSDataAPI
{
    private static Logger logger = LoggerFactory.getLogger(DSDataAPI.class);
    private static final String GENERALIZE_BLACK_API = "https://api.dsdatas.com/blackData/fireGeneralizeBlack";
    private static final String MULTIPLE_APP_API = "https://api.dsdatas.com/credit/api/v1/query";
    private static final String MULTIPLE_APP_API_2 =
            "https://api.dsdatas.com/notify/fire/multipleApplications/report/v1";

    private static final String APP_KEY = "e7e552ec9d83114ee17b81fa04230200";
    private static final String SECURITY_KEY = "cb17fe4f427c8eaf232f37ab3cfa2ab6c204f670";

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
            return JSON.parseObject(resp, Blacklist.class);
        }
        catch(MetadataHttpException | IOException e)
        {
            logger.error("Failed to revoke DaSheng blacklist api: {}", e.getMessage());
            throw new MetadataAPIException("error to revoke DaSheng data api");
        }
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
        String orgStr = sortMapToString(signMap);
        try
        {
            String sign = sign(orgStr);
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

    public void callMultiAppApiS2(String orderNo) throws InvalidKeyException, NoSuchAlgorithmException, IOException
    {
        Map<String, Object> body = new HashMap<>();
        body.put("apiKey", APP_KEY);
        body.put("orderNo", orderNo);
        body.put("timestamp", System.currentTimeMillis());
        String orgStr = sortMapToString(body);
        body.put("sign", sign(orgStr));

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

    private static Map<String, String> getHeader()
    {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;charset=UTF-8");
        return header;
    }

    private static String sortMapToString(Map<String, Object> map)
    {
        if(map != null)
        {
            List<String> keyList = new ArrayList<>(map.keySet());
            Collections.sort(keyList);
            SortedMap<String, Object> sortedMap = new TreeMap<>();
            for(String k : keyList)
            {
                sortedMap.put(k, map.get(k));
            }
            return JSON.toJSONString(sortedMap);
        }
        return null;
    }

    private static String sign(String data) throws NoSuchAlgorithmException, InvalidKeyException
    {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec spec = new SecretKeySpec(SECURITY_KEY.getBytes(), "HmacSHA256");
        hmacSHA256.init(spec);
        byte[] encData = hmacSHA256.doFinal(data.getBytes());
        return byteArrayToHexString(encData);
    }

    private static String byteArrayToHexString(byte[] data)
    {
        StringBuilder hs = new StringBuilder();
        String s;
        for(int n = 0; data != null && n < data.length; n++)
        {
            s = Integer.toHexString(data[n] & 0XFF);
            if(s.length() == 1)
            {
                hs.append('0');
            }
            hs.append(s);
        }
        return hs.toString().toLowerCase();
    }
}
