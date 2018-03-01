package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper
{
    List<UserInfo> getListBy(@Param("roleId") Long roleId, @Param("enabled") Integer enabled);
}
