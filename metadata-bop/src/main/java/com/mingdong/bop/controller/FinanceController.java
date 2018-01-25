package com.mingdong.bop.controller;

import com.mingdong.bop.service.TradeService;
import com.mingdong.core.model.BLResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "finance")
public class FinanceController
{
    @Resource
    private TradeService tradeService;

    @GetMapping(value = "/rechargeList")
    public BLResp getProductRecharge(){
        BLResp resp = BLResp.build();
        tradeService.getProductRechargeList(null,null,null,null,null,resp);
        return resp;
    }
}
