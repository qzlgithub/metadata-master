package com.mingdong.mis.service;

import com.mingdong.mis.constant.APIProduct;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.vo.AbsPayload;

public interface DSDataService
{
    <T extends AbsPayload> void getData(APIProduct product, Long accountId, Long clientId, Long userId, String ip,
            T req, MDResp res);
}
