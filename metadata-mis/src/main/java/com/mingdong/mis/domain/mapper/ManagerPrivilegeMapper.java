package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ManagerPrivilege;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerPrivilegeMapper
{
    void addList(@Param("list") List<ManagerPrivilege> list);

    void delete(Long id);

    List<ManagerPrivilege> getPrivilegeIdListByManager(Long managerId);

    void deleteByManager(Long managerId);
}
