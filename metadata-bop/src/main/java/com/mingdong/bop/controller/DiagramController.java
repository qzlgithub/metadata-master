package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.constant.TimeScope;
import com.mingdong.bop.model.ChartVO;
import com.mingdong.bop.service.ClientService;
import com.mingdong.bop.service.ProductService;
import com.mingdong.bop.service.SystemService;
import com.mingdong.bop.util.DateRangeUtils;
import com.mingdong.common.model.Page;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.annotation.LoginRequired;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.RestResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class DiagramController
{
    @Resource
    private ClientService clientService;
    @Resource
    private ProductService productService;
    @Resource
    private SystemService systemService;

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

    @PostMapping(value = "/diagram/recharge/bar")
    public RestResp getRechargeStatsBar(@RequestBody ChartVO chartVO)
    {
        RestResp resp = new RestResp();
        TimeScope scope = TimeScope.getByCode(chartVO.getScope());
        if(scope != TimeScope.CUSTOM && scope != TimeScope.LATEST_7_DAYS && scope != TimeScope.LATEST_30_DAYS &&
                scope != TimeScope.LATEST_1_YEAR)
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
        clientService.getClientRechargeBar(range, compareFrom, null, resp);
        return resp;
    }

    @PostMapping(value = "/diagram/recharge/pie")
    public RestResp getRechargeStatsPie(@RequestBody ChartVO chartVO)
    {
        RestResp resp = new RestResp();
        TimeScope scope = TimeScope.getByCode(chartVO.getScope());
        if(scope != TimeScope.CUSTOM && scope != TimeScope.LATEST_7_DAYS && scope != TimeScope.LATEST_30_DAYS &&
                scope != TimeScope.LATEST_1_YEAR)
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
        clientService.getClientRechargePie(range, compareFrom, resp);
        return resp;
    }

    @PostMapping(value = "/diagram/request/line")
    public RestResp getRequestStatsLine(@RequestBody ChartVO chartVO)
    {
        RestResp resp = new RestResp();
        TimeScope scope = TimeScope.getByCode(chartVO.getScope());
        if(scope != TimeScope.CUSTOM && scope != TimeScope.TODAY && scope != TimeScope.YESTERDAY &&
                scope != TimeScope.LATEST_7_DAYS && scope != TimeScope.LATEST_30_DAYS)
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
        clientService.getRequestStatsLine(range, unit, compareFrom, chartVO.getProductIds(), chartVO.getClientName(),
                resp);
        return resp;
    }

    @PostMapping(value = "/diagram/request/pie")
    public RestResp getRequestStatsPie(@RequestBody ChartVO chartVO)
    {
        RestResp resp = new RestResp();
        TimeScope scope = TimeScope.getByCode(chartVO.getScope());
        if(scope != TimeScope.CUSTOM && scope != TimeScope.TODAY && scope != TimeScope.YESTERDAY &&
                scope != TimeScope.LATEST_7_DAYS && scope != TimeScope.LATEST_30_DAYS)
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
        clientService.getRequestStatsPie(range, compareFrom, chartVO.getProductIds(), chartVO.getClientName(), resp);
        return resp;
    }

    @PostMapping(value = "/diagram/request/bar")
    public RestResp getRequestStatsBar(@RequestBody ChartVO chartVO)
    {
        RestResp resp = new RestResp();
        TimeScope scope = TimeScope.getByCode(chartVO.getScope());
        if(scope != TimeScope.CUSTOM && scope != TimeScope.TODAY && scope != TimeScope.YESTERDAY &&
                scope != TimeScope.LATEST_7_DAYS && scope != TimeScope.LATEST_30_DAYS)
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
        clientService.getRequestStatsBar(range, compareFrom, chartVO.getProductIds(), chartVO.getClientName(), resp);
        return resp;
    }

    @LoginRequired
    @GetMapping(value = "/diagram/index/client")
    public RestResp indexClient(@RequestParam(value = Field.PAGE_NUM, required = false) Integer pageNum,
            @RequestParam(value = Field.PAGE_SIZE, required = false) Integer pageSize)
    {
        RestResp res = new RestResp();
        clientService.getClientTraffic24h(new Page(pageNum, pageSize), res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/diagram/index/product")
    public RestResp indexProduct()
    {
        RestResp res = new RestResp();
        productService.getProductRatio1d(res);
        return res;
    }

    @LoginRequired
    @GetMapping(value = "/diagram/index/warning")
    public RestResp indexWarning()
    {
        RestResp res = new RestResp();
        systemService.getWarningManageList(res);
        return res;
    }

}
