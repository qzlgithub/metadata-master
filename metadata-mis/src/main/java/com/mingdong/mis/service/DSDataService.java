package com.mingdong.mis.service;

import com.mingdong.mis.constant.APIProduct;
import com.mingdong.mis.model.MetadataRes;
import com.mingdong.mis.model.vo.AbsRequest;

public interface DSDataService
{
    <T extends AbsRequest> void getData(APIProduct product, Long accountId, Long clientId, Long userId, String ip,
            T req, MetadataRes res);
}
