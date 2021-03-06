package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientProductMapper
{
    void add(ClientProduct o);

    void updateSkipNull(ClientProduct o);

    ClientProduct findById(Long id);

    ClientProduct findByClientAndProduct(@Param("clientId") Long clientId, @Param("productId") Long productId);

    ClientProduct findByAppId(String appId);

    void deleteByIds(@Param("ids") Long[] ids);

    void addAll(@Param("list") List<ClientProduct> list);
}
