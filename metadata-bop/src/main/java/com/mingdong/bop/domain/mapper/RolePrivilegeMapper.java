package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.RolePrivilege;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePrivilegeMapper
{
    void addList(@Param("list") List<RolePrivilege> list);

    List<RolePrivilege> getByRole(Long roleId);

    void deleteByRole(Long roleId);
}
