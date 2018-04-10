package com.test.mis;

import com.alibaba.fastjson.JSON;
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
import java.util.SortedMap;

public class APITest
{
    private static final String URL_CDK = "http://47.100.162.249:9900/credit/overdue";
    private static final String PHONE_FILE_PATH = "e://phone.txt";
    private static final String SECRET_KEY = "95016dbd7c834fa39ae81aae30622269";
    private static final String NAME = "测试";
    private static final String ID_NO = "330123199001011122";
    private static final String TEMPLATE =
            "{\"timestamp\":%d,\"sign\":\"%s\",\"payload\":{\"phone\":\"%s\",\"name\":\"%s\",\"idNo\":\"%s\"}}";

    public static void main(String[] args) throws IOException, MetadataCoreException, MetadataHttpException
    {
        List<String> phoneList = readPhoneNumber();
        System.out.println("total: " + phoneList.size());
        long ts;
        int pass = 0;
        for(String phone : phoneList)
        {
            pass++;
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
            headers.put("Access-Token", "a2sb1f64rc87mc5ava7b61xbp4n8ka0ft9y7tdg33606qdc65244qbcabd8a74qa");
            String content = String.format(TEMPLATE, ts, sign, phone, NAME, ID_NO);
            HttpEntity entity = HttpUtils.postData(URL_CDK, headers, content);
            String s = EntityUtils.toString(entity);

            System.out.println(pass + " - " + phone + " : " + s);
        }
    }

    private static List<String> readPhoneNumber() throws IOException
    {
        List<String> phoneList = new ArrayList<>(9999);
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
