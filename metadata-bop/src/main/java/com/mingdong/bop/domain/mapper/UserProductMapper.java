package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.UserProduct;

public interface UserProductMapper
{
    void add(UserProduct obj);

    void updateById(UserProduct obj);

    void updateSkipNull(UserProduct obj);
}
