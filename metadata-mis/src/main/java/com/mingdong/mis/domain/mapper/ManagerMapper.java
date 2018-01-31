package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Manager;
import org.apache.ibatis.annotations.Param;

public interface ManagerMapper
{
    void add(Manager manager);

    void updateSkipNull(Manager manager);

    Manager findById(Long id);

    Manager findByUsername(String username);

    int countBy(@Param("roleId") Long roleId, @Param("enabled") Integer enabled);
}
