package com.mingdong.mis.controller;

import com.mingdong.mis.constant.APIProduct;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.AbsPayload;
import com.mingdong.mis.model.vo.RequestVO;
import com.mingdong.mis.service.DSDataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "ds-data")
public class DSDataController
{
    @Resource
    private DSDataService dsDataService;

    /*@AuthRequired
    @PostMapping(value = "blacklist", headers = {"accept-version=1.0"})
    public MDResp callBlacklistService(@RequestBody RequestVO<BlacklistVO> requestVO)
    {
        return getData(APIProduct.DS_DATA_BLACKLIST, requestVO);
    }

    @AuthRequired
    @PostMapping(value = "multi-app", headers = {"accept-version=1.0"})
    public MDResp callMultipleAppService(@RequestBody RequestVO<MultipleAppVO> requestVO)
    {
        return getData(APIProduct.DS_DATA_MULTI_APP, requestVO);
    }*/

    private <T extends AbsPayload> MDResp getData(APIProduct product, RequestVO<T> requestVO)
    {
        MDResp res = RequestThread.getResp();
        MDResult result = requestVO.checkParamAndSign(RequestThread.getSecretKey());
        if(result != MDResult.OK)
        {
            res.setResult(result);
            return res;
        }
        dsDataService.getData(product, RequestThread.getClientProductId(), RequestThread.getClientId(),
                RequestThread.getUserId(), RequestThread.getHost(), requestVO.getPayload(), res);
        return res;
    }
}
