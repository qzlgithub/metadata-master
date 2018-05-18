package com.mingdong.mis.controller;

import com.mingdong.common.util.CollectionUtils;
import com.mingdong.core.annotation.AuthRequired;
import com.mingdong.core.constant.BillPlan;
import com.mingdong.mis.constant.MDResult;
import com.mingdong.mis.handler.impl.ChargeByTimeHandler;
import com.mingdong.mis.handler.impl.ChargeByUseHandler;
import com.mingdong.mis.model.MDResp;
import com.mingdong.mis.model.RequestThread;
import com.mingdong.mis.model.vo.PersonVO;
import com.mingdong.mis.model.vo.PhoneCollVO;
import com.mingdong.mis.model.vo.RequestVO;
import com.mingdong.mis.service.DetectService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private DetectService detectService;

    @PostMapping(value = "/detection/{product}/{token}")
    public MDResp detect(@PathVariable("product") String product, @PathVariable("token") String token,
            @RequestBody PhoneCollVO phoneCollVO) throws Exception
    {
        MDResp resp = MDResp.create();
        if(phoneCollVO == null || CollectionUtils.isEmpty(phoneCollVO.getPhones()) ||
                phoneCollVO.getPhones().size() > 100)
        {
            return resp;
        }
        if(!"blacklist".equals(product) && !"overdue".equals(product) && !"multi-register".equals(product) &&
                !"creditable".equals(product) && !"favourable".equals(product) && !"rejectee".equals(product) &&
                !"hologram".equals(product))
        {
            return resp;
        }
        if(!detectService.isDetectionTokenValid(token))
        {
            resp.response(MDResult.INVALID_ACCESS_TOKEN);
            return resp;
        }
        if("blacklist".equals(product))
        {
            detectService.getBlacklistInfo(phoneCollVO.getPhones(), resp);
        }
        else if("overdue".equals(product))
        {
            detectService.getOverdueInfo(phoneCollVO.getPhones(), resp);
        }
        else if("multi-register".equals(product))
        {
            detectService.getMultiRegisterInfo(phoneCollVO.getPhones(), resp);
        }
        else if("creditable".equals(product))
        {
            detectService.getCreditableInfo(phoneCollVO.getPhones(), resp);
        }
        else if("favourable".equals(product))
        {
            detectService.getFavourableInfo(phoneCollVO.getPhones(), resp);
        }
        else if("rejectee".equals(product))
        {
            detectService.getRejecteeInfo(phoneCollVO.getPhones(), resp);
        }
        else if("hologram".equals(product))
        {
            detectService.getHologramInfo(phoneCollVO.getPhones(), resp);
        }
        return resp;
    }

    /**
     * 1. 常欠客
     */
    @AuthRequired
    @PostMapping(value = "/credit/overdue")
    public MDResp getTargetOverdueInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 2. 黑名单
     */
    @AuthRequired
    @PostMapping(value = "/credit/blacklist")
    public MDResp checkPersonInBlacklist(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 3. 多头客
     */
    @AuthRequired
    @PostMapping(value = "/credit/multi-register")
    public MDResp getTargetRegisterInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 4. 通过客
     */
    @AuthRequired
    @PostMapping(value = "/credit/creditable")
    public MDResp getTargetPassInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 5. 优良客
     */
    @AuthRequired
    @PostMapping(value = "/credit/favourable")
    public MDResp getTargetExcellentInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 6. 拒贷客
     */
    @AuthRequired
    @PostMapping(value = "/credit/rejectee")
    public MDResp getTargetRefuseInfo(@RequestBody RequestVO<PersonVO> requestVO)
    {
        return revokeAPI(requestVO);
    }

    /**
     * 7. 全息报告
     */
    @AuthRequired
    @PostMapping(value = "/credit/hologram")
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
