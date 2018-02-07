package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.UserProduct;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserProductMapper
{
    void add(UserProduct obj);

    void updateSkipNull(UserProduct obj);

    UserProduct findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    List<UserProduct> findListBy(@Param("userId") Long userId, @Param("productId") Long productId);

    void updateToken(@Param("updateDate") Date updateDate,@Param("token") String token,@Param("ids") List<Long> ids);
}
