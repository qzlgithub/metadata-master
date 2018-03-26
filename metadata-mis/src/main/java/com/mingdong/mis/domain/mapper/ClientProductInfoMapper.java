package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientProductInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ClientProductInfoMapper
{
    List<ClientProductInfo> getWillOverByDate(@Param("before") Date before, @Param("after") Date after);

    List<ClientProductInfo> getWillOverByTimes(@Param("amount") BigDecimal amount);
}
