package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;
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
    Integer getClientCountByDate(Date monthDay, Date currentDay);

    /**
     * 根据日期获取客户信息列表
     */
    ClientInfoListDTO getClientInfoListByDate(Date date, Date currentDay, Page page);

    /**
     * 根据日期获取充值总额
     */
    BigDecimal getClientRechargeStatsByDate(Date nowDay, Date currentDay);

    /**
     * 获取充值总额
     */
    BigDecimal getClientRechargeStatsAll();

    /**
     * 根据日期获取充值信息列表
     */
    ProductRechargeInfoListDTO getProductRechargeInfoListBy(Date date, Date currentDay, Page page);

    /**
     * 根据日期获取客户充值次数
     */
    Integer getClientRechargeCountByDate(Date date, Date currentDay);

    ListDTO<StatsDateInfoDTO> getRequestListStats(Date beforeDate, Date currentDay, String name, Long productId);
}
