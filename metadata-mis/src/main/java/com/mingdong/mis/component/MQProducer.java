package com.mingdong.mis.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.backend.constant.KafkaTopic;
import com.mingdong.backend.model.Traffic;
import com.mingdong.mis.model.vo.AbsPayload;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MQProducer
{
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void userRequest(String requestNo, Long clientId, String corpName, Long productId, String productName,
            String ip, AbsPayload payload, long timestamp)
    {
        Traffic traffic = new Traffic(requestNo, clientId, corpName, productId, productName, timestamp, ip);
        traffic.setHashCode(JSON.toJSONString(payload).hashCode());
        kafkaTemplate.send(KafkaTopic.TRAFFIC, JSON.toJSONString(traffic));
    }
}
