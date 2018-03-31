package com.mingdong.mis.handler.impl;

import com.mingdong.mis.model.Metadata;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.AbsPayload;
import com.mingdong.mis.model.vo.PhoneVO;
import com.mingdong.mis.processor.impl.OverdueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RouteHandler
{
    private static Logger logger = LoggerFactory.getLogger(RouteHandler.class);
    @Resource
    private OverdueProcessor overdueProcessor;

    public Metadata routeProcessor(AbsPayload payload)
    {
        switch(RequestThread.getProduct())
        {
            case FIN_CDK:
                return overdueProcessor.process((PhoneVO) payload);
            default:
                logger.warn("Invalid product: " + RequestThread.getProduct());
                return null;
        }
    }
}
