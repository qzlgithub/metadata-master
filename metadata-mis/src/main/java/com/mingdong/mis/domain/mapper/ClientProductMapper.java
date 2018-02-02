package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientProduct;
import org.apache.ibatis.annotations.Param;

public interface ClientProductMapper
{
    void add(ClientProduct clientProduct);

    void updateSkipNull(ClientProduct obj);

    ClientProduct findById(Long id);

    ClientProduct findByClientAndProduct(@Param("clientId") Long clientId, @Param("productId") Long productId);

    ClientProduct findByAppId(String appId);
}
