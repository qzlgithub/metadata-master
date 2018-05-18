package com.mingdong.mis.domain.mapper;

import com.mingdong.mis.domain.entity.Sistem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SistemMapper
{
    void updateClientUserMax(@Param("clientUserMax") String clientUserMax, @Param("date") Date date);

    void updateServiceQQ(@Param("serviceQQ") String serviceQQ, @Param("date") Date date);

    String getClientUserMax();

    String getServiceQQ();

    List<Sistem> getAllList();

    void updateTestToken(@Param("testToken") String testToken, @Param("date") Date date);

    String getTestToken();
}
