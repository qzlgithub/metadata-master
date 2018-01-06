package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.RolePrivilege;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePrivilegeMapper
{
    void add(RolePrivilege rolePrivilege);

    void addList(@Param("list") List<RolePrivilege> list);

    void delete(Long id);

    void updateById(RolePrivilege rolePrivilege);

    RolePrivilege findById(Long id);

    List<RolePrivilege> getAll();

    List<RolePrivilege> getByRole(Long roleId);

    void deleteByRole(Long roleId);
}
