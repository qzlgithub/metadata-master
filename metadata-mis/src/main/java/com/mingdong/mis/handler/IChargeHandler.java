package com.mingdong.mis.handler;

import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.vo.AbsPayload;

public interface IChargeHandler
{
    void work(AbsPayload payload, MDResp resp) throws Exception;
}
