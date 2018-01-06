package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientProductMapper
{
    void add(ClientProduct clientProduct);

    void delete(Long id);

    void updateById(ClientProduct clientProduct);

    void updateSkipNull(ClientProduct obj);

    ClientProduct findById(Long id);

    List<ClientProduct> getAll();

    ClientProduct findByClientAndProduct(@Param("clientId") Long clientId, @Param("productId") Long productId);

}
