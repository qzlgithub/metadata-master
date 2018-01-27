package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Privilege;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PrivilegeMapper
{
    Privilege findById(Long id);

    List<Privilege> getByParent(Long parentId);

    List<Privilege> getParentIdByChildId(@Param("privilegeIdList") List<Long> privilegeIdList);

    List<Privilege> getTopListByRole(Long roleId);

    List<Privilege> getByParentAndStatus(@Param("parentId") Long parentId, @Param("enabled") Integer enabled);

    void updateSkipNull(Privilege privilege);

    List<Privilege> getListByLevel(int level);

    void updateModuleStatusByIds(@Param("enabled") Integer enabled, @Param("data") Date date,
            @Param("idList") List<Long> idList);
}
