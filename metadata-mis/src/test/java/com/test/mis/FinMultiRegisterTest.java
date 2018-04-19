package com.test.mis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.util.MapUtils;
import com.mingdong.core.exception.MetadataCoreException;
import com.mingdong.core.exception.MetadataHttpException;
import com.mingdong.mis.util.HttpUtils;
import com.mingdong.mis.util.SignUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;

public class FinMultiRegisterTest
{
    private static final String URL_DTK = "http://47.100.162.249:9900/credit/multi-register";
    private static final String PHONE_FILE_PATH = "e://phone_mr.txt";
    private static final String SECRET_KEY = "0d593dc3cacb49b1a090d0b14b1e2c6a";
    private static final String NAME = "测试";
    private static final String ID_NO = "330123199001011122";
    private static final String TEMPLATE =
            "{\"timestamp\":%d,\"sign\":\"%s\",\"payload\":{\"phone\":\"%s\",\"name\":\"%s\",\"idNo\":\"%s\"}}";

    public static void main(String[] args) throws IOException, MetadataCoreException, MetadataHttpException
    {
        List<String> phoneList = readPhoneNumber();
        long ts;
        String phone;
        while(true)
        {
            int i = new Random().nextInt(phoneList.size() + 1);
            phone = phoneList.get(i);
            ts = System.currentTimeMillis() / 1000;
            Map<String, Object> m = new HashMap<>();
            m.put("phone", phone);
            m.put("name", NAME);
            m.put("idNo", ID_NO);
            m.put("timestamp", ts);
            SortedMap sm = MapUtils.sortKey(m);
            String sign = SignUtils.sign(JSON.toJSONString(sm), SECRET_KEY);
            Map<String, String> headers = new HashMap<>();
            headers.put("accept-version", "1.0");
            headers.put("Content-Type", "application/json");
            headers.put("Access-Token", "f7q1i411r2oaa51cg8v2b2x264vbofw1rbzak10az8jfyfnbj2ceoeo4i1p2958f");
            String content = String.format(TEMPLATE, ts, sign, phone, NAME, ID_NO);
            HttpEntity entity = HttpUtils.postData(URL_DTK, headers, content);
            String s = EntityUtils.toString(entity);
            JSONObject json = JSON.parseObject(s);
            boolean hit = json.getInteger("code") == 0 && json.getInteger("resCode") == 0;
            System.out.println("phone : " + hit);
        }
    }

    private static List<String> readPhoneNumber() throws IOException
    {
        List<String> phoneList = new ArrayList<>();
        FileReader reader = new FileReader(PHONE_FILE_PATH);
        BufferedReader br = new BufferedReader(reader);
        String str;
        while((str = br.readLine()) != null)
        {
            phoneList.add(str);
        }
        br.close();
        reader.close();
        return phoneList;
    }
}
