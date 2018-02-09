package com.mingdong.mis.util;

import com.alibaba.fastjson.JSON;
import com.mingdong.common.constant.Charset;
import com.mingdong.common.util.MapUtils;
import com.mingdong.core.exception.MetadataCoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class SignUtils
{
    private static final String HMAC_SHA_256 = "HmacSHA256";
    private static Logger logger = LoggerFactory.getLogger(SignUtils.class);

    private SignUtils()
    {
    }

    public static String sign(String content, String key) throws MetadataCoreException
    {
        try
        {
            Mac hmacSHA256 = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), HMAC_SHA_256);
            hmacSHA256.init(spec);
            byte[] encData = hmacSHA256.doFinal(content.getBytes(Charset.UTF_8));
            return byteArrayToHexString(encData);
        }
        catch(NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e)
        {
            logger.error("Failed to get signature of data[{}] with key[{}]: {}", content, key, e.getMessage());
            throw new MetadataCoreException("error to obtain digital signature");
        }
    }

    private static String byteArrayToHexString(byte[] data)
    {
        StringBuilder sb = new StringBuilder();
        String s;
        for(int n = 0; data != null && n < data.length; n++)
        {
            s = Integer.toHexString(data[n] & 0XFF);
            if(s.length() == 1)
            {
                sb.append('0');
            }
            sb.append(s);
        }
        return sb.toString().toLowerCase();
    }

    public static void main(String[] args) throws MetadataCoreException
    {
        Map<String, Object> m = new HashMap<>();
        m.put("idNo", "413001197306040513");
        m.put("name", "詹海欧");
        m.put("phone", "13911059220");
        m.put("timestamp", 1234567890);
        //        m.put("orderNo", "6367286835493068800");
        SortedMap sm = MapUtils.sortKey(m);
        String str = JSON.toJSONString(sm);
        System.out.println("str: " + str);
        String sign = sign(str, "8a4b2c4dbaa8441da4bcf960a9fc6bb6");
        System.out.println("sign: " + sign);
    }
}
