package com.mingdong.bop.controller;

import com.mingdong.bop.constant.Field;
import com.mingdong.bop.model.ChartVO;
import com.mingdong.bop.service.ClientService;
import com.mingdong.common.util.DateUtils;
import com.mingdong.core.constant.RangeUnit;
import com.mingdong.core.constant.RestResult;
import com.mingdong.core.model.DateRange;
import com.mingdong.core.model.RestResp;
import com.mingdong.core.util.DateRangeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class StatisticsController
{
    @Resource
    private ClientService clientService;

    /**
     * 客户数据统计：7日新增，当月新增，当前总数
     */
    @GetMapping(value = "/client/stats1.html")
    public ModelAndView getClientStats()
    {
        ModelAndView mv = new ModelAndView("setting/client1");
        Date date = new Date();
        Date weekTime = DateUtils.getNDaysEarlierStart(date, 6);
        Date monthTime = DateUtils.getMonthStart(date);
        int weekIncrement = clientService.getClientIncrementFrom(weekTime);
        int monthIncrement = clientService.getClientIncrementFrom(monthTime);
        int total = clientService.getClientIncrementFrom(null);
        mv.addObject(Field.WEEK_INCREMENT, weekIncrement);
        mv.addObject(Field.MONTH_INCREMENT, monthIncrement);
        mv.addObject(Field.TOTAL, total);
        return mv;
    }

    /**
     * 客户新增数走势图
     */
    @PostMapping(value = "/client/chart")
    public RestResp getClientChartData(@RequestBody ChartVO chartVO)
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
            range = DateRangeUtils.getLatestNDaysRange(days);
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

    /**
     * 客户新增数走势图
     */
    @PostMapping(value = "/recharge/chart")
    public RestResp getRechargeChartData(@RequestBody ChartVO chartVO)
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
            range = DateRangeUtils.getLatestNDaysRange(days);
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
        clientService.getClientRechargeTrend(range, compareFrom, rangeUnit, resp);
        return resp;
    }
}
