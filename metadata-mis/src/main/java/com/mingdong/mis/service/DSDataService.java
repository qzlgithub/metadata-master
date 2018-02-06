package com.mingdong.mis.service;

import com.mingdong.mis.model.MetadataRes;
import com.mingdong.mis.model.vo.BlacklistVO;

public interface DSDataService
{
    void getBlacklistData(Long accountId, Long clientId, Long userId, String ip, BlacklistVO req, MetadataRes res);
}
