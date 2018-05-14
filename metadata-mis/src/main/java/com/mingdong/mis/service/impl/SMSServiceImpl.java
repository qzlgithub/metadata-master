package com.mingdong.mis.service.impl;

import com.mingdong.mis.domain.entity.SMSLog;
import com.mingdong.mis.domain.mapper.SMSLogMapper;
import com.mingdong.mis.service.SMSService;
import com.mingdong.mis.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SMSServiceImpl implements SMSService
{
    private static final String SMS_URL = "http://client.movek.net:8888/sms.aspx";
    private static final String CONTENT = "%s【元数据】";

    @Resource
    private SMSLogMapper smsLogMapper;

    @Override
    public void sendSMS(Integer type, String content, String phone) throws IOException
    {
        Map<String, Object> param = new HashMap<>();
        param.put("action", "send");
        param.put("userid", "2407");
        param.put("account", "sdk-a2407-2407");
        param.put("password", "240707");
        param.put("mobile", phone);
        param.put("content", String.format(CONTENT, content));
        HttpEntity entity = HttpUtils.get(SMS_URL, null, param);
        String s = EntityUtils.toString(entity);
        Document doc = null;
        try
        {
            doc = DocumentHelper.parseText(s);
            Element rootElt = doc.getRootElement();
            Element returnstatus = rootElt.element("returnstatus");
            Element taskID = rootElt.element("taskID");
            Date date = new Date();
            SMSLog smsLog = new SMSLog();
            smsLog.setCreateTime(date);
            smsLog.setUpdateTime(date);
            smsLog.setPhone(phone);
            smsLog.setContent(content);
            smsLog.setSuccess("Success".equals(returnstatus.getStringValue()) ? 1 : 0);
            smsLog.setTaskId(taskID.getStringValue());
            smsLog.setType(type);
            smsLogMapper.add(smsLog);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
