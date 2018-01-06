package com.mingdong.bop.domain.mapper;

import com.mingdong.bop.domain.entity.ClientUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClientUserMapper
{
    void add(ClientUser corpUser);

    void delete(Long id);

    void updateById(ClientUser corpUser);

    ClientUser findById(Long id);

    List<ClientUser> findBy(Long id, Integer primary);

    List<ClientUser> getAll();

    ClientUser findByUsername(String username);

    void updateSkipNull(ClientUser corpUser);

    ClientUser findMasterUser(Long clientId);

    boolean isPrimAccoNo();

    List<ClientUser> getListByClientAndStatus(@Param("clientId") Long clientId, @Param("enabled") Integer enabled);

    void resetMasterPasswordByClient(@Param("clientIdList") List<Long> clientIdList, @Param("password") String password,
            @Param("date") Date date);
}
