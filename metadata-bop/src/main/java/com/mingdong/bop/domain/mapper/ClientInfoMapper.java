package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientInfoMapper
{
    int countBy(@Param("enabled") Integer enabled, @Param("account") String account, @Param("cropName") String cropName,
            @Param("shortName") String shortName, @Param("industryIdList") List<Long> industryIdList);

    List<ClientInfo> getListBy(@Param("enabled") Integer enabled, @Param("account") String account,
            @Param("cropName") String cropName, @Param("shortName") String shortName,
            @Param("industryIdList") List<Long> industryIdList);

    List<ClientInfo> getSimilarCorpByName(@Param("name") String name, @Param("clientId") Long clientId);

    List<ClientInfo> getClientInfoListByDate(@Param("date") Date date);
}
