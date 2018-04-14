package com.mingdong.bop.manage.impl;

import com.mingdong.backend.model.SummaryStatsDTO;
import com.mingdong.backend.service.BEDStatsService;
import com.mingdong.bop.constant.Field;
import com.mingdong.bop.manage.StatisticsManage;
import com.mingdong.common.util.NumberUtils;
import com.mingdong.core.model.RestResp;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class StatisticsManageImpl implements StatisticsManage
{
    @Resource
    private BEDStatsService bedStatsService;
    /*@Resource
    private BEDDiagramService bedDiagramService;*/

    @Override
    public Map<String, Object> getSummaryStatistics()
    {
        Map<String, Object> map = new HashMap<>();
        SummaryStatsDTO stats = bedStatsService.getSummaryStatisticsInfo();
        map.put(Field.CLIENT_INC_30D, stats.getClientIncIn30Days() == null ? 0 : stats.getClientIncIn30Days());
        map.put(Field.CLIENT_TOTAL, stats.getClientTotal() == null ? 0 : stats.getClientTotal());
        map.put(Field.RECHARGE_7D, NumberUtils.formatAmount(stats.getRechargeAmountIn7Days()));
        map.put(Field.RECHARGE_30D, NumberUtils.formatAmount(stats.getRechargeAmountIn30Days()));
        map.put(Field.PROFIT_TODAY, NumberUtils.formatAmount(stats.getProfitAmountToday()));
        map.put(Field.PROFIT_YESTERDAY, NumberUtils.formatAmount(stats.getProfitAmountYesterday()));
        // 请求
        map.put(Field.REQUEST_TODAY, stats.getRequestToday() == null ? 0 : stats.getRequestToday());
        map.put(Field.REQUEST_YESTERDAY, stats.getRequestYesterday() == null ? 0 : stats.getRequestYesterday());
        map.put(Field.REQUEST_THIS_MONTH, stats.getRequestThisMonth() == null ? 0 : stats.getRequestThisMonth());
        map.put(Field.REQUEST_TOTAL, stats.getRequestTotal() == null ? 0 : stats.getRequestTotal());
        // 失败请求
        map.put(Field.REQUEST_FAILED_TODAY, stats.getRequestFailedToday() == null ? 0 : stats.getRequestFailedToday());
        map.put(Field.REQUEST_FAILED_YESTERDAY,
                stats.getRequestFailedYesterday() == null ? 0 : stats.getRequestFailedYesterday());
        map.put(Field.REQUEST_FAILED_THIS_MONTH,
                stats.getRequestFailedThisMonth() == null ? 0 : stats.getRequestFailedThisMonth());
        map.put(Field.REQUEST_FAILED_TOTAL, stats.getRequestFailedTotal() == null ? 0 : stats.getRequestFailedTotal());
        // 第三方失败请求
        map.put(Field.REQUEST_3RD_FAILED_TODAY,
                stats.getRequest3rdFailedToday() == null ? 0 : stats.getRequest3rdFailedToday());
        map.put(Field.REQUEST_3RD_FAILED_YESTERDAY,
                stats.getRequest3rdFailedYesterday() == null ? 0 : stats.getRequest3rdFailedYesterday());
        map.put(Field.REQUEST_3RD_FAILED_THIS_MONTH,
                stats.getRequest3rdFailedThisMonth() == null ? 0 : stats.getRequest3rdFailedThisMonth());
        map.put(Field.REQUEST_3RD_FAILED_TOTAL,
                stats.getRequest3rdFailedTotal() == null ? 0 : stats.getRequest3rdFailedTotal());
        return map;
    }

    @Override
    public void getThirdRequestDiagramIn24h(RestResp resp)
    {
        resp.addData(Field.REQUEST_TOTAL, 723324L);
        resp.addData(Field.REQUEST_FAILED_TOTAL, 232L);
        resp.addData(Field.X_AXIS_DATA,
                new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:30", "08:00", "09:00",
                        "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00",
                        "20:00", "21:00", "22:00", "23:00"});
        resp.addData(Field.SERIES_DATA,
                new int[]{100, 200, 300, 400, 500, 300, 550, 500, 400, 390, 380, 390, 400, 500, 600, 750, 800, 700, 600,
                        400, 800, 700, 600, 400});
    }
}
