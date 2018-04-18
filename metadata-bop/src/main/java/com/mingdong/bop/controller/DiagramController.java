package com.mingdong.bop.controller;

import com.mingdong.bop.constant.TimeScope;
import com.mingdong.bop.model.ChartVO;
import com.mingdong.bop.service.ClientService;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.RestResp;
import com.mingdong.bop.util.DateRangeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class DiagramController
{
    @Resource
    private ClientService clientService;

    @PostMapping(value = "/diagram/client/increment")
    public RestResp getClientIncrementDiagramData(@RequestBody ChartVO chartVO)
    {
        RestResp resp = new RestResp();
        Integer days = chartVO.getScope();
        RangeUnit rangeUnit = RangeUnit.getById(chartVO.getUnit());
        if(days == null || rangeUnit == null)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(days == 0 && (chartVO.getStartDate() == null || chartVO.getEndDate() == null))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        DateRange range;
        Date compareFrom = null;
        if(days != 0)
        {
            range = DateRangeUtils.getTimeRangeByScope(TimeScope.getByCode(days));
        }
        else
        {
            range = new DateRange(DateUtils.clearTime(chartVO.getStartDate()),
                    DateUtils.clearTime(chartVO.getEndDate()));
            if(chartVO.getCompareFrom() != null)
            {
                compareFrom = DateUtils.clearTime(chartVO.getCompareFrom());
            }
        }
        clientService.getClientIncreaseTrend(range, compareFrom, rangeUnit, resp);
        return resp;
    }

    @PostMapping(value = "/diagram/recharge/bar")
    public RestResp getRechargeChartData(@RequestBody ChartVO chartVO)
    {
        RestResp resp = new RestResp();
        Integer scope = chartVO.getScope();
        if(scope == null)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(scope == 0 && (chartVO.getStartDate() == null || chartVO.getEndDate() == null))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        DateRange range;
        Date compareFrom = null;
        if(scope != 0)
        {
            range = DateRangeUtils.getTimeRangeByScope(TimeScope.getByCode(scope));
        }
        else
        {
            range = new DateRange(DateUtils.clearTime(chartVO.getStartDate()),
                    DateUtils.clearTime(chartVO.getEndDate()));
            if(chartVO.getCompareFrom() != null)
            {
                compareFrom = DateUtils.clearTime(chartVO.getCompareFrom());
            }
        }
        clientService.getClientRechargeTrend(range, compareFrom, null, resp);
        return resp;
    }
}
