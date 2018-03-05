package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.GroupFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupFunctionMapper
{
    void addList(@Param("list") List<GroupFunction> list);

    List<GroupFunction> getByGroupId(Long groupId);

    void deleteByGroupId(Long groupId);
}
