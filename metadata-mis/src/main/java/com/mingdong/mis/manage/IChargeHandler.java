package com.mingdong.mis.manage;

import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.vo.AbsPayload;

public interface IChargeHandler
{
    void work(AbsPayload payload, MDResp resp);
}
