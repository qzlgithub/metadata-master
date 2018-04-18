package com.mingdong.bop.controller;

import com.mingdong.bop.constant.TimeScope;
import com.mingdong.bop.model.ChartVO;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.util.DateRangeUtils;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.RestResp;
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
        TimeScope scope = TimeScope.getByCode(chartVO.getScope());
        if(scope != TimeScope.CUSTOM && scope != TimeScope.LATEST_30_DAYS && scope != TimeScope.LATEST_90_DAYS &&
                scope != TimeScope.LATEST_1_YEAR)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        RangeUnit unit = RangeUnit.getById(chartVO.getUnit());
        if(unit == null)
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        if(scope == TimeScope.CUSTOM && (chartVO.getStartDate() == null || chartVO.getEndDate() == null))
        {
            resp.setError(RestResult.KEY_FIELD_MISSING);
            return resp;
        }
        DateRange range;
        Date compareFrom = null;
        if(scope != TimeScope.CUSTOM)
        {
            range = DateRangeUtils.getTimeRangeByScope(scope);
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
        clientService.getClientIncrementDiagramData(range, unit, compareFrom, resp);
        return resp;
    }
}
