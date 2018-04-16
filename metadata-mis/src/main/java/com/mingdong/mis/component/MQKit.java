package com.mingdong.mis.component;

import com.alibaba.fastjson.JSON;
import com.mingdong.backend.constant.KafkaTopic;
import com.mingdong.backend.model.Traffic;
import com.mingdong.core.constant.QueryStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MQKit
{
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void userRequest(String ip, long timestamp, Long clientId, String corpName, Long productId,
            String productName, int payloadId, QueryStatus status)
    {
        Traffic traffic = new Traffic();
        traffic.setHost(ip);
        traffic.setTimestamp(timestamp);
        traffic.setClientId(clientId);
        traffic.setCorpName(corpName);
        traffic.setProductId(productId);
        traffic.setProductName(productName);
        traffic.setPayloadId(payloadId);
        traffic.setStatus(status.getCode());
        kafkaTemplate.send(KafkaTopic.TRAFFIC, JSON.toJSONString(traffic));
    }
}
