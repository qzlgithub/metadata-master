package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientRemindInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientRemindInfoMapper
{
    List<ClientRemindInfo> getListBy(@Param("managerId") Long managerId, @Param("keyword") String keyword,
            @Param("type") Integer type, @Param("date") Date date, @Param("dispose") Integer dispose);

    int countBy(@Param("managerId") Long managerId, @Param("keyword") String keyword, @Param("type") Integer type,
            @Param("date") Date date, @Param("dispose") Integer dispose);
}
