package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Group;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupMapper
{
    void add(Group o);

    void updateSkipNull(Group o);

    Group findById(Long id);

    List<Group> getList();

    int countAll();

    Group findByName(String name);

    List<Group> getByStatus(@Param("enabled") Integer enabled);
}
