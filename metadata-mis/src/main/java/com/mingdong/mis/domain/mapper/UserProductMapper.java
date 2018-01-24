package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.UserProduct;
import org.apache.ibatis.annotations.Param;

public interface UserProductMapper
{
    void add(UserProduct obj);

    void updateSkipNull(UserProduct obj);

    UserProduct findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
