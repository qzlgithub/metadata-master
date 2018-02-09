package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientInfoMapper
{
    int countBy(@Param("enabled") Integer enabled, @Param("account") String account, @Param("cropName") String cropName,
            @Param("shortName") String shortName, @Param("industryIdList") List<Long> industryIdList);

    List<ClientInfo> getSimilarCorpByName(@Param("name") String name, @Param("clientId") Long clientId);

    List<ClientInfo> getClientInfoListByDate(@Param("start") Date start, @Param("end") Date end);

    int countBy(@Param("keyword") String keyword, @Param("industryList") List<Long> industryList,
            @Param("enabled") Integer enabled);

    List<ClientInfo> getListBy(@Param("keyword") String keyword, @Param("industryList") List<Long> industryList,
            @Param("enabled") Integer enabled);
}
