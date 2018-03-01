package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.RoleFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleFunctionMapper
{
    void addList(@Param("list") List<RoleFunction> list);

    List<RoleFunction> getByRole(Long roleId);

    void deleteByRole(Long roleId);
}
