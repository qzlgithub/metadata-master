package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper
{
    void add(Role o);

    void updateSkipNull(Role o);

    Role findById(Long id);

    List<Role> getList();

    int countAll();

    Role findByName(String name);

    List<Role> getByStatus(@Param("enabled") Integer enabled);
}
