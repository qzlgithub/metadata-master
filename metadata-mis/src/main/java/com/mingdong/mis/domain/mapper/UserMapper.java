package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper
{
    void add(User user);

    void updateSkipNull(User user);

    User findById(Long id);

    User findByUsername(String username);

    int countBy(@Param("roleId") Long roleId, @Param("enabled") Integer enabled);
}
