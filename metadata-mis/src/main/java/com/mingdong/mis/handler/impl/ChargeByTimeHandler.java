package com.mingdong.mis.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.constant.TrueOrFalse;
import com.mingdong.mis.component.RedisDao;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.handler.IChargeHandler;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.AbsPayload;
import com.mingdong.mis.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 按时间计费
 */
@Component
public class ChargeByTimeHandler implements IChargeHandler
{
    private static Logger logger = LoggerFactory.getLogger(ChargeByTimeHandler.class);
    @Resource
    private RedisDao redisDao;
    @Resource
    private DataService dataService;
    @Resource
    private RouteHandler routeHandler;

    @Override
    public void work(AbsPayload payload, MDResp resp)
    {
        String uuid = StringUtils.getUuid();
        long t = System.currentTimeMillis();
        logger.info(uuid + " [1]: " + 0);
        redisDao.incProductTraffic(resp.getTimestamp(), RequestThread.getProductId());
        logger.info(uuid + " [2]: " + (System.currentTimeMillis() - t));
        if(!checkTimeValid(resp.getTimestamp()))
        {
            resp.setResult(MDResult.PRODUCT_EXPIRED);
            return;
        }
        Metadata metadata = routeHandler.routeProcessor(payload);
        logger.info(uuid + " [3]: " + (System.currentTimeMillis() - t));
        // 保存请求记录，并返回请求编号
        String requestNo = dataService.saveRequestLog(payload, metadata.isHit(), resp.requestAt());
        logger.info(uuid + " [4]: " + (System.currentTimeMillis() - t));
        resp.setStatus(metadata.isHit() ? TrueOrFalse.FALSE : TrueOrFalse.TRUE);
        resp.setRequestNo(requestNo);
        resp.setData((JSONObject) JSON.toJSON(metadata.getData()));
        logger.info(uuid + " [5]: " + (System.currentTimeMillis() - t));
    }

    /**
     * 检查请求时间是否有效
     *
     * @param timestamp 请求时间的Unix时间戳
     */
    private boolean checkTimeValid(long timestamp)
    {
        return timestamp * 1000 >= RequestThread.getStart() && timestamp * 1000 < RequestThread.getEnd();
    }
}
