package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ManagerPrivilege;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerPrivilegeMapper
{
    void add(ManagerPrivilege managerPrivilege);

    void addList(@Param("list") List<ManagerPrivilege> list);

    void delete(Long id);

    void updateById(ManagerPrivilege managerPrivilege);

    ManagerPrivilege findById(Long id);

    List<ManagerPrivilege> getAll();

    List<ManagerPrivilege> getByIdList(@Param("privilegeIdList") List<Long> privilegeIdList);

    List<ManagerPrivilege> getPrivilegeIdListByManager(Long managerId);

    void deleteByManager(Long managerId);
}
