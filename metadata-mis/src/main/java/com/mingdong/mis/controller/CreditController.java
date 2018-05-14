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

    /**
     * 1. 常欠客
     */
    @AuthRequired
    @RequestMapping(value = "/credit/overdue")
    public MDResp getTargetOverdueInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 2. 黑名单
     */
    @AuthRequired
    @RequestMapping(value = "/credit/blacklist")
    public MDResp checkPersonInBlacklist(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 3. 多头客
     */
    @AuthRequired
    @RequestMapping(value = "/credit/multi-register")
    public MDResp getTargetRegisterInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 4. 通过客
     */
    @AuthRequired
    @RequestMapping(value = "/credit/creditable")
    public MDResp getTargetPassInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 5. 优良客
     */
    @AuthRequired
    @RequestMapping(value = "/credit/favourable")
    public MDResp getTargetExcellentInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 6. 拒贷客
     */
    @AuthRequired
    @RequestMapping(value = "/credit/rejectee")
    public MDResp getTargetRefuseInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 7. 全息报告
     */
    @AuthRequired
    @RequestMapping(value = "/credit/hologram")
    public MDResp getTargetReportInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    private MDResp revokeAPI(RequestVO requestVO)
    {
        RequestThread.setPayloadId(requestVO.getPayloadHashCode());
        MDResp resp = RequestThread.getResp();
        MDResult result = requestVO.checkParamAndSign(RequestThread.getSecretKey());
        if(result != MDResult.OK)
        {
            resp.response(result);
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
