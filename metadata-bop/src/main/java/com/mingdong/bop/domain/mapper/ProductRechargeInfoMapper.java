package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ProductRechargeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ProductRechargeInfoMapper
{

    List<ProductRechargeInfo> getListBy(@Param("clientId") Long clientId, @Param("productId") Long productId,
            @Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
