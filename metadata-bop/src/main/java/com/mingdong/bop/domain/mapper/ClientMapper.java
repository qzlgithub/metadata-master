package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.Client;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientMapper
{
    void add(Client client);

    void delete(Long id);

    void updateById(Client client);

    Client findById(Long id);

    List<Client> getAll();

    void updateSkipNull(Client client);

    void updateStatusByIds(@Param("idList") List<Long> idList, @Param("enabled") Integer enabled);

    void setClientDeleted(@Param("idList") List<Long> idList, @Param("date") Date date);
}
