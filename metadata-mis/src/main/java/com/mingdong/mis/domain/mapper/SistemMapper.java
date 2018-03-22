package com.mingdong.mis.domain.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface SistemMapper
{
    void updateClientUserMax(@Param("clientUserMax") String clientUserMax, @Param("date") Date date);

    void updateServiceQQ(@Param("serviceQQ") String serviceQQ, @Param("date") Date date);

    String getClientUserMax();

    String getServiceQQ();

    String getFilePth();
}
