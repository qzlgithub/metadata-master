package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientInfoMapper
{
    List<ClientInfo> getSimilarCorpByName(@Param("name") String name, @Param("clientId") Long clientId);

    List<ClientInfo> getClientInfoListByDate(@Param("start") Date start, @Param("end") Date end);

    List<ClientInfo> getListBy(@Param("keyword") String keyword, @Param("industryList") List<Long> industryList,
            @Param("enabled") Integer enabled);
}
