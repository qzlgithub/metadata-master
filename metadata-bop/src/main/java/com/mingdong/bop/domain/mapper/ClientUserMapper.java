package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientUserMapper
{
    void add(ClientUser obj);

    void updateById(ClientUser obj);

    void updateSkipNull(ClientUser obj);

    ClientUser findById(Long id);

    ClientUser findByUsername(String username);

    List<ClientUser> getListByClientAndStatus(@Param("clientId") Long clientId, @Param("enabled") Integer enabled);

    void resetPasswordByIds(@Param("password") String password, @Param("date") Date date,
            @Param("idList") List<Long> idList);

    void updateStatusByIds(@Param("enabled") Integer enabled, @Param("date") Date date,
            @Param("idList") List<Long> idList);

    List<ClientUser> getListByClientsAndPrimary(@Param("clientIdList") List<Long> clientIdList);
}
