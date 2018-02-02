package com.mingdong.mis.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

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

public class DSDataAPI
{
    private static final String GENERALIZE_BLACK_API = "https://api.dsdatas.com/blackData/fireGeneralizeBlack";
    private static final String MULTIPLE_APP_API = "https://api.dsdatas.com/credit/api/v1/query";
    private static final String MULTIPLE_APP_API_2 =
            "https://api.dsdatas.com/notify/fire/multipleApplications/report/v1";

    private static final String APP_KEY = "e7e552ec9d83114ee17b81fa04230200";
    private static final String SECURITY_KEY = "cb17fe4f427c8eaf232f37ab3cfa2ab6c204f670";

    public static void callMultiAppApiS1(String idNo, String name, String phone)
            throws InvalidKeyException, NoSuchAlgorithmException, IOException
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
        String sign = sign(orgStr);

        body.put("payload", payload);
        body.put("sign", sign);

        HttpEntity entity = HttpUtils.postData(MULTIPLE_APP_API, getHeader(), JSON.toJSONString(body));
        String resp = EntityUtils.toString(entity, "UTF-8");
        System.out.println(resp);
    }

    public static void callMultiAppApiS2(String orderNo)
            throws InvalidKeyException, NoSuchAlgorithmException, IOException
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

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, IOException
    {
        // callMultiAppApiS1("330721198910115417", "祝俊", "18868429798");
        //        callMultiAppApiS2("6364723070897082368");
        String s =
                "{\"apiKey\":\"e7e552ec9d83114ee17b81fa04230200\",\"orderNo\":\"6364744953474437120\",\"timestamp\":1517468501844}";
        System.out.println(sign(s));
    }
}
