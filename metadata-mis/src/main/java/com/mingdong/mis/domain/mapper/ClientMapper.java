package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Client;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientMapper
{
    void add(Client client);

    void updateById(Client client);

    void updateSkipNull(Client client);

    void setClientDeleted(@Param("idList") List<Long> idList, @Param("date") Date date);

    Client findById(Long id);

    List<Client> getListByIdList(@Param("idList") List<Long> idList);

    Client findByPrimaryAccount(Long primaryAccountId);
}
