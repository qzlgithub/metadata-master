package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Client;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientMapper
{
    void add(Client o);

    void updateById(Client o);

    void updateSkipNull(Client o);

    void setClientDeleted(@Param("idList") List<Long> idList, @Param("date") Date date);

    Client findById(Long id);

    List<Client> getListByIdList(@Param("idList") List<Long> idList);

    Client findByPrimaryAccount(Long primaryAccountId);

    Client findByCorpNameOrCorpLicense(@Param("corpName") String corpName, @Param("license") String license);

    int countBy(@Param("keyword") String keyword, @Param("industryList") List<Long> industryList,
            @Param("enabled") Integer enabled, @Param("managerId") Long managerId);

    void updateStatusByIds(@Param("enabled") Integer enabled, @Param("date") Date date,
            @Param("idList") List<Long> idList);

    int countByStartTime(Date date);

    List<Client> findAll();
}
