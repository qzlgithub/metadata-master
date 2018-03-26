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
    private static final Logger logger = LoggerFactory.getLogger(SignUtils.class);

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
        long ts = System.currentTimeMillis();
        System.out.println("timestamp: " + ts);
        Map<String, Object> m = new HashMap<>();
        m.put("phone", "15881821749");
        m.put("timestamp", ts);
        SortedMap sm = MapUtils.sortKey(m);
        String str = JSON.toJSONString(sm);
        System.out.println("str: " + str);
        String[] keys = {"c8a3b60a412c44fc96daf10c56c72f93"};
        for(String key : keys)
        {
            String sign = sign(str, key);
            System.out.println("sign: " + sign);
        }
    }
}
