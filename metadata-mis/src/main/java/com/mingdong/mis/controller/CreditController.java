package com.mingdong.mis.controller;

import com.mingdong.core.annotation.AuthRequired;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.handler.impl.ChargeByTimeHandler;
import com.mingdong.mis.handler.impl.ChargeByUseHandler;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.PersonVO;
import com.mingdong.mis.model.vo.RequestVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class CreditController
{
    @Resource
    private ChargeByTimeHandler chargeByTimeService;
    @Resource
    private ChargeByUseHandler chargeByUseService;

    @AuthRequired
    @RequestMapping(value = "/credit/overdue")
    public MDResp getTargetOverdueInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        MDResp resp = RequestThread.getResp();
        MDResult result = requestVO.checkParamAndSign(RequestThread.getSecretKey());
        if(result != MDResult.OK)
        {
            resp.setResult(result);
            return resp;
        }
        if(BillPlan.BY_TIME.equals(RequestThread.getBillPlan()))
        {
            chargeByTimeService.work(requestVO.getPayload(), resp);
        }
        else
        {
            chargeByUseService.work(requestVO.getPayload(), resp);
        }
        return resp;
    }

    @AuthRequired
    @RequestMapping(value = "/credit/blacklist")
    public MDResp checkPersonInBlacklist(@RequestBody RequestVO<PersonVO> requestVO)
    {
        MDResp resp = RequestThread.getResp();
        MDResult result = requestVO.checkParamAndSign(RequestThread.getSecretKey());
        if(result != MDResult.OK)
        {
            resp.setResult(result);
            return resp;
        }
        if(BillPlan.BY_TIME.equals(RequestThread.getBillPlan()))
        {
            chargeByTimeService.work(requestVO.getPayload(), resp);
        }
        else
        {
            chargeByUseService.work(requestVO.getPayload(), resp);
        }
        return resp;
    }
}
