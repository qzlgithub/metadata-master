package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductInfoMapper
{

    List<ProductInfo> getListByEnabled(@Param("enabled") Integer enabled);

    int countByEnabled(@Param("enabled") Integer enabled);

}
