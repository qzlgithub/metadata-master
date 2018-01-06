package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.Privilege;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrivilegeMapper
{
    void add(Privilege privilege);

    void delete(Long id);

    void updateById(Privilege privilege);

    Privilege findById(Long id);

    List<Privilege> getAll();

    List<Privilege> getByParent(Long parentId);

    List<Privilege> getParentIdByChildId(@Param("privilegeIdList") List<Long> privilegeIdList);

    List<Privilege> getTopListByRole(Long roleId);

    int countAll();

    List<Privilege> getByParentAndStatus(@Param("parentId") Long parengId, @Param("enabled") Integer enabled);

    void updateSkipNull(Privilege privilege);
}
