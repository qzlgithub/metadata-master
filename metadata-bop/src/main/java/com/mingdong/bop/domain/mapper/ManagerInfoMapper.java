package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ManagerInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerInfoMapper
{
    List<ManagerInfo> getListBy(@Param("roleId") Long roleId, @Param("enabled") Integer enabled);
}
