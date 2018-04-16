package com.mingdong.backend.listener;

import com.alibaba.fastjson.JSON;
import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.constant.KafkaTopic;
import com.mingdong.backend.model.Traffic;
import com.mingdong.core.constant.QueryStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RequestListener
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
        redisDao.requestMessage(traffic.getHost(), traffic.getProductName(), traffic.getCorpName(),
                QueryStatus.getNameByCode(traffic.getStatus()));
    }
}
