package com.mingdong.mis.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.constant.ResCode;
import com.mingdong.mis.handler.IChargeHandler;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.AbsPayload;
import com.mingdong.mis.service.DataService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 按时间计费
 */
@Component
public class ChargeByTimeHandler implements IChargeHandler
{
    @Resource
    private DataService dataService;
    @Resource
    private RouteHandler routeHandler;

    @Override
    public void work(AbsPayload payload, MDResp resp)
    {
        if(!checkTimeValid(resp.getTimestamp()))
        {
            resp.response(MDResult.PRODUCT_EXPIRED);
            return;
        }
        Metadata metadata = routeHandler.routeProcessor(payload);
        // 保存请求记录，并返回请求编号
        String requestNo = dataService.saveRequestLog(payload, metadata.isHit(), resp.requestAt());
        resp.setRequestNo(requestNo);
        if(metadata.isHit())
        {
            resp.setResCode(ResCode.NORMAL);
            resp.setResData((JSONObject) JSON.toJSON(metadata.getData()));
        }
        else
        {
            resp.setResCode(ResCode.NOT_HIT);
        }
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
