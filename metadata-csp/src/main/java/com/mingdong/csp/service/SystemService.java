package com.mingdong.csp.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.model.RestResp;

public interface SystemService
{
    void getNewsList(Integer type, Page page, RestListResp resp);

    void getNewsDetail(Long id, RestResp resp);

}
