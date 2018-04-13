package com.mingdong.backend.listener;

import com.alibaba.fastjson.JSON;
import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.constant.KafkaTopic;
import com.mingdong.backend.model.Traffic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TrafficListener
{
    @Resource
    private RedisDao redisDao;

    @KafkaListener(topics = KafkaTopic.TRAFFIC)
    public void process(String msg)
    {
        Traffic traffic = JSON.parseObject(msg, Traffic.class);
        redisDao.cacheMetadata(traffic.getClientId(), traffic.getCorpName(), traffic.getProductId(),
                traffic.getProductName());
        redisDao.realTimeTraffic(traffic.getTimestamp(), traffic.getProductId(), traffic.getClientId());
    }
}
