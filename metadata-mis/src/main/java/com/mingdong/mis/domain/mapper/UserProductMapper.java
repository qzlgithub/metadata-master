package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.UserProduct;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserProductMapper
{
    void add(UserProduct obj);

    void updateSkipNull(UserProduct obj);

    void clearAccessToken(@Param("date") Date date, @Param("idList") List<Long> idList);

    UserProduct findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    List<UserProduct> getListByProduct(Long productId);
}
