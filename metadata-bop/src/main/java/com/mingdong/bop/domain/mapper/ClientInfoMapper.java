package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientInfoMapper
{
    void add(ClientInfo clientInfo);

    void delete(Long clientId);

    void updateById(ClientInfo clientInfo);

    ClientInfo findById(@Param("clientId") Long clientId);

    int countBy(@Param("enabled") Integer enabled, @Param("account") String account, @Param("cropName") String cropName,
            @Param("shortName") String shortName, @Param("industryIdList") List<Long> industryIdList);

    List<ClientInfo> getListBy(@Param("enabled") Integer enabled, @Param("account") String account,
            @Param("cropName") String cropName, @Param("shortName") String shortName,
            @Param("industryIdList") List<Long> industryIdList);

    ClientInfo editClient(@Param("corpId") Long corpId, @Param("corpName") String corpName,
            @Param("shortName") String shortName, @Param("industryId") Long industryId,
            @Param("account") String contact, @Param("phone") String phone, @Param("email") String email,
            @Param("license") String license, @Param("clientEnabled") Integer clientEnabled,
            @Param("accountEnabled") Integer accountEnabled);

    ClientInfo getById(@Param("clientId") Long clientId);

    ClientInfo updateDelById(@Param("clientId") Long clientId);

    ClientInfo updateEnaById(@Param("clientId") Long clientId);

    List<ClientInfo> getSimilarCorpByName(@Param("name") String name, @Param("clientId") Long clientId);
}
