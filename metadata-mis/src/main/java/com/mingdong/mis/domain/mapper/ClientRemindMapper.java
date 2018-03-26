package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientRemind;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientRemindMapper
{
    void add(ClientRemind clientRemind);

    List<ClientRemind> getListByDispose(Integer dispose);

    void deleteByIds(@Param("ids") List<Long> ids);

    void updateSkipNull(ClientRemind clientRemind);

    ClientRemind getClientRemindById(Long id);
}
