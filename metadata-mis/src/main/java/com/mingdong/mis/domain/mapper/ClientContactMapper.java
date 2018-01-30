package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.ClientContact;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientContactMapper
{
    void addList(@Param("list") List<ClientContact> list);

    List<ClientContact> getListByClient(@Param("clientId") Long clientId);
}
