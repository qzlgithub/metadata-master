package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoDTO;
import com.mingdong.core.model.dto.StatsDateInfoDTO;

import java.math.BigDecimal;
import java.util.Date;

public interface RemoteStatsService
{
    /**
     * 获取总客户数量
     */
    Integer getAllClientCount();

    /**
     * 根据日期获取客户数量
     */
    Integer getClientCountByDate(Date fromDate, Date toDate);

    /**
     * 根据日期获取客户信息列表
     */
    ClientInfoListDTO getClientInfoListByDate(Date fromDate, Date toDate, Page page);

    /**
     * 根据日期获取充值总额
     */
    BigDecimal getClientRechargeStatsByDate(Date fromDate, Date toDate);

    /**
     * 获取充值总额
     */
    BigDecimal getClientRechargeStatsAll();

    /**
     * 根据日期获取充值信息列表
     */
    ListDTO<ProductRechargeInfoDTO> getProductRechargeInfoListBy(Date fromDate, Date toDate, Page page);

    ListDTO<ProductRechargeInfoDTO> getRechargeInfoListBy(Date fromDate, Date toDate);

    /**
     * 根据日期获取客户充值次数
     */
    Integer getClientRechargeCountByDate(Date fromDate, Date toDate);

    ListDTO<StatsDateInfoDTO> getRequestListStats(Date fromDate, Date toDate, String name, Long productId);

    ListDTO<StatsDateInfoDTO> getRevenueListStats(Date fromDate, Date toDate);

    void statsDataForHour(Date date);
}
