package com.mingdong.core.service;

import com.mingdong.common.model.Page;
import com.mingdong.core.model.dto.ClientInfoListDTO;
import com.mingdong.core.model.dto.ProductRechargeInfoListDTO;

import java.math.BigDecimal;
import java.util.Date;

public interface RemoteStatsService
{
    Integer getAllClientCount();

    Integer getClientCountByDate(Date monthDay, Date currentDay);

    ClientInfoListDTO getClientInfoListByDate(Date date, Date currentDay, Page page);

    BigDecimal getClientRechargeStatsByDate(Date nowDay, Date currentDay);

    BigDecimal getClientRechargeStatsAll();

    ProductRechargeInfoListDTO getProductRechargeInfoListBy(Date date, Date currentDay, Page page);

    Integer getClientRechargeCountByDate(Date date, Date currentDay);
}
