package com.mingdong.mis.controller;

import com.mingdong.core.annotation.AuthRequired;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.PhoneVO;
import com.mingdong.mis.model.vo.RequestVO;
import com.mingdong.mis.manage.ChargeByHitHandler;
import com.mingdong.mis.manage.ChargeByTimeHandler;
import com.mingdong.mis.manage.ChargeByUseHandler;
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
    @Resource
    private ChargeByHitHandler chargeByHitService;

    @AuthRequired
    @RequestMapping(value = "/credit/overdue")
    public MDResp getTargetOverdueInfo(@RequestBody RequestVO<PhoneVO> requestVO)
    {
        MDResp resp = RequestThread.getResp();
        MDResult result = requestVO.checkParamAndSign(RequestThread.getAppSecret());
        if(result != MDResult.OK)
        {
            resp.setResult(result);
            return resp;
        }
        BillPlan billPlan = BillPlan.getById(RequestThread.getBillPlan());
        switch(billPlan)
        {
            case PER_USE:
                chargeByUseService.work(requestVO.getPayload(), resp);
                break;
            case PER_HIT:
                chargeByHitService.work(requestVO.getPayload(), resp);
                break;
            default:
                chargeByTimeService.work(requestVO.getPayload(), resp);
        }
        return resp;
    }
}
