package com.mingdong.mis.controller;

import com.mingdong.core.annotation.AuthRequired;
import com.mingdong.mis.constant.APIProduct;
import com.mingdong.mis.constant.MetadataResult;
import com.mingdong.mis.model.MetadataRes;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.AbsRequest;
import com.mingdong.mis.model.vo.AccessVO;
import com.mingdong.mis.model.vo.BlacklistVO;
import com.mingdong.mis.model.vo.MultipleAppVO;
import com.mingdong.mis.service.DSDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "ds-data")
public class DSDataController
{
    @Resource
    private DSDataService dsDataService;

    @AuthRequired
    @PostMapping(value = "blacklist", headers = {"accept-version=1.0"})
    public MetadataRes callBlacklistService(@RequestBody AccessVO<BlacklistVO> accessVO)
    {
        return getData(APIProduct.DS_DATA_BLACKLIST, accessVO);
    }

    @AuthRequired
    @PostMapping(value = "multi-app", headers = {"accept-version=1.0"})
    public MetadataRes callMultipleAppService(@RequestBody AccessVO<MultipleAppVO> accessVO)
    {
        return getData(APIProduct.DS_DATA_MULTI_APP, accessVO);
    }

    private <T extends AbsRequest> MetadataRes getData(APIProduct product, AccessVO<T> accessVO)
    {
        MetadataRes res = RequestThread.getResult();
        // 数据验签
        if(!accessVO.checkSign(RequestThread.getAppSecret()))
        {
            res.setResult(MetadataResult.RC_9);
            return res;
        }
        dsDataService.getData(product, RequestThread.getAccountId(), RequestThread.getClientId(),
                RequestThread.getUserId(), RequestThread.getIp(), accessVO.getPayload(), res);
        return res;
    }
}
