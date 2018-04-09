package com.mingdong.mq.listener;

import com.mingdong.mq.component.RedisDao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TrafficHandler
{
    @Resource
    private RedisDao redisDao;

    @KafkaListener(topics = "traffic")
    public void process(ConsumerRecord<?, ?> cr)
    {
        System.out.println(cr.topic() + " - " + cr.key() + ": " + cr.value());

    }
}
