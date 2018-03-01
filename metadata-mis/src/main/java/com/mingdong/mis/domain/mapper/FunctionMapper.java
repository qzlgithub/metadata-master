package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Function;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface FunctionMapper
{
    void updateSkipNull(Function function);

    void updateModuleStatusByIds(@Param("enabled") Integer enabled, @Param("data") Date date,
            @Param("idList") List<Long> idList);

    Function findById(Long id);

    List<Function> getParentIdByChildId(@Param("privilegeIdList") List<Long> privilegeIdList);

    List<Function> getListByParent(Long parentId);

    List<Function> getListByLevel(int level);

    List<Function> getModuleListByRole(Long roleId);
}
