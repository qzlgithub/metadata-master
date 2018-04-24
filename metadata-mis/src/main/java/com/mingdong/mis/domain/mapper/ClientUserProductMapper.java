package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientUserProduct;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientUserProductMapper
{
    void add(ClientUserProduct o);

    void updateSkipNull(ClientUserProduct o);

    void clearAccessToken(@Param("date") Date date, @Param("idList") List<Long> idList);

    ClientUserProduct findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    List<ClientUserProduct> getListByProduct(Long productId);

    List<ClientUserProduct> getListBy(@Param("clientId") Long clientId, @Param("productId") Long productId);

    ClientUserProduct findById(Long id);

    List<ClientUserProduct> getTokenListByClients(@Param("clientIdList") List<Long> clientIdList);

    List<ClientUserProduct> getTokenListByClientUser(Long clientUserId);
}
