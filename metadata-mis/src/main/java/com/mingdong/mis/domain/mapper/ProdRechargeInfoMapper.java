package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ProdRechargeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProdRechargeInfoMapper
{
    List<ProdRechargeInfo> getBy(@Param("corpId") Long corpId, @Param("productId") Long productId);
}
