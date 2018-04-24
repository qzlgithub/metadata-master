package com.mingdong.backend.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.constant.KafkaTopic;
import com.mingdong.backend.model.Traffic;
import com.mingdong.backend.service.ClientRequestService;
import com.mingdong.backend.util.HttpUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.QueryStatus;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger("ACCESS");
    private static final String IP_URL = "https://int.dpool.sina.com.cn/iplookup/iplookup.php";
    @Resource
    private RedisDao redisDao;
    @Resource
    private ClientRequestService clientRequestService;

    @KafkaListener(topics = KafkaTopic.TRAFFIC)
    public void process(String msg)
    {
        LOGGER.info(">>> {}", msg);
        Traffic traffic = JSON.parseObject(msg, Traffic.class);
        String hourStr = getRequestHourMin(traffic.getTimestamp());
        QueryStatus queryStatus = QueryStatus.getByCode(traffic.getStatus());
        String city = "未知";
        try
        {
            Map<String, Object> body = new HashMap<>();
            body.put("format", "json");
            body.put("ip", traffic.getHost());
            HttpEntity entity = HttpUtils.get(IP_URL, new HashMap<>(), body);
            String resp = EntityUtils.toString(entity, "UTF-8");
            if(!StringUtils.isNullBlank(resp))
            {
                JSONObject jsonObject = JSONObject.parseObject(resp);
                if(!StringUtils.isNullBlank(jsonObject.getString("city")))
                {
                    city = jsonObject.getString("city");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        redisDao.cacheMetadata(traffic.getClientId(), traffic.getCorpName(), traffic.getProductId(),
                traffic.getProductName());
        redisDao.realTimeTraffic(hourStr, traffic.getProductId(), traffic.getClientId(), city);
        redisDao.requestMessage(traffic.getHost(), traffic.getProductName(), traffic.getCorpName(),
                queryStatus.getName());
        if(queryStatus != QueryStatus.SUCCESS)
        {
            clientRequestService.saveErrorRequest(traffic.getTimestamp(), traffic.getClientId(), traffic.getProductId(),
                    queryStatus);
        }
    }

    private String getRequestHourMin(long timestamp)
    {
        Date date = new Date(timestamp - timestamp % 300000);
        return DateUtils.format(date, "HH:mm");
    }
}
