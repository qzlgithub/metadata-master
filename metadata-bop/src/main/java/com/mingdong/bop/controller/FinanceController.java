package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.RequestThread;
import com.mingdong.bop.service.TradeService;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.StringUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.model.RestListResp;
import com.mingdong.core.util.BusinessUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class FinanceController
{
    @Resource
    private TradeService tradeService;

    @LoginRequired
    @GetMapping(value = "/finance/recharge")
    public RestListResp getRechargeList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.RECHARGE_TYPE, required = false) Long rechargeType,
            @RequestParam(value = Field.PRODUCT, required = false) Long product,
            @RequestParam(value = Field.MANAGER, required = false) Long manager,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        keyword = StringUtils.isNullBlank(keyword) ? null : keyword.trim();
        Date from = fromDate, to = toDate;
        if(fromDate != null && toDate != null)
        {
            if(fromDate.after(toDate))
            {
                from = toDate;
                to = from;
            }
        }
        to = BusinessUtils.getLastDayStartTime(to);
        tradeService.getProductRechargeInfoList(keyword, product, RequestThread.isManager()?manager:RequestThread.getOperatorId(), rechargeType, from, to,
                new Page(pageNum, pageSize), res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/finance/bill")
    public RestListResp getBillList(@RequestParam(value = Field.KEYWORD, required = false) String keyword,
            @RequestParam(value = Field.PRODUCT, required = false) Long productId,
            @RequestParam(value = Field.BILL_PLAN, required = false) Integer billPlan,
            @RequestParam(value = Field.FROM_DATE, required = false) Date fromDate,
            @RequestParam(value = Field.TO_DATE, required = false) Date toDate,
            @RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestListResp res = new RestListResp();
        keyword = StringUtils.isNullBlank(keyword) ? null : keyword.trim();
        Date from = fromDate, to = toDate;
        if(fromDate != null && toDate != null)
        {
            if(fromDate.after(toDate))
            {
                from = toDate;
                to = from;
            }
        }
        to = BusinessUtils.getLastDayStartTime(to);
        tradeService.getClientBillList(keyword, productId, billPlan, from, to, RequestThread.isManager()?null:RequestThread.getOperatorId(), new Page(pageNum, pageSize), res);
        return res;
    }
}
