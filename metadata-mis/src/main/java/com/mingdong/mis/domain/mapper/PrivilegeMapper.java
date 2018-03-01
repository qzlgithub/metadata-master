package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Privilege;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PrivilegeMapper
{
    void updateSkipNull(Privilege privilege);

    void updateModuleStatusByIds(@Param("enabled") Integer enabled, @Param("data") Date date,
            @Param("idList") List<Long> idList);

    Privilege findById(Long id);

    List<Privilege> getParentIdByChildId(@Param("privilegeIdList") List<Long> privilegeIdList);

    List<Privilege> getListByParent(Long parentId);

    List<Privilege> getListByLevel(int level);

    List<Privilege> getModuleListByRole(Long roleId);
}
