package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientContact;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientContactMapper
{
    void addList(@Param("list") List<ClientContact> list);

    void updateById(ClientContact o);

    void deleteByIds(@Param("idList") List<Long> idList);

    ClientContact findById(Long id);

    List<ClientContact> getListByClient(@Param("clientId") Long clientId);
}
