package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.UserFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFunctionMapper
{
    void addList(@Param("list") List<UserFunction> list);

    void deleteByManager(Long managerId);

    List<UserFunction> getListByUser(Long managerId);
}
