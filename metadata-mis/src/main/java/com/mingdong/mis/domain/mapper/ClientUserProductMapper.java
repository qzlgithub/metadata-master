package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientUserProduct;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientUserProductMapper
{
    void add(ClientUserProduct obj);

    void updateSkipNull(ClientUserProduct obj);

    void clearAccessToken(@Param("date") Date date, @Param("idList") List<Long> idList);

    ClientUserProduct findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    List<ClientUserProduct> getListByProduct(Long productId);
}
