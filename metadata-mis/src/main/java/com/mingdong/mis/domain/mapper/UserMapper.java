package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper
{
    void add(User o);

    void updateSkipNull(User o);

    User findById(Long id);

    User findByUsername(String username);

    int countBy(@Param("roleType") Integer roleType, @Param("enabled") Integer enabled);

    List<User> getListBy(@Param("roleType") Integer roleType, @Param("enabled") Integer enabled);

    List<User> getListByIds(@Param("ids") List<Long> managerIds);

    List<User> getListByAlarm(@Param("alarm") Integer alarm, @Param("enabled") Integer enabled);

}
