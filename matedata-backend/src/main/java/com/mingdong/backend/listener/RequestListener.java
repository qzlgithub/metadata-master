package com.mingdong.backend.listener;

import com.alibaba.fastjson.JSON;
import com.mingdong.backend.component.RedisDao;
import com.mingdong.backend.constant.KafkaTopic;
import com.mingdong.backend.model.Traffic;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.QueryStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class RequestListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger("ACCESS");
    @Resource
    private RedisDao redisDao;

    @KafkaListener(topics = KafkaTopic.TRAFFIC)
    public void process(String msg)
    {
        LOGGER.info(">>> {}", msg);
        Traffic traffic = JSON.parseObject(msg, Traffic.class);
        String hourStr = getRequestHourMin(traffic.getTimestamp());

        redisDao.cacheMetadata(traffic.getClientId(), traffic.getCorpName(), traffic.getProductId(),
                traffic.getProductName());
        redisDao.realTimeTraffic(hourStr, traffic.getProductId(), traffic.getClientId());
        redisDao.requestMessage(traffic.getHost(), traffic.getProductName(), traffic.getCorpName(),
                QueryStatus.getNameByCode(traffic.getStatus()));
    }

    private String getRequestHourMin(long timestamp)
    {
        Date date = new Date(timestamp - timestamp % 300000);
        return DateUtils.format(date, "HH:mm");
    }
}
