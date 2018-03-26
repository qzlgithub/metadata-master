package com.mingdong.mis.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SMSUtils
{
    private static final String SMS_URL = "http://client.movek.net:8888/sms.aspx";
    private static final String CONTENT = "%s【元数据】";

    private SMSUtils()
    {
    }

    public static void sendSMS(String content, String phone) throws IOException
    {
        Map<String, Object> param = new HashMap<>();
        param.put("action", "send");
        param.put("userid", "2407");
        param.put("account", "sdk-a2407-2407");
        param.put("password", "240707");
        param.put("mobile", phone);
        param.put("content", String.format(CONTENT, content));
        HttpUtils.get(SMS_URL, null, param);
    }
}
