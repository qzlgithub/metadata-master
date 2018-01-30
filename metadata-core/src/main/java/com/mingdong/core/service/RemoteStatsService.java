package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;

import java.math.BigDecimal;
import java.util.Date;

public interface RemoteStatsService
{
    /**
     * 获取总客户数量
     *
     * @return
     */
    Integer getAllClientCount();

    /**
     * 根据日期获取客户数量
     *
     * @param monthDay
     * @param currentDay
     * @return
     */
    Integer getClientCountByDate(Date monthDay, Date currentDay);

    /**
     * 根据日期获取客户信息列表
     *
     * @param date
     * @param currentDay
     * @param page
     * @return
     */
    ClientInfoListDTO getClientInfoListByDate(Date date, Date currentDay, Page page);

    /**
     * 根据日期获取充值总额
     *
     * @param nowDay
     * @param currentDay
     * @return
     */
    BigDecimal getClientRechargeStatsByDate(Date nowDay, Date currentDay);

    /**
     * 获取充值总额
     *
     * @return
     */
    BigDecimal getClientRechargeStatsAll();

    /**
     * 根据日期获取充值信息列表
     *
     * @param date
     * @param currentDay
     * @param page
     * @return
     */
    ProductRechargeInfoListDTO getProductRechargeInfoListBy(Date date, Date currentDay, Page page);

    /**
     * 根据日期获取客户充值次数
     *
     * @param date
     * @param currentDay
     * @return
     */
    Integer getClientRechargeCountByDate(Date date, Date currentDay);
}
