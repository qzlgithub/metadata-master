package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.Manager;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerMapper
{
    void add(Manager manager);

    void delete(Long id);

    void updateById(Manager manager);

    Manager findById(Long id);

    List<Manager> getAll();

    Manager findByUsername(String username);

    int countBy(@Param("roleId") Long roleId, @Param("enabled") Integer enabled);

    void updateSkipNull(Manager manager);
}
