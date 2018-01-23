package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.UserProduct;
import org.apache.ibatis.annotations.Param;

public interface UserProductMapper
{
    void add(UserProduct obj);

    void updateSkipNull(UserProduct obj);

    UserProduct findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
