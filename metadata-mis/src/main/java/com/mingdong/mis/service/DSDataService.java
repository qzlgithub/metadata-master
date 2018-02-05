package com.mingdong.mis.service;

import com.mingdong.mis.model.MetadataRes;
import com.mingdong.mis.model.vo.BlacklistVO;

public interface DSDataService
{
    void getBlacklistData(Long productId, Long clientId, Long userId, String ip, BlacklistVO request, MetadataRes res);
}
